package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.service.ICopyTradeRelationService;
import com.ruoyi.system.service.ICopyTradeTraderService;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户端跟单接口。
 * 提供交易员列表、交易员详情，以及跟随和停止跟随操作。
 */
@RestController
@RequestMapping("/api/copyTrade")
public class ApiCopyTradeController extends BaseController {
    /** 交易员服务。 */
    @Resource
    private ICopyTradeTraderService copyTradeTraderService;

    /** 跟单关系(跟单人员)服务。 */
    @Resource
    private ICopyTradeRelationService copyTradeRelationService;

    /**
     * 查询可跟随的交易员列表。
     *
     * @param copyTradeTrader 交易员筛选条件
     * @return 分页结果
     */
    @GetMapping("/trader/list")
    public TableDataInfo traderList(CopyTradeTrader copyTradeTrader) {
        // 用户端只展示启用状态的交易员。
        copyTradeTrader.setStatus(0);
        // 开启分页和默认排序，保证热门交易员稳定展示。
        startPage();
        startOrderBy("sort is null,sort,id desc");
        List<CopyTradeTrader> list = copyTradeTraderService.selectCopyTradeTraderList(copyTradeTrader);
        for (CopyTradeTrader tradeTrader : list) {
            copyTradeTraderService.fillFollowStatus(tradeTrader,  UserApiKeyUtils.getUserId());
        }
        return getDataTable(list);
    }

    /**
     * 查询当前用户已跟随的交易员列表。
     *
     * @return 分页结果
     */
    @GetMapping("/relation/followedList")
    public TableDataInfo followedTraderList() {
        CopyTradeRelation copyTradeRelation = new CopyTradeRelation();
        copyTradeRelation.setFollowerUserId(UserApiKeyUtils.getUserId());
        copyTradeRelation.setStatus(0);
        startPage();
        startOrderBy("id desc");
        List<CopyTradeRelation> list = copyTradeRelationService.selectCopyTradeRelationList(copyTradeRelation);
        for (CopyTradeRelation relation : list) {
            copyTradeRelationService.fillOtherInfo(relation);
        }
        return getDataTable(list);
    }

    /**
     * 跟随某个交易员。
     *
     * @param copyTradeRelation 跟单参数
     * @return 处理结果
     */
    @RepeatSubmit
    @PostMapping("/relation/follow")
    public AjaxResult follow(@RequestBody CopyTradeRelation copyTradeRelation) {
        copyTradeRelation.setFollowerUserId(UserApiKeyUtils.getUserId());
        return copyTradeRelationService.followTrader(copyTradeRelation);
    }

    /**
     * 编辑跟单配置。
     *
     * @param copyTradeRelation 跟单配置参数
     * @return 处理结果
     */
    @RepeatSubmit
    @PostMapping("/relation/edit")
    public AjaxResult editRelation(@RequestBody CopyTradeRelation copyTradeRelation) {
        return toAjax(copyTradeRelationService.updateCopyTradeRelationConfig(copyTradeRelation, UserApiKeyUtils.getUserId()));
    }

    /**
     * 停止跟随某个交易员。
     *
     * @param id 跟单关系(跟单人员)主键
     * @return 处理结果
     */
    @RepeatSubmit
    @PostMapping("/relation/unfollow")
    public AjaxResult unfollow(@RequestBody Long id) {
        return copyTradeRelationService.unfollowTrader(id, UserApiKeyUtils.getUserId());
    }
}
