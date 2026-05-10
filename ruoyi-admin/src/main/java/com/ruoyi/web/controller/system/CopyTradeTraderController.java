package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.service.ICopyTradeRelationService;
import com.ruoyi.system.service.ICopyTradeTraderService;
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
 * 后台交易员管理接口。
 * 负责交易员配置的新增、修改、删除和后台查询。
 */
@RestController
@RequestMapping("/system/copyTradeTrader")
public class CopyTradeTraderController extends BaseController {
    /** 交易员服务。 */
    @Resource
    private ICopyTradeTraderService copyTradeTraderService;

    /** 跟单关系服务。 */
    @Resource
    private ICopyTradeRelationService copyTradeRelationService;

    /**
     * 查询交易员列表。
     *
     * @param copyTradeTrader 查询条件
     * @return 分页结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeTrader:list')")
    @GetMapping("/list")
    public TableDataInfo list(CopyTradeTrader copyTradeTrader) {
        // 后台默认按排序值和主键倒序展示，便于运营维护。
        startPage();
        startOrderBy("sort is null,sort,id desc");
        List<CopyTradeTrader> list = copyTradeTraderService.selectCopyTradeTraderList(copyTradeTrader);
        return getDataTable(list);
    }

    /**
     * 查询交易员详情。
     *
     * @param id 交易员主键
     * @return 交易员详情
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeTrader:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(copyTradeTraderService.selectCopyTradeTraderById(id));
    }

    /**
     * 新增交易员。
     *
     * @param copyTradeTrader 交易员参数
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeTrader:add')")
    @Log(title = "新增跟单交易员", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody CopyTradeTrader copyTradeTrader) {
        // 交易员必须绑定到一个真实用户。
        if (copyTradeTrader.getUserId() == null) {
            throw new ServiceException("请选择交易员用户");
        }
        return toAjax(copyTradeTraderService.insertCopyTradeTrader(copyTradeTrader));
    }

    /**
     * 修改交易员。
     *
     * @param copyTradeTrader 交易员参数
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeTrader:edit')")
    @Log(title = "修改跟单交易员", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody CopyTradeTrader copyTradeTrader) {
        // 修改前必须指定主键。
        if (copyTradeTrader.getId() == null) {
            throw new ServiceException("请选择需要修改的交易员");
        }
        checkTraderCanBeModified(copyTradeTrader.getId());
        return toAjax(copyTradeTraderService.updateCopyTradeTrader(copyTradeTrader));
    }

    /**
     * 启用交易员。
     *
     * @param id 交易员主键
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeTrader:edit')")
    @Log(title = "启用跟单交易员", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/enable/{id}")
    public AjaxResult enable(@PathVariable Long id) {
        return toAjax(copyTradeTraderService.updateCopyTradeTraderStatus(id, 0));
    }

    /**
     * 停用交易员。
     *
     * @param id 交易员主键
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeTrader:edit')")
    @Log(title = "停用跟单交易员", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping("/disable/{id}")
    public AjaxResult disable(@PathVariable Long id) {
        return toAjax(copyTradeTraderService.updateCopyTradeTraderStatus(id, 1));
    }

    /**
     * 删除交易员。
     *
     * @param ids 主键数组
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeTrader:remove')")
    @Log(title = "删除跟单交易员", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        throw new ServiceException("交易员禁止删除，请使用停用功能");
    }

    /**
     * 已有人跟单时禁止通过后台编辑交易员配置。
     *
     * @param id 交易员主键
     */
    private void checkTraderCanBeModified(Long id) {
        CopyTradeTrader oldTrader = copyTradeTraderService.selectCopyTradeTraderById(id);
        if (oldTrader == null) {
            throw new ServiceException("交易员不存在");
        }
        int followerCount = copyTradeRelationService.countActiveFollowerByTraderUserId(oldTrader.getUserId());
        if (followerCount > 0) {
            throw new ServiceException("已有用户跟单，禁止修改交易员配置");
        }
    }

}
