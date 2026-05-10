package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.service.ICopyTradeRelationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台跟单关系(跟单人员)管理接口。
 * 负责后台查看和清理跟单关系(跟单人员)数据。
 */
@RestController
@RequestMapping("/system/copyTradeRelation")
public class CopyTradeRelationController extends BaseController {
    /** 跟单关系(跟单人员)服务。 */
    @Resource
    private ICopyTradeRelationService copyTradeRelationService;

    /**
     * 查询跟单关系(跟单人员)列表。
     *
     * @param copyTradeRelation 查询条件
     * @return 分页结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:list')")
    @GetMapping("/list")
    public TableDataInfo list(CopyTradeRelation copyTradeRelation) {
        // 后台按最近创建的关系倒序查看更符合运营习惯。
        startPage();
        startOrderBy("id desc");
        List<CopyTradeRelation> list = copyTradeRelationService.selectCopyTradeRelationList(copyTradeRelation);
        return getDataTable(list);
    }

    /**
     * 查询跟单关系(跟单人员)详情。
     *
     * @param id 跟单关系(跟单人员)主键
     * @return 跟单关系(跟单人员)详情
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(copyTradeRelationService.selectCopyTradeRelationById(id));
    }

    /**
     * 新增跟单关系(跟单人员)。
     * 新增即创建有效跟随关系，复用 followTrader 统一处理业务校验、历史关系恢复和默认参数。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)参数
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:add')")
    @Log(title = "新增跟单关系(跟单人员)", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody CopyTradeRelation copyTradeRelation) {
        if (copyTradeRelation == null) {
            throw new ServiceException("请填写跟单关系(跟单人员)信息");
        }
        return copyTradeRelationService.followTrader(copyTradeRelation);
    }

    /**
     * 修改跟单关系(跟单人员)执行配置。
     * 普通编辑只允许修改 followMode、followAmount、followRatio、maxOpenOrders。
     * status 必须通过跟随/停止跟随接口修改，traderId、traderUserId、followerUserId 创建后禁止修改。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)参数
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:edit')")
    @Log(title = "修改跟单关系(跟单人员)", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody CopyTradeRelation copyTradeRelation) {
        if (copyTradeRelation == null || copyTradeRelation.getId() == null) {
            throw new ServiceException("请选择需要修改的跟单关系(跟单人员)");
        }
        return toAjax(copyTradeRelationService.updateCopyTradeRelationConfig(copyTradeRelation, null));
    }

    /**
     * 后台新增或恢复跟随关系。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)参数
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:follow')")
    @Log(title = "跟随交易员", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/follow")
    public AjaxResult follow(@RequestBody CopyTradeRelation copyTradeRelation) {
        if (copyTradeRelation == null || copyTradeRelation.getFollowerUserId() == null) {
            throw new ServiceException("请选择跟单用户");
        }
        return copyTradeRelationService.followTrader(copyTradeRelation);
    }

    /**
     * 后台停止跟随关系。
     *
     * @param id 跟单关系(跟单人员)主键
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:unfollow')")
    @Log(title = "停止跟随交易员", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/unfollow/{id}")
    public AjaxResult unfollow(@PathVariable Long id) {
        return copyTradeRelationService.unfollowTrader(id, null);
    }

    /**
     * 删除跟单关系(跟单人员)。
     *
     * @param ids 主键数组
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:remove')")
    @Log(title = "删除跟单关系(跟单人员)", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(copyTradeRelationService.deleteCopyTradeRelationByIds(ids));
    }
}
