package com.ruoyi.web.controller.api;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.UserTeamLevelLine;
import com.ruoyi.system.service.IUserTeamLevelLineService;
import org.springframework.beans.factory.annotation.Autowired;
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
 *  *  * cache待优化
 */
@RestController
@RequestMapping("/api/userTeam")
public class ApiUserTeamController extends BaseController {

    @Autowired
    private IUserTeamLevelLineService userTeamLevelLineService;

    /**
     * 查询下级团队信息
     */
    @GetMapping(value = "queryLowerTeamInfo")
    public AjaxResult list(UserTeamLevelLine userTeamLevelLine){
        userTeamLevelLine.setUserId(SecurityUtils.getUserId());
        if (userTeamLevelLine.getTeamLevel() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要查看团队数据的等级");
        }
        startPage();
        List<UserInfo> list = userTeamLevelLineService.queryLowerTeamInfo(userTeamLevelLine);
        return AjaxResult.success(getDataTable(list));
    }

    /**
     * 我的团队数据仪表板
     */
    @GetMapping(value = "myTeamDataDashboard")
    public AjaxResult myTeamDataDashboard(UserTeamLevelLine userTeamLevelLine){
        userTeamLevelLine.setUserId(SecurityUtils.getUserId());
        Map<String, Object> map = userTeamLevelLineService.myTeamDataDashboard(userTeamLevelLine);
        return AjaxResult.success(map);
    }
}
