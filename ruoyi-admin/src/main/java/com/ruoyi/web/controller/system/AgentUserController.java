package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.SysUserLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IAgentUserService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代理信息
 *
 * @author ruoyi
 * 日志优化完成
 */
@RestController
@RequestMapping("/system/agentUser")
public class AgentUserController extends BaseController {

    @Autowired
    private IAgentUserService agentUserService;

    @Autowired
    private ISysUserService userService;

    /**
     * 获取代理用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user)
    {
        startPage();
        startOrderBy("user_id desc");
        user.setDeptId(3L);
        List<SysUser> list = agentUserService.selectAgentUserList(user);
        agentUserService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 获取代理用户信息列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(SysUser user)
    {
        startPage();
        startOrderBy("user_id desc");
        user.getParams().put("agentData",1);
        user.setStatus("0");
        List<SysUser> list = agentUserService.selectAgentUserList(user);
        return getDataTable(list);
    }

    /**
     * 获取代理用户信息列表
     */
    @GetMapping("/listGroup")
    public TableDataInfo listGroup(SysUser user)
    {
        startOrderBy("user_id desc");
        user.getParams().put("agentData",1);
        List<SysUser> list = agentUserService.selectAgentUserList(user);
        list = agentUserService.buildAgentTree(list);
        return getDataTable(list);
    }

    /**
     * 获取代理用户信息详情
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(agentUserService.selectAgentUserById(id));
    }

    /**
     * 新增代理用户信息
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:add')")
    @Log(title = "新增代理用户信息", businessType = BusinessType.INSERT,dict = SysUserLogDict.class,
            saveParamNames = {"userId","userName","nickName","email","phonenumber","password","inviteCode","status"})
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody SysUser user)
    {
        //用户名
        String userName = user.getUserName();
        if (StringUtils.isEmpty(userName)){
            throw new ServiceException("请输入用户账号");
        }else {
            //去除头尾空格
            user.setUserName(userName.trim());
        }
        if (StringUtils.isEmpty(user.getPassword())){
            throw new ServiceException("请输入登录密码");
        }
        if (!userService.checkUserNameUnique(user))
        {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        //如果当前登陆者是代理,则是添加代理员工
        if (SecurityUtils.getDeptId().equals(3L)){
            user.setSupUserId(SecurityUtils.getUserId());
            return addLowerAgent(user);
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setDeptId(3L);
        user.setRoleIds(new Long[]{100L});
        return toAjax(agentUserService.insertAgentUser(user));
    }

    /**
     * 添加下级代理
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:addLowerAgent')")
    @Log(title = "添加下级代理", businessType = BusinessType.INSERT,dict = SysUserLogDict.class,
            saveParamNames = {"userId","userName","nickName","email","phonenumber","password","inviteCode","status","supUserId","上级账号"})
    @RepeatSubmit
    @PostMapping(value = "addLowerAgent")
    public AjaxResult addLowerAgent(@RequestBody SysUser user)
    {
        //用户名
        String userName = user.getUserName();
        if (StringUtils.isEmpty(userName)){
            throw new ServiceException("请输入用户账号");
        }else {
            //去除头尾空格
            user.setUserName(userName.trim());
        }
        if (user.getSupUserId() == null){
            throw new ServiceException("请选择需要添加下级代理的代理组长");
        }
        if (StringUtils.isEmpty(user.getPassword())){
            throw new ServiceException("请输入登录密码");
        }
        if (!userService.checkUserNameUnique(user))
        {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setDeptId(3L);
        user.setRoleIds(new Long[]{101L});
        return toAjax(agentUserService.insertAgentUser(user));
    }

    /**
     * 修改代理用户信息
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:edit')")
    @Log(title = "修改代理用户信息", businessType = BusinessType.UPDATE,dict = SysUserLogDict.class,
            saveParamNames = {"userId","userName","nickName","email","phonenumber","password","inviteCode","status"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        //用户名
        String userName = user.getUserName();
        if (StringUtils.isNotEmpty(userName)){
            //去除头尾空格
            user.setUserName(userName.trim());
        }
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user))
        {
            throw new ServiceException("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            throw new ServiceException("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            throw new ServiceException("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUserName(null);
        return toAjax(agentUserService.updateAgentUser(user));
    }

    /**
     * 删除代理
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:remove')")
    @Log(title = "删除代理用户信息", businessType = BusinessType.DELETE,dict = SysUserLogDict.class,
            saveParamNames = {"userId","userName","users"})
    @RepeatSubmit
    @DeleteMapping("/{agentUserIds}")
    public AjaxResult remove(@PathVariable Long[] agentUserIds)
    {
        if (ArrayUtils.contains(agentUserIds, getUserId()))
        {
            throw new ServiceException("当前用户不能删除");
        }
        return toAjax(agentUserService.deleteUserByIds(agentUserIds));
    }

    /**
     * 变更上级(变更代理的上级)
     */
    @PreAuthorize("@ss.hasPermi('system:agentUser:changeSuperior')")
    @Log(title = "变更上级(变更代理的上级)", businessType = BusinessType.UPDATE,
            saveParamNames = {"用户ID","用户账号","上级ID","上级账号"})
    @RepeatSubmit
    @PostMapping("/{changeSuperior}")
    public AjaxResult changeSuperior(Long supUserId,Long userId)
    {
        if (supUserId == null){
            throw new ServiceException("请选择上级");
        }
        if (userId == null){
            throw new ServiceException("请选择需要变更上级的选项");
        }
        return toAjax(agentUserService.changeSuperior(supUserId,userId));
    }
}
