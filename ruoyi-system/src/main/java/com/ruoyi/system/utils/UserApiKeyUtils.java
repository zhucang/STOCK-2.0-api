package com.ruoyi.system.utils;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.UserApiKeyMapper;

public class UserApiKeyUtils extends SecurityUtils {


    private static UserApiKeyMapper userApiKeyMapper = SpringUtils.getBean(UserApiKeyMapper.class);

    /**
     * 用户ID
     **/
    public static Long getUserId()
    {
        try
        {
            return getLoginUser().getUserId();
        }
        catch (Exception e)
        {
            try{
                //用户ID
                Long userId = null;
                //请求头获取appId和apikey
                String appId = HttpUtils.getHttpServletRequest().getHeader("appId");
                String apiKey = HttpUtils.getHttpServletRequest().getHeader("apiKey");
                if (StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(apiKey)){
                    userId = userApiKeyMapper.selectUserIdByApiKey(appId,apiKey);
                }
                if (userId != null){
                    HttpUtils.getHttpServletRequest().setAttribute("useUserApiKey","true");
                    return userId;
                }else {
                    throw new ServiceException();
                }
            }catch (Exception ex){
                throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
            }
        }
    }



}
