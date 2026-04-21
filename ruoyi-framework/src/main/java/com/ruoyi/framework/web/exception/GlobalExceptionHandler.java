package com.ruoyi.framework.web.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.DemoModeException;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 * 
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        if (requestURI.contains("/api/")){
            return AjaxResult.error("hint_operationExceptionTryAgain",e.getMessage());
        }else {
            return AjaxResult.error("系统错误",e.getMessage());
        }
//        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        Integer code = e.getCode();
        //如果需要打印日志
        if (e.getNeedPrintDebugLog() != null && e.getNeedPrintDebugLog() == true){
            log.error(e.getMessage(), e);
        }
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * 多语言异常
     */
    @ExceptionHandler(LangException.class)
    public AjaxResult handleLangException(LangException e, HttpServletRequest request)
    {
        return AjaxResult.error(e.getLangKey(),e.getParams(),e.getMsg());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        if (requestURI.contains("/api/")){
            return AjaxResult.error("hint_operationExceptionTryAgain",e.getMessage());
        }else {
            return AjaxResult.error("系统错误",e.getMessage());
        }
//        return AjaxResult.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), e.getValue()));
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        //请求路径
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        if (requestURI.contains("/api/")){
            return AjaxResult.error("hint_operationExceptionTryAgain",e.getMessage());
        }else {
            if(ExceptionUtil.getRootCauseMessage(e).startsWith("RuntimeException:")){
                String message = ExceptionUtil.getRootCauseMessage(e).replace("RuntimeException: ","");
                return AjaxResult.error(message,e.getMessage());
            }else {
                if (StringUtils.isNotEmpty(e.getMessage()) && e.getMessage().length() > 30){
                    return AjaxResult.error("系统错误",e.getMessage());
                }else {
                    return AjaxResult.error(e.getMessage());
                }

            }
        }
//        return AjaxResult.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        //请求路径
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        if (requestURI.contains("/api/")){
            return AjaxResult.error("hint_operationExceptionTryAgain",e.getMessage());
        }else {
            return AjaxResult.error("系统错误",e.getMessage());
        }
//        return AjaxResult.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e,HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
//        String message = e.getAllErrors().get(0).getDefaultMessage();
        //请求路径
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/")){
            return AjaxResult.error("hint_operationExceptionTryAgain",e.getMessage());
        }else {
            return AjaxResult.error("输入格式错误",e.getMessage());
        }
//        return AjaxResult.error(message);
    }

    /**
     * 请求参数验证异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e,HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
//        String message = e.getAllErrors().get(0).getDefaultMessage();
        //请求路径
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/")){
            return AjaxResult.error("hint_39",e.getMessage());
        }else {
            return AjaxResult.error("请检查输入格式",e.getMessage());
        }
//        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("演示模式，不允许操作");
    }
}
