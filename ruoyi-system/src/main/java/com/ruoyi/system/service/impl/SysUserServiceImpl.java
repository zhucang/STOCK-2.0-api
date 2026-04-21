package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.googleAuth.GoogleAuthenticatorUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUserPost;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IAgentTeamLevelLineService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysPost::getPostName).collect(Collectors.joining(","));
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     * 
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            //当前用户的角色
            List<Long> currencyUserRoleIds = SecurityUtils.getLoginUser().getUser().getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList());
            //被修改的用户角色
            List<Long> modifyUserRoleIds = roleMapper.selectRoleListByUserId(userId);
            for (int i = 0; i < modifyUserRoleIds.size(); i++) {
                if (currencyUserRoleIds.contains(modifyUserRoleIds.get(i))){
                    throw new ServiceException("你当前没有权限操作此数据！");
                }
            }
            SysUser user = new SysUser();
            user.setUserId(userId);
            user.getParams().put("agentData",0);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users))
            {
                throw new ServiceException("你当前没有权限操作此数据！");
            }
        }
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user)
    {
        //邀请码
        String inviteCode = user.getInviteCode();
        //如果填写了邀请码
        if (StringUtils.isNotEmpty(inviteCode)){
            //去除头尾空格
            inviteCode = inviteCode.trim();
            user.setInviteCode(inviteCode);
            //验证邀请码是否重复
            if (inviteCode.startsWith("U") || inviteCode.startsWith("0")){
                throw new RuntimeException("不允许以大写字母U开头或者数字0开头");
            }
            SysUser byInviteCode = userMapper.selectUserByInviteCode(inviteCode);
            if (byInviteCode != null){
                throw new RuntimeException("此邀请码已存在");
            }
        }else {
            //生成邀请码
            inviteCode = "1"+ CodeUtils.generateInviteCode(null);
            while (true){
                SysUser byInviteCode = userMapper.selectUserByInviteCode(inviteCode);
                if (byInviteCode != null){
                    inviteCode = "1"+ CodeUtils.generateInviteCode(null);
                }else {
                    user.setInviteCode(inviteCode);
                    break;
                }
            }
        }
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        //如果有设置上级，更新代理团队信息
        if (user.getSupUserId() != null){
            int count = agentTeamLevelLineService.updateAgentTeamLevelLine(user.getUserId(), user.getSupUserId(), 0);
            if (count <= 0){
                throw new RuntimeException("系统繁忙");
            }
        }
        return rows;
    }

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user)
    {
        //用户名称
        String userName = user.getUserName();
        if (StringUtils.isNotEmpty(userName)){
            //去除头尾空格
            user.setUserName(userName.trim());
        }
        //验证邀请码是否重复
        //邀请码
        String inviteCode = user.getInviteCode();
        if (StringUtils.isNotEmpty(inviteCode)){
            //去除头尾空格
            inviteCode = inviteCode.trim();
            user.setInviteCode(inviteCode);
            if (inviteCode.startsWith("U") || inviteCode.startsWith("0")){
                throw new RuntimeException("不允许以大写字母U开头或者数字0开头");
            }
            SysUser byInviteCode = userMapper.selectUserByInviteCode(inviteCode);
            if (byInviteCode != null && !byInviteCode.getUserId().equals(user.getUserId())){
                throw new RuntimeException("此邀请码已存在");
            }
        }
        Long userId = user.getUserId();
        //修改用户表关联的代理昵称
        userInfoMapper.replaceAgentIdAndAgentName(userId,userId,userName,user.getNickName(),null);
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 禁用所有客户管理专用账号
     * @return
     */
    @Override
    public int disableAllCustomerAccounts() {
        userMapper.disableAllCustomerAccounts();
        return 1;
    }

    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotEmpty(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>(posts.length);
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            userPostMapper.batchUserPost(list);
        }
    }

    /**
     * 新增用户角色信息
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotEmpty(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(Long userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds)
    {
        //用户名称列表
        List<SysUser> users = new ArrayList<>();
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
            //获取用户信息
            SysUser user = userMapper.selectUserById(userId);
            if (user == null){
                continue;
            }
            users.add(user);
            //更新代理团队信息
            int count = agentTeamLevelLineService.updateAgentTeamLevelLine(user.getUserId(), user.getSupUserId(), 1);
            if (count <= 0){
                throw new RuntimeException("系统繁忙");
            }
        }
        //日志记录用户信息
        HttpUtils.getRequestLogParams().put("JSONArray:users", JSONObject.toJSONString(users));
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    BeanValidators.validateWithException(validator, user);
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }


    /**
     * 启用歌验证器验证
     * @return
     */
    @Override
    public AjaxResult enableGoogleAuthenticator(){
        //当前登录用户id
        Long userId = SecurityUtils.getUserId();
        //生成谷歌验证密钥
        String secretKey = GoogleAuthenticatorUtils.getRandomSecretKey();
        // 根据验证码，账户，服务商生成 TOPT 密钥的 URI
        String uri = GoogleAuthenticatorUtils.getGoogleAuthenticatorBarCode(secretKey, "", "");
        // 根据 TOPT 密钥的 URI生成二维码，存储在本地
        //下载地址
        String uploadPath = RuoYiConfig.getUploadPath("google-auth");
        File file = new File(uploadPath);
        if (!file.exists()){
            file.mkdir();
        }
        //二维码图片文件名称
        String fileName = userId + "." + "png";
        //二维码图片文件url
        String fileUrl = uploadPath + "/" + fileName;
        //二维码图片映射url
        String mappingUrl = fileUrl.replace(RuoYiConfig.getProfile(), Constants.RESOURCE_PREFIX);
        //输出流
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileUrl);
            GoogleAuthenticatorUtils.createQRCode(uri, fileOutputStream, 200, 200);
        }catch (Exception e){

        }finally {
            if (fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException ex) {
                    throw new ServiceException(ex.getMessage());
                }
                ;
            }
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setGoogleAuthSecurityKey(secretKey);
        int count = userMapper.updateUser(sysUser);
        if (count <= 0){
            return AjaxResult.error("系统繁忙");
        }
        return AjaxResult.success(secretKey).put("qrCodeUrl",mappingUrl);
    }

    /**
     * 禁用谷歌验证器验证
     * @param sysUserId 后台用户id
     * @return
     */
    public int disableGoogleAuthenticator(Long sysUserId){
        if (sysUserId == null){
            sysUserId = SecurityUtils.getUserId();
        }
        //用户信息
        SysUser sysUser = userMapper.selectUserById(sysUserId);
        if (sysUser == null || !"0".equals(sysUser.getDelFlag())){
            throw new ServiceException("获取用户信息异常");
        }
        //日志记录用户账号
        HttpUtils.getRequestLogParams().put("用户账号",sysUser.getUserName());
        sysUser.setGoogleAuthSecurityKey("");
        int count = userMapper.updateUser(sysUser);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 获取谷歌验证器秘钥
     * @return
     */
    @Override
    public String getGoogleAuthSecurityKey(Long userId){
        //谷歌验证器秘钥
        String googleAuthSecurityKey = userMapper.getGoogleAuthSecurityKey(userId);
        return googleAuthSecurityKey;
    }
}
