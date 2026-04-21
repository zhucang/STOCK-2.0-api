package com.ruoyi.framework.aspectj;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessStatus;
import com.ruoyi.common.enums.HttpMethod;
import com.ruoyi.common.filter.PropertyPreExcludeFilter;
import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysOperLog;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 操作日志记录处理
 * 
 * @author ruoyi
 */
@Aspect
@Component
public class LogAspect
{
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /** 排除敏感属性字段 */
    public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };

    /** 计算操作消耗时间 */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog)
    {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult)
    {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     * 
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e)
    {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult)
    {
        try
        {
            // 获取当前的用户
            LoginUser loginUser = null;
            try {
                loginUser = SecurityUtils.getLoginUser();
            }catch (Exception exception){

            }
            // *========数据库日志=========*//
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = IpUtils.getIpAddr();
            operLog.setOperIp(ip);
            operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            if (loginUser != null)
            {
                operLog.setOperName(loginUser.getUsername());
                //如果是app用户，设置关联用户id
                if (loginUser.getAppUser() != null){
                    operLog.setRelateAppUserId(loginUser.getUserId());
                }
            }else {
                Object userAccount = HttpUtils.getRequestLogParams().get("userAccount");
                if (userAccount == null){
                    userAccount = "未登录用户";
                }
                operLog.setOperName(String.valueOf(userAccount));
            }

            if (e != null)
            {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            //字段字典匹配
            try {
                paramsMatchDict(controllerLog,operLog);
            }catch (Exception ex){
                System.out.println(ex.getStackTrace());
            }
            // 设置消耗时间
            operLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
        finally
        {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * 
     * @param log 日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operLog, Object jsonResult) throws Exception
    {
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData())
        {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog, log.excludeParamNames(),log);
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult))
        {
            operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 字段字典匹配
     * @param log 日志
     * @param operLog 操作日志
     */
    public void paramsMatchDict(Log log, SysOperLog operLog) throws Exception {
        //请求参数jsonObj
        JSONObject operParamJsonObj = null;
        //参数map
        Object paramsMap = operLog.getParams().get("paramsMap");
        //日志附加参数
        JSONObject logParams = HttpUtils.getRequestLogParams();
        if (paramsMap != null){
            operParamJsonObj = (JSONObject) paramsMap;
        }else {
            if (logParams.size() == 0){
                return;
            }else {
                operParamJsonObj = logParams;
            }
        }
        //字典
        AbstractLogDictMap abstractLogDictMap = log.dict().newInstance();
        //需要保存的参数名称
        List<String> saveParamNames = Arrays.asList(log.saveParamNames());
        //遍历
        for (Map.Entry<String, Object> objectEntry : logParams.entrySet()) {
            //日志key
            String key = objectEntry.getKey();
            //日志value
            Object value = objectEntry.getValue();
            try {
                //如果是json数组
                if (key.contains("JSONArray:")){
                    //日志value
                    JSONArray jsonArray = JSONArray.parseArray(String.valueOf(value));
                    jsonArray = arraysParamsMatchDict(abstractLogDictMap, saveParamNames, jsonArray);
                    operParamJsonObj.put(key.replace("JSONArray:",""),jsonArray);
                }else {
                    //如果是json对象，解析后全部插入
                    JSONObject jsonObject = JSONObject.parseObject(String.valueOf(objectEntry.getValue()));
                    operParamJsonObj.putAll(jsonObject);
                }
            }catch (Exception e){
                //如果只是键值对，直接插入
                operParamJsonObj.put(objectEntry.getKey(),objectEntry.getValue());
            }
        }
        //结果
        JSONObject result = new JSONObject();
        //遍历
        for (Map.Entry<String, Object> entry : operParamJsonObj.entrySet()) {
            //参数字段
            String paramKey = entry.getKey();
            if (saveParamNames.size() > 0 && !saveParamNames.contains(paramKey)){
                continue;
            }
            //参数值
            String paramValue = String.valueOf(entry.getValue());
            //参数值对应字典
            String paramValueDictValue = abstractLogDictMap.get(paramKey,paramValue);
            //如果参数名称有对应字典
            if (StringUtils.isNotEmpty(paramValueDictValue)){
                paramValue = paramValueDictValue;
            }
            //参数名称对应字典
            String paramNameDictValue = abstractLogDictMap.get(paramKey);
            //如果参数名称有对应字典
            if (StringUtils.isNotEmpty(paramNameDictValue)){
                paramKey = paramNameDictValue;
            }
            result.put(paramKey,paramValue);
        }
        operLog.setOperParamTranslate(StringUtils.substring(JSON.toJSONString(result), 0, 3000));
    }

    /**
     * 数组字段字典匹配
     * @param abstractLogDictMap 字典
     * @param saveParamNames 需要保存的参数名称
     * @param jsonArray json数组
     * @throws Exception
     */
    public JSONArray arraysParamsMatchDict(AbstractLogDictMap abstractLogDictMap, List<String> saveParamNames,JSONArray jsonArray){
        //结果
        JSONArray result = new JSONArray();
        //遍历取字典
        for (int i = 0; i < jsonArray.size(); i++) {
            //参数obj
            JSONObject operParamJsonObj = jsonArray.getJSONObject(i);
            //结果集对象
            JSONObject jsonObject = new JSONObject();
            //遍历
            for (Map.Entry<String, Object> entry : operParamJsonObj.entrySet()) {
                //参数字段
                String paramKey = entry.getKey();
                //验证是否保存
                if (saveParamNames.size() > 0 && !saveParamNames.contains(paramKey)){
                    continue;
                }
                //参数值
                String paramValue = String.valueOf(entry.getValue());
                //参数值对应字典
                String paramValueDictValue = abstractLogDictMap.get(paramKey,paramValue);
                //如果参数名称有对应字典
                if (StringUtils.isNotEmpty(paramValueDictValue)){
                    paramValue = paramValueDictValue;
                }
                //参数名称对应字典
                String paramNameDictValue = abstractLogDictMap.get(paramKey);
                //如果参数名称有对应字典
                if (StringUtils.isNotEmpty(paramNameDictValue)){
                    paramKey = paramNameDictValue;
                }
                jsonObject.put(paramKey,paramValue);
            }
            result.add(jsonObject);
        }
        return result;
    }

    /**
     * 获取请求的参数，放到log中
     * 
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog, String[] excludeParamNames,Log log) throws Exception
    {
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        //涉及的app用户id`
        Object relateAppUserId = null;
        //请求方法
        String requestMethod = operLog.getRequestMethod();
        if (StringUtils.isEmpty(paramsMap)
                && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)))
        {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            try {
                JSONObject jsonObject = JSONObject.parseObject(params);
                operLog.getParams().put("paramsMap",jsonObject);
                relateAppUserId = jsonObject.get(log.relateAppUserId());
            }catch (Exception e){
                System.out.println(e.getStackTrace());
            }
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        }
        else
        {
            operLog.getParams().put("paramsMap",JSONObject.parseObject(JSONObject.toJSONString(paramsMap)));
            relateAppUserId = paramsMap.get(log.relateAppUserId());
            operLog.setOperParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
        if (relateAppUserId == null){
            relateAppUserId = HttpUtils.getRequestLogParams().get(log.relateAppUserId());
        }
        //如果涉及的app用户id是空
        if (operLog.getRelateAppUserId() == null){
            //请求方法
            String method = operLog.getMethod();
            //过滤urls
            String[] saveRelateAppUserIdFilter = {"SysUserController","SysRoleController","SysRegisterController","SysProfileController","SysPostController","SysLoginController","SysDeptController","AgentUserController"};
            //如果用户id不为空，并且路径允许，则保存
            if (relateAppUserId != null && Arrays.stream(saveRelateAppUserIdFilter).filter(a->method.contains(a)).count() == 0){
                try {
                    operLog.setRelateAppUserId(Long.valueOf(String.valueOf(relateAppUserId)));
                }catch (Exception e){
                    System.out.println(e.getStackTrace());
                }
            }
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (Object o : paramsArray)
            {
                if (StringUtils.isNotNull(o) && !isFilterObject(o))
                {
                    try
                    {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params += jsonObj.toString() + " ";
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames)
    {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * 判断是否需要过滤的对象。
     * 
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        Class<?> clazz = o.getClass();
        if (clazz.isArray())
        {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        else if (Collection.class.isAssignableFrom(clazz))
        {
            Collection collection = (Collection) o;
            for (Object value : collection)
            {
                return value instanceof MultipartFile;
            }
        }
        else if (Map.class.isAssignableFrom(clazz))
        {
            Map map = (Map) o;
            for (Object value : map.entrySet())
            {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
