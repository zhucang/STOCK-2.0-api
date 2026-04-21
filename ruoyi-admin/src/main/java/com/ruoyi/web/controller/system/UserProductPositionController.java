package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserCryptocurrencyPositionLogDict;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserProductPosition;
import com.ruoyi.system.service.IUserProductPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户合约交易订单Controller
 * 
 * @author ruoyi
 * @date 2025-06-25
 */
@RestController
@RequestMapping("/system/userProductPosition")
public class UserProductPositionController extends BaseController
{
    @Autowired
    private IUserProductPositionService userProductPositionService;

    /**
     * 查询用户合约交易订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:userProductPosition:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserProductPosition userProductPosition)
    {
        startPage();
        startOrderBy("a.id desc");
        List<UserProductPosition> list = userProductPositionService.selectUserProductPositionList(userProductPosition);
        PageHelper.clearPage();
        userProductPositionService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 导出用户合约交易订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:userProductPosition:export')")
    @Log(title = "用户合约交易订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserProductPosition userProductPosition)
    {
        List<UserProductPosition> list = userProductPositionService.selectUserProductPositionList(userProductPosition);
        ExcelUtil<UserProductPosition> util = new ExcelUtil<UserProductPosition>(UserProductPosition.class);
        util.exportExcel(response, list, "用户合约交易订单数据");
    }

    /**
     * 获取用户合约交易订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userProductPosition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userProductPositionService.selectUserProductPositionById(id));
    }

    /**
     * 新增用户合约交易订单
     */
    @PreAuthorize("@ss.hasPermi('system:userProductPosition:add')")
    @Log(title = "用户合约交易订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserProductPosition userProductPosition)
    {
        return toAjax(userProductPositionService.insertUserProductPosition(userProductPosition));
    }

    /**
     * 修改用户合约交易订单
     */
    @PreAuthorize("@ss.hasPermi('system:userProductPosition:edit')")
    @Log(title = "用户合约交易订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserProductPosition userProductPosition)
    {
        return toAjax(userProductPositionService.updateUserProductPosition(userProductPosition));
    }

    /**
     * 删除用户合约交易订单
     */
    @PreAuthorize("@ss.hasPermi('system:userProductPosition:remove')")
    @Log(title = "用户合约交易订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userProductPositionService.deleteUserProductPositionByIds(ids));
    }

    /**
     * 锁仓、解仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:userProductPosition:lockUserPosition')")
    @Log(title = "锁仓、解仓操作", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "lockUserPosition")
    public AjaxResult lockUserPosition(Long positionId, Integer lockStatus, String lockMsg) {
        if (positionId == null){
            throw new ServiceException("请选择需要操作的持仓订单");
        }
        if (lockStatus == null){
            throw new ServiceException("请选择操作状态");
        }
        return toAjax(userProductPositionService.lockUserPosition(positionId,lockStatus,lockMsg));
    }

    /**
     * 强制平仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:userProductPosition:forceSell')")
    @Log(title = "强制平仓操作", businessType = BusinessType.UPDATE,dict = UserCryptocurrencyPositionLogDict.class,
            saveParamNames = {"id","orderCode","productCode","buyOrderPrice","sellOrderPrice","orderDirection","orderNum","orderLever","orderFee","orderYhsFee","currencyId","currencyName","profitAndLose","allProfitAndLose","doType"})
    @RepeatSubmit
    @PostMapping(value = "forceSell")
    public AjaxResult forceSell(Long positionId , Integer sellMode, BigDecimal target){
        if (positionId == null){
            throw new ServiceException("请选择需要平仓的订单");
        }
        if (sellMode == null){
            throw new ServiceException("请选择平仓模式");
        }
        if (target == null){
            throw new ServiceException("请输入价格/比例/金额");
        }
        try{
            return toAjax(userProductPositionService.forceSell(positionId,sellMode,target));
        }catch (LangException e){
            throw new ServiceException(e.getMsg());
        }
    }
}
