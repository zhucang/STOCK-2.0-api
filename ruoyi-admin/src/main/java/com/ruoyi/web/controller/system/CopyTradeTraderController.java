package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.service.ICopyTradeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台交易员管理接口。
 */
@RestController
@RequestMapping("/system/copyTradeTrader")
public class CopyTradeTraderController extends BaseController {
    /** 跟单业务服务。 */
    @Resource
    private ICopyTradeService copyTradeService;

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
        List<CopyTradeTrader> list = copyTradeService.selectCopyTradeTraderList(copyTradeTrader);
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
        return success(copyTradeService.selectCopyTradeTraderById(id));
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
        return toAjax(copyTradeService.insertCopyTradeTrader(copyTradeTrader));
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
        return toAjax(copyTradeService.updateCopyTradeTrader(copyTradeTrader));
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
        return toAjax(copyTradeService.deleteCopyTradeTraderByIds(ids));
    }
}
