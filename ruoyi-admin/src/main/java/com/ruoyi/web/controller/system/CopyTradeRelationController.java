package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.service.ICopyTradeRelationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台跟单关系管理接口。
 * 负责后台查看和清理跟单关系数据。
 */
@RestController
@RequestMapping("/system/copyTradeRelation")
public class CopyTradeRelationController extends BaseController {
    /** 跟单关系服务。 */
    @Resource
    private ICopyTradeRelationService copyTradeRelationService;

    /**
     * 查询跟单关系列表。
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
     * 查询跟单关系详情。
     *
     * @param id 跟单关系主键
     * @return 跟单关系详情
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(copyTradeRelationService.selectCopyTradeRelationById(id));
    }

    /**
     * 删除跟单关系。
     *
     * @param ids 主键数组
     * @return 处理结果
     */
    @PreAuthorize("@ss.hasPermi('system:copyTradeRelation:remove')")
    @Log(title = "删除跟单关系", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(copyTradeRelationService.deleteCopyTradeRelationByIds(ids));
    }
}
