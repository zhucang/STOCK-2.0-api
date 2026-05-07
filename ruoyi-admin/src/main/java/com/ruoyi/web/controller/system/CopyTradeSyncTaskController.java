package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.CopyTradeSyncTask;
import com.ruoyi.system.service.ICopyTradeSyncTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台跟单同步任务管理接口。
 */
@RestController
@RequestMapping("/system/copyTradeSyncTask")
public class CopyTradeSyncTaskController extends BaseController {
    @Resource
    private ICopyTradeSyncTaskService copyTradeSyncTaskService;

    /** 查询跟单同步任务列表。 */
    @PreAuthorize("@ss.hasPermi('system:copyTradeSyncTask:list')")
    @GetMapping("/list")
    public TableDataInfo list(CopyTradeSyncTask copyTradeSyncTask) {
        startPage();
        startOrderBy("id desc");
        List<CopyTradeSyncTask> list = copyTradeSyncTaskService.selectCopyTradeSyncTaskList(copyTradeSyncTask);
        return getDataTable(list);
    }

    /** 查询跟单同步任务详情。 */
    @PreAuthorize("@ss.hasPermi('system:copyTradeSyncTask:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(copyTradeSyncTaskService.selectCopyTradeSyncTaskById(id));
    }

    /** 删除跟单同步任务。 */
    @PreAuthorize("@ss.hasPermi('system:copyTradeSyncTask:remove')")
    @Log(title = "删除跟单同步任务", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(copyTradeSyncTaskService.deleteCopyTradeSyncTaskByIds(ids));
    }
}
