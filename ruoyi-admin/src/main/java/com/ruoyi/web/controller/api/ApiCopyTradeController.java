package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.service.ICopyTradeService;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户端跟单接口。
 * 提供交易员列表、我的跟单关系，以及跟随/停止跟随操作。
 */
@RestController
@RequestMapping("/api/copyTrade")
public class ApiCopyTradeController extends BaseController {
    /** 跟单业务服务。 */
    @Resource
    private ICopyTradeService copyTradeService;

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
        List<CopyTradeTrader> list = copyTradeService.selectCopyTradeTraderList(copyTradeTrader);
        return getDataTable(list);
    }

    /**
     * 查询单个交易员详情。
     *
     * @param id 交易员主键
     * @return 交易员详情
     */
    @GetMapping("/trader/{id}")
    public AjaxResult traderInfo(@PathVariable Long id) {
        return success(copyTradeService.selectCopyTradeTraderById(id));
    }

    /**
     * 查询当前用户自己的跟单关系列表。
     *
     * @param copyTradeRelation 跟单关系筛选条件
     * @return 分页结果
     */
    @GetMapping("/relation/myList")
    public TableDataInfo myRelationList(CopyTradeRelation copyTradeRelation) {
        // 强制限定为当前登录用户的数据，避免越权查看。
        copyTradeRelation.setFollowerUserId(UserApiKeyUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<CopyTradeRelation> list = copyTradeService.selectCopyTradeRelationList(copyTradeRelation);
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
        return copyTradeService.followTrader(copyTradeRelation);
    }

    /**
     * 停止跟随某个交易员。
     *
     * @param relationId 跟单关系主键
     * @return 处理结果
     */
    @RepeatSubmit
    @PostMapping("/relation/unfollow")
    public AjaxResult unfollow(Long relationId) {
        return copyTradeService.unfollowTrader(relationId);
    }
}
