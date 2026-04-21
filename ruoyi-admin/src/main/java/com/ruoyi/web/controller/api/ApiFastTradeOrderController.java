package com.ruoyi.web.controller.api;

import cn.hutool.core.thread.ThreadUtil;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.FastTradeOrderLogDict;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FastTradeOrder;
import com.ruoyi.system.service.IFastTradeOrderService;
import com.ruoyi.system.service.IPlatformCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 极速交易订单Controller
 *
 * @author ruoyi
 * @date 2023-11-02
 */
@RestController
@RequestMapping("/api/fastTradeOrder")
public class ApiFastTradeOrderController extends BaseController
{
    @Autowired
    private IFastTradeOrderService fastTradeOrderService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询极速交易订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FastTradeOrder fastTradeOrder)
    {
        fastTradeOrder.setUserId(SecurityUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<FastTradeOrder> list = fastTradeOrderService.selectFastTradeOrderList(fastTradeOrder);
        PageHelper.clearPage();
        fastTradeOrderService.fillOtherInfo(list);
        TableDataInfo dataTable = getDataTable(list);
        dataTable.getMapData().put("nowDateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,new Date(),null));
        return dataTable;
    }

    /**
     * 极速交易订单详情
     */
    @RequestMapping({"detail"})
    @Log(title = "极速交易订单详情", businessType = BusinessType.OTHER,dict = FastTradeOrderLogDict.class)
    public AjaxResult detail(Long id) {
        Long userId = SecurityUtils.getUserId();
        //订单信息
        FastTradeOrder fastTradeOrder = fastTradeOrderService.selectFastTradeOrderById(id);
        //校验身份
        if (!fastTradeOrder.getUserId().equals(userId)){
            throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //获取次数
        int count = 1;
        //订单是否正常结算
        int ex = 0;
        while (true){
            //如果订单已经结算
            if (fastTradeOrder.getOrderStatus().equals(1)){
                break;
            }
            //如果获取次数超过6次，提示错误
            if (count > 6){
                fastTradeOrder.setOrderProfit(BigDecimal.ZERO);
                ex = 1;
                break;
            }
            //0.3秒后重新获取数据
            ThreadUtil.sleep(500);
            //重新获取订单信息
            fastTradeOrder = fastTradeOrderService.selectFastTradeOrderById(id);
            //获取次数+1
            count++;
        }
        return AjaxResult.success(fastTradeOrder).put("ex",ex);
    }

    /**
     * 极速交易下单
     * @param fastTradeOrder
     * @return
     */
    @RepeatSubmit
    @PostMapping(value = "addFastTradeOrder")
    @Log(title = "用户极速交易下单", businessType = BusinessType.OTHER,dict = FastTradeOrderLogDict.class,
            saveParamNames = {"id","orderCode","currencyId","currencyName","productType","productCode","orderDirection","orderPrice","feeRate","handingFee","fastTradeOrderOptionsId","durationValue","durationLabel","winProfitRatio","loseProfitRatio","buyPrice","loseMoneyMethod"})
    public AjaxResult addFastTradeOrder(FastTradeOrder fastTradeOrder){
        if (fastTradeOrder.getFastTradeOrderOptionsId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择下单玩法");
        }
        if (fastTradeOrder.getOrderDirection() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择交易方向");
        }
        if (fastTradeOrder.getOrderPrice() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请输入下单金额");
        }
        if (fastTradeOrder.getOrderPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new LangException("hint_orderAmountMoreThenZero","下单金额必须大于0");
        }
        if (fastTradeOrder.getCurrencyId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择交易币种");
        }
        return AjaxResult.success(fastTradeOrderService.addFastTradeOrder(fastTradeOrder));
    }
}
