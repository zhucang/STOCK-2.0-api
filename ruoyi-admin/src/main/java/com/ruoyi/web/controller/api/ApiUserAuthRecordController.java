package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.UserAuthRecordLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserAuthRecord;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserAuthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户实名认证信息Controller
 * 
 * @author ruoyi
 * @date 2024-04-05
 */
@RestController
@RequestMapping("/api/userAuthRecord")
public class ApiUserAuthRecordController extends BaseController
{
    @Autowired
    private IUserAuthRecordService userAuthRecordService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询用户实名认证信息
     */
    @GetMapping(value = "/detail")
    public AjaxResult detail(Integer authLevel) {
        if (authLevel == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择认证等级");
        }
        return AjaxResult.success(userAuthRecordService.selectLastOne(SecurityUtils.getUserId(),authLevel));
    }

    /**
     * 用户实名认证申请
     */
    @PostMapping(value = "/userAuthApply")
    @RepeatSubmit
    @Log(title = "用户实名认证申请", businessType = BusinessType.OTHER,dict = UserAuthRecordLogDict.class,
            saveParamNames = {"id","idNumber","realName","img1Key","img2Key","img3Key","authStatus","authMethod","authLevel"})
    public AjaxResult userAuthApply(@RequestBody UserAuthRecord userAuthRecord) {
        //用户id
        userAuthRecord.setUserId(SecurityUtils.getUserId());
        if (userAuthRecord.getAuthMethod() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择认证方式");
        }
        if (userAuthRecord.getAuthLevel() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择认证等级");
        }
        if (StringUtils.isEmpty(userAuthRecord.getIdNumber())){
            throw new LangException(HintConstants.PARAM_NULL,"请输入证件号码");
        }
        //用户实名认证是否填写真实姓名
        Integer selectSwitchStatusById117 = switchSetService.selectSwitchStatusById(117L);
        if (selectSwitchStatusById117.equals(0)){
            if (StringUtils.isEmpty(userAuthRecord.getRealName())){
                throw new LangException(HintConstants.PARAM_NULL,"请输入真实姓名");
            }else {
                //去除头尾空格
                userAuthRecord.setRealName(userAuthRecord.getRealName().trim());
            }
        }
        //初级认证
        if (userAuthRecord.getAuthLevel().equals(0)){

        }else if (userAuthRecord.getAuthLevel().equals(1)){
            //高级认证
            if (StringUtils.isEmpty(userAuthRecord.getImg1Key())){
                throw new LangException(HintConstants.PARAM_NULL,"请上传证件正面图片");
            }
            //用户实名认证是否上传背面身份证
            Integer selectSwitchStatusById118 = switchSetService.selectSwitchStatusById(118L);
            if (selectSwitchStatusById118.equals(0)){
                if (StringUtils.isEmpty(userAuthRecord.getImg2Key())){
                    throw new LangException(HintConstants.PARAM_NULL,"请上传证件反面图片");
                }
            }
            //用户实名认证是否上传手持身份证
            Integer selectSwitchStatusById63 = switchSetService.selectSwitchStatusById(63L);
            if (selectSwitchStatusById63.equals(0)){
                if (StringUtils.isEmpty(userAuthRecord.getImg3Key())){
                    throw new LangException(HintConstants.PARAM_NULL,"请上传手持身份证图片");
                }
            }
        }
        return toAjax(userAuthRecordService.userAuthApply(userAuthRecord));
    }
}
