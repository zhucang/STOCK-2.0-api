package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserAuthRecordLogDict;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserAuthRecord;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserAuthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户实名认证信息Controller
 * 
 * @author ruoyi
 * @date 2024-04-05
 */
@RestController
@RequestMapping("/system/userAuthRecord")
public class UserAuthRecordController extends BaseController
{
    @Autowired
    private IUserAuthRecordService userAuthRecordService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询用户实名认证信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:userAuthRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserAuthRecord userAuthRecord)
    {
        startPage();
        PageUtils.getLocalPage().setUnsafeOrderBy("field(`auth_status`,0,1,2),id desc");
        List<UserAuthRecord> list = userAuthRecordService.selectUserAuthRecordList(userAuthRecord);
        return getDataTable(list);
    }

//    /**
//     * 导出用户实名认证信息列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:userAuthRecord:export')")
//    @Log(title = "用户实名认证信息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, UserAuthRecord userAuthRecord)
//    {
//        List<UserAuthRecord> list = userAuthRecordService.selectUserAuthRecordList(userAuthRecord);
//        ExcelUtil<UserAuthRecord> util = new ExcelUtil<UserAuthRecord>(UserAuthRecord.class);
//        util.exportExcel(response, list, "用户实名认证信息数据");
//    }

    /**
     * 获取用户实名认证信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userAuthRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userAuthRecordService.selectUserAuthRecordById(id));
    }

    /**
     * 根据用户id获取用户实名认证信息
     */
    @PreAuthorize("@ss.hasAnyPermi('system:userAuthRecord:getInfoByUserId,system:touristsAuthRecord:getInfoByUserId')")
    @GetMapping(value = "/getInfoByUserId")
    public AjaxResult getInfoByUserId(Long userId,Integer authLevel)
    {
        return success(userAuthRecordService.selectLastOne(userId,authLevel));
    }

//    /**
//     * 新增用户实名认证信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:userAuthRecord:add')")
//    @Log(title = "用户实名认证信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody UserAuthRecord userAuthRecord)
//    {
//        return toAjax(userAuthRecordService.insertUserAuthRecord(userAuthRecord));
//    }

//    /**
//     * 修改用户实名认证信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:userAuthRecord:edit')")
//    @Log(title = "用户实名认证信息", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody UserAuthRecord userAuthRecord)
//    {
//        return toAjax(userAuthRecordService.updateUserAuthRecord(userAuthRecord));
//    }

    /**
     * 删除用户实名认证信息
     */
    @PreAuthorize("@ss.hasPermi('system:userAuthRecord:remove')")
    @Log(title = "用户实名认证信息", businessType = BusinessType.DELETE,dict = UserAuthRecordLogDict.class,
            saveParamNames = {"userNo","idNumber","realName","img1Key","img2Key","img3Key","authStatus","authMethod","authLevel","userAuthRecords"})
    @RepeatSubmit
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userAuthRecordService.deleteUserAuthRecordByIds(ids));
    }

    /**
     * 用户实名认证审核
     */
    @PreAuthorize("@ss.hasPermi('system:userAuthRecord:userAuthReview')")
    @Log(title = "用户实名认证审核", businessType = BusinessType.UPDATE,dict = UserAuthRecordLogDict.class,
            saveParamNames = {"userNo","idNumber","realName","img1Key","img2Key","img3Key","authStatus","authMethod","authLevel"})
    @RepeatSubmit
	@PostMapping("/userAuthReview")
    public AjaxResult userAuthReview(@RequestBody UserAuthRecord userAuthRecord)
    {
        if (userAuthRecord.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (userAuthRecord.getAuthStatus() == null){
            throw new ServiceException("请选择实名状态");
        }
//        if (userAuthRecord.getIsActive().equals(3)){
//            if (StringUtils.isEmpty(userAuthRecord.getAuthMsg())) {
//                throw new RuntimeException("审核失败信息必填");
//            }
//        }
        //用户实名认证是否填写真实姓名
        Integer selectSwitchStatusById117 = switchSetService.selectSwitchStatusById(117L);
        if (selectSwitchStatusById117.equals(0)){
            if (StringUtils.isEmpty(userAuthRecord.getRealName())){
                throw new ServiceException("请输入真实姓名");
            }else {
                //去除头尾空格
                userAuthRecord.setRealName(userAuthRecord.getRealName().trim());
            }
        }
        return toAjax(userAuthRecordService.userAuthReview(userAuthRecord));
    }
}
