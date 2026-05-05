package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.CopyTradeOrder;
import com.ruoyi.system.service.ICopyTradeOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台跟单订单映射管理接口。
 * 主要用于运营查看主单和跟单单之间的映射关系，以及处理异常脏数据。
 */
@RestController
@RequestMapping("/system/copyTradeOrder")
public class CopyTradeOrderController extends BaseController {
    /** 跟单订单映射服务。 */
    @Resource
    private ICopyTradeOrderService copyTradeOrderService;

    /**
     * 查询跟单订单映射列表。
     *
     * @param copyTradeOrder 查询条件
     * @return 分页结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(CopyTradeOrder copyTradeOrder) {
        // 后台一般优先查看最新同步出来的映射记录。
        startPage();
        startOrderBy("id desc");
        List<CopyTradeOrder> list = copyTradeOrderService.selectCopyTradeOrderList(copyTradeOrder);
        return getDataTable(list);
    }

    /**
     * 查询跟单订单映射详情。
     *
     * @param id 跟单订单映射主键
     * @return 跟单订单映射详情
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeOrder:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(copyTradeOrderService.selectCopyTradeOrderById(id));
    }

    /**
     * 删除跟单订单映射。
     *
     * @param ids 主键数组
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeOrder:remove')")
    @Log(title = "删除跟单订单映射", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(copyTradeOrderService.deleteCopyTradeOrderByIds(ids));
    }
}
