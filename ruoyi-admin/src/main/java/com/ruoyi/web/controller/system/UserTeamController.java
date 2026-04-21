package com.ruoyi.web.controller.system;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.system.domain.UserTeamLevelLine;
import com.ruoyi.system.service.IUserTeamLevelLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 优化*
 * 用户团队Controller
 *
 * @author ruoyi
 * @date 2023-02-20
 */
@RestController
@RequestMapping("/system/userTeam")
public class UserTeamController extends BaseController {

    @Autowired
    private IUserTeamLevelLineService userTeamLevelLineService;

    /**
     * 查询下级团队信息
     */
    @PreAuthorize("@ss.hasPermi('system:userTeam:myTeamDataDashboard')")
    @GetMapping(value = "queryLowerTeamInfo")
    public AjaxResult list(UserTeamLevelLine userTeamLevelLine){
        if (userTeamLevelLine.getTeamLevel() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要查看团队数据的等级");
        }
        if (userTeamLevelLine.getUserId() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要查看团队的用户");
        }
        startPage();
        List<UserInfo> list = userTeamLevelLineService.queryLowerTeamInfo(userTeamLevelLine);
        return AjaxResult.success(getDataTable(list));
    }

    /**
     * 我的团队数据仪表板
     */
    @PreAuthorize("@ss.hasPermi('system:userTeam:myTeamDataDashboard')")
    @GetMapping(value = "myTeamDataDashboard")
    public AjaxResult myTeamDataDashboard(UserTeamLevelLine userTeamLevelLine){
        if (userTeamLevelLine.getUserId() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要查看团队的用户");
        }
        Map<String, Object> map = userTeamLevelLineService.myTeamDataDashboard(userTeamLevelLine);
        return AjaxResult.success(map);
    }
}
