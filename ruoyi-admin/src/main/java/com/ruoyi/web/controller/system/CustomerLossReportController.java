package com.ruoyi.web.controller.system;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.CustomerLossReport;
import com.ruoyi.system.domain.CustomerLossReportNew;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.service.ICustomerLossReportService;
import com.ruoyi.system.service.IPlatformCurrencyService;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.utils.currencyExchangeRate.ExchangeRateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

/**
 * 报表Controller
 *
 * @author ruoyi
 * @date 2023-02-20
 */
@RestController
@RequestMapping("/system/customerLossReport")
public class CustomerLossReportController extends BaseController {

    @Autowired
    private ICustomerLossReportService customerLossReportService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private ISwitchSetService switchSetService;

    @GetMapping(value = "/list")
    public TableDataInfo multiCurrency(CustomerLossReport customerLossReport){
        customerLossReport.setPageNum(null);
        customerLossReport.setPageSize(null);
        //结果集
        List<CustomerLossReport> result = null;
        //所有币种
        PlatformCurrency search = new PlatformCurrency();
        search.setStatus(0);
        List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(search);
        ExchangeRateUtil.fillExchangeRate(platformCurrencies);
        //币种map
        Map<Long, PlatformCurrency> platformCurrencyMap = platformCurrencies.stream().collect(Collectors.toMap(PlatformCurrency::getId, a -> a));
        //代理id
        Long agentId = customerLossReport.getAgentId();
        //贷款是否计入总客损开关
        Integer switchStatus92 = switchSetService.selectSwitchStatusById(92L);
        customerLossReport.getParams().put("switchStatus92",switchStatus92);
        //遍历
        for (int i = 0; i < platformCurrencies.size(); i++) {
            //币种信息
            PlatformCurrency platformCurrencyVo = platformCurrencies.get(i);
            //币种名称
            String currencyName = platformCurrencyVo.getCurrencyName();
            //币种id
            Long currencyId = platformCurrencyVo.getId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,3L,platformCurrencyMap).get("exchangeRate");
            if (exchangeRate.compareTo(BigDecimal.ZERO) == 0){
                throw new RuntimeException("获取"+platformCurrencyVo.getCurrencyName()+"与USDT的汇率异常");
            }
            customerLossReport.setCurrencyId(currencyId);
            List<CustomerLossReport> customerLossReports = new ArrayList<>();
            if (agentId == null){
                CustomerLossReport customerLossReportVo = new CustomerLossReport();
                customerLossReportVo.setStartTime(customerLossReport.getStartTime());
                customerLossReportVo.setEndTime(customerLossReport.getEndTime());
                customerLossReportVo.setIsLastMonth(customerLossReport.getIsLastMonth());
                customerLossReportVo.setPageNum(customerLossReport.getPageNum());
                customerLossReportVo.setPageSize(customerLossReport.getPageSize());
                customerLossReportVo.setCurrencyId(customerLossReport.getCurrencyId());
                customerLossReportVo.setParams(customerLossReport.getParams());
                customerLossReports = customerLossReportService.getAllAnalysis(customerLossReportVo);
            }else {
                customerLossReport.setAgentId(agentId);
                customerLossReports = customerLossReportService.getBillAnalysis(customerLossReport);
            }
            if (result == null){
                result = customerLossReports;
            }
            for (int j = 0; j < result.size(); j++) {
                //充值信息
                Map<String, Object> rechargeInfoMap = new HashMap<>();
                rechargeInfoMap.put("amount",customerLossReports.get(j).getRechargeAmount());
                rechargeInfoMap.put("count",customerLossReports.get(j).getRechargeCount());
                rechargeInfoMap.put("rechargePersonNum",customerLossReports.get(j).getRechargePersonNum());
                rechargeInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> rechargeInfo = result.get(j).getRechargeInfo();
                rechargeInfo.add(rechargeInfoMap);
                result.get(j).setRechargeInfo(rechargeInfo);

                //此币种的充值金额
                BigDecimal rechargeAmount = customerLossReports.get(j).getRechargeAmount();
                //折合USDT
                BigDecimal rechargeUSDTValue = rechargeAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setRechargeAmount(rechargeUSDTValue);
                    result.get(j).setRechargeCount(customerLossReports.get(j).getRechargeCount());
                    result.get(j).setRechargePersonNum(customerLossReports.get(j).getRechargePersonIds().size());
                }else {
                    result.get(j).setRechargeAmount(result.get(j).getRechargeAmount().add(rechargeUSDTValue));
                    result.get(j).setRechargeCount(result.get(j).getRechargeCount()+customerLossReports.get(j).getRechargeCount());
                    Map<Long, Integer> rechargePersonIdsResult = result.get(j).getRechargePersonIds();
                    Map<Long, Integer> rechargePersonIds = customerLossReports.get(j).getRechargePersonIds();
                    for (Map.Entry<Long, Integer> entry : rechargePersonIds.entrySet()) {
                        Long key = entry.getKey();
                        Integer value = entry.getValue();
                        Integer vo = rechargePersonIdsResult.get(key);
                        if (vo == null){
                            rechargePersonIdsResult.put(key,value);
                        }else {
                            rechargePersonIdsResult.put(key,vo+value);
                        }
                    }
                    result.get(j).setRechargePersonIds(rechargePersonIdsResult);
                    result.get(j).setRechargePersonNum(rechargePersonIdsResult.size());
                }

                //在线支付充值信息
                Map<String, Object> onlineRechargeInfoMap = new HashMap<>();
                onlineRechargeInfoMap.put("amount",customerLossReports.get(j).getOnlineRechargeAmount());
                onlineRechargeInfoMap.put("count",customerLossReports.get(j).getOnlineRechargeCount());
                onlineRechargeInfoMap.put("onlineRechargePersonNum",customerLossReports.get(j).getOnlineRechargePersonNum());
                onlineRechargeInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> onlineRechargeInfo = result.get(j).getOnlineRechargeInfo();
                onlineRechargeInfo.add(onlineRechargeInfoMap);
                result.get(j).setOnlineRechargeInfo(onlineRechargeInfo);

                //此币种的充值金额
                BigDecimal onlineRechargeAmount = customerLossReports.get(j).getOnlineRechargeAmount();
                //折合USDT
                BigDecimal onlineRechargeUSDTValue = onlineRechargeAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setOnlineRechargeAmount(onlineRechargeUSDTValue);
                    result.get(j).setOnlineRechargeCount(customerLossReports.get(j).getOnlineRechargeCount());
                    result.get(j).setOnlineRechargePersonNum(customerLossReports.get(j).getOnlineRechargePersonIds().size());
                }else {
                    result.get(j).setOnlineRechargeAmount(result.get(j).getOnlineRechargeAmount().add(onlineRechargeUSDTValue));
                    result.get(j).setOnlineRechargeCount(result.get(j).getOnlineRechargeCount()+customerLossReports.get(j).getOnlineRechargeCount());
                    Map<Long, Integer> onlineRechargePersonIdsResult = result.get(j).getOnlineRechargePersonIds();
                    Map<Long, Integer> onlineRechargePersonIds = customerLossReports.get(j).getOnlineRechargePersonIds();
                    for (Map.Entry<Long, Integer> entry : onlineRechargePersonIds.entrySet()) {
                        Long key = entry.getKey();
                        Integer value = entry.getValue();
                        Integer vo = onlineRechargePersonIdsResult.get(key);
                        if (vo == null){
                            onlineRechargePersonIdsResult.put(key,value);
                        }else {
                            onlineRechargePersonIdsResult.put(key,vo+value);
                        }
                    }
                    result.get(j).setOnlineRechargePersonIds(onlineRechargePersonIdsResult);
                    result.get(j).setOnlineRechargePersonNum(onlineRechargePersonIdsResult.size());
                }


                //提现信息
                Map<String, Object> withdrawInfoMap = new HashMap<>();
                withdrawInfoMap.put("amount",customerLossReports.get(j).getWithdrawAmount());
                withdrawInfoMap.put("count",customerLossReports.get(j).getWithdrawCount());
                withdrawInfoMap.put("withdrawPersonNum",customerLossReports.get(j).getWithdrawPersonNum());
                withdrawInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> withdrawInfo = result.get(j).getWithdrawInfo();
                withdrawInfo.add(withdrawInfoMap);
                result.get(j).setWithdrawInfo(withdrawInfo);

                //折合USDT
                //此币种的提现金额
                BigDecimal withdrawAmount = customerLossReports.get(j).getWithdrawAmount();
                BigDecimal withdrawUSDTValue = withdrawAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setWithdrawAmount(withdrawUSDTValue);
                    result.get(j).setWithdrawCount(customerLossReports.get(j).getWithdrawCount());
                    result.get(j).setWithdrawPersonNum(customerLossReports.get(j).getWithdrawPersonIds().size());
                }else {
                    result.get(j).setWithdrawAmount(result.get(j).getWithdrawAmount().add(withdrawUSDTValue));
                    result.get(j).setWithdrawCount(result.get(j).getWithdrawCount()+customerLossReports.get(j).getWithdrawCount());

                    Map<Long, Integer> withdrawPersonIdsResult = result.get(j).getWithdrawPersonIds();
                    Map<Long, Integer> withdrawPersonIds = customerLossReports.get(j).getWithdrawPersonIds();
                    for (Map.Entry<Long, Integer> entry : withdrawPersonIds.entrySet()) {
                        Long key = entry.getKey();
                        Integer value = entry.getValue();
                        Integer vo = withdrawPersonIdsResult.get(key);
                        if (vo == null){
                            withdrawPersonIdsResult.put(key,value);
                        }else {
                            withdrawPersonIdsResult.put(key,vo+value);
                        }
                    }
                    result.get(j).setWithdrawPersonIds(withdrawPersonIdsResult);
                    result.get(j).setWithdrawPersonNum(withdrawPersonIdsResult.size());
                }

                //上分信息
                Map<String, Object> upPointInfoMap = new HashMap<>();
                upPointInfoMap.put("amount",customerLossReports.get(j).getUpPointAmount());
                upPointInfoMap.put("count",customerLossReports.get(j).getUpPointCount());
                upPointInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> upPointInfo = result.get(j).getUpPointInfo();
                upPointInfo.add(upPointInfoMap);
                result.get(j).setUpPointInfo(upPointInfo);

                //此币种的上分金额
                BigDecimal upPointAmount = customerLossReports.get(j).getUpPointAmount();
                //折合USDT
                BigDecimal upPointUSDTValue = upPointAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setUpPointAmount(upPointUSDTValue);
                }else {
                    BigDecimal upPointAmount1 = result.get(j).getUpPointAmount();
                    BigDecimal add = upPointAmount1.add(upPointUSDTValue);
                    result.get(j).setUpPointAmount(add);
                    result.get(j).setUpPointCount(result.get(j).getUpPointCount()+customerLossReports.get(j).getUpPointCount());
                }

                //下分信息
                Map<String, Object> downPointInfoMap = new HashMap<>();
                downPointInfoMap.put("amount",customerLossReports.get(j).getDownPointAmount());
                downPointInfoMap.put("count",customerLossReports.get(j).getDownPointCount());
                downPointInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> downPointInfo = result.get(j).getDownPointInfo();
                downPointInfo.add(downPointInfoMap);
                result.get(j).setDownPointInfo(downPointInfo);

                //折合USDT
                //此币种的下分金额
                BigDecimal downPointAmount = customerLossReports.get(j).getDownPointAmount();
                BigDecimal downPointUSDTValue = downPointAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setDownPointAmount(downPointUSDTValue);
                }else {
                    result.get(j).setDownPointAmount(result.get(j).getDownPointAmount().add(downPointUSDTValue));
                    result.get(j).setDownPointCount(result.get(j).getDownPointCount()+customerLossReports.get(j).getDownPointCount());
                }

                //赠送彩金信息
                Map<String, Object> inWinningsInfoMap = new HashMap<>();
                inWinningsInfoMap.put("amount",customerLossReports.get(j).getInWinningsAmount());
                inWinningsInfoMap.put("count",customerLossReports.get(j).getInWinningsCount());
                inWinningsInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> InWinningsInfo = result.get(j).getInWinningsInfo();
                InWinningsInfo.add(inWinningsInfoMap);
                result.get(j).setInWinningsInfo(InWinningsInfo);

                //折合USDT
                //此币种的赠送彩金金额
                BigDecimal inWinningsAmount = customerLossReports.get(j).getInWinningsAmount();
                BigDecimal inWinningsUSDTValue = inWinningsAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setInWinningsAmount(inWinningsUSDTValue);
                }else {
                    result.get(j).setInWinningsAmount(result.get(j).getInWinningsAmount().add(inWinningsUSDTValue));
                    result.get(j).setInWinningsCount(result.get(j).getInWinningsCount()+customerLossReports.get(j).getInWinningsCount());
                }

                //回收彩金信息
                Map<String, Object> outWinningsInfoMap = new HashMap<>();
                outWinningsInfoMap.put("amount",customerLossReports.get(j).getOutWinningsAmount());
                outWinningsInfoMap.put("count",customerLossReports.get(j).getOutWinningsCount());
                outWinningsInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> outWinningsInfo = result.get(j).getOutWinningsInfo();
                outWinningsInfo.add(outWinningsInfoMap);
                result.get(j).setOutWinningsInfo(outWinningsInfo);

                //折合USDT
                //此币种的回收彩金金额
                BigDecimal outWinningsAmount = customerLossReports.get(j).getOutWinningsAmount();
                BigDecimal outWinningsUSDTValue = outWinningsAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setOutWinningsAmount(outWinningsUSDTValue);
                }else {
                    result.get(j).setOutWinningsAmount(result.get(j).getOutWinningsAmount().add(outWinningsUSDTValue));
                    result.get(j).setOutWinningsCount(result.get(j).getOutWinningsCount()+customerLossReports.get(j).getOutWinningsCount());
                }

                //贷款信息
                Map<String, Object> loanInfoMap = new HashMap<>();
                loanInfoMap.put("amount",customerLossReports.get(j).getLoanAmount());
                loanInfoMap.put("count",customerLossReports.get(j).getLoanCount());
                loanInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> loanInfo = result.get(j).getLoanInfo();
                loanInfo.add(loanInfoMap);
                result.get(j).setLoanInfo(loanInfo);

                //折合USDT
                //此币种的贷款金额
                BigDecimal loanAmount = customerLossReports.get(j).getLoanAmount();
                BigDecimal loanUSDTValue = loanAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setLoanAmount(loanUSDTValue);
                }else {
                    result.get(j).setLoanAmount(result.get(j).getLoanAmount().add(loanUSDTValue));
                    result.get(j).setLoanCount(result.get(j).getLoanCount()+customerLossReports.get(j).getLoanCount());
                }

                //免客损贷款信息
                Map<String, Object> loanInfoNoStatisticalMap = new HashMap<>();
                loanInfoNoStatisticalMap.put("amount",customerLossReports.get(j).getLoanAmountNoStatistical());
                loanInfoNoStatisticalMap.put("count",customerLossReports.get(j).getLoanCountNoStatistical());
                loanInfoNoStatisticalMap.put("currencyName",currencyName);
                List<Map<String, Object>> loanInfoNoStatistical = result.get(j).getLoanInfoNoStatistical();
                loanInfoNoStatistical.add(loanInfoNoStatisticalMap);
                result.get(j).setLoanInfoNoStatistical(loanInfoNoStatistical);

                //折合USDT
                //此币种的贷款金额
                BigDecimal loanAmountNoStatistical = customerLossReports.get(j).getLoanAmountNoStatistical();
                BigDecimal loanNoStatisticalUSDTValue = loanAmountNoStatistical.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setLoanAmountNoStatistical(loanNoStatisticalUSDTValue);
                }else {
                    result.get(j).setLoanAmountNoStatistical(result.get(j).getLoanAmountNoStatistical().add(loanNoStatisticalUSDTValue));
                    result.get(j).setLoanCountNoStatistical(result.get(j).getLoanCountNoStatistical()+customerLossReports.get(j).getLoanCountNoStatistical());
                }

                //客损
                Map<String, Object> customerLossInfoMap = new HashMap<>();
                customerLossInfoMap.put("amount",customerLossReports.get(j).getCustomerLossAmount());
                customerLossInfoMap.put("currencyName",currencyName);
                List<Map<String, Object>> customerLossInfo = result.get(j).getCustomerLossInfo();
                customerLossInfo.add(customerLossInfoMap);
                result.get(j).setCustomerLossInfo(customerLossInfo);

                //折合USDT
                //客损
                BigDecimal customerLossAmount = customerLossReports.get(j).getCustomerLossAmount();
                if (customerLossAmount == null){
                    customerLossAmount = BigDecimal.ZERO;
                }
                BigDecimal customerLossUSDTValue = customerLossAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (i == 0){
                    result.get(j).setCustomerLossAmount(customerLossUSDTValue);
                }else {
                    result.get(j).setCustomerLossAmount(result.get(j).getCustomerLossAmount().add(customerLossUSDTValue));
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            CustomerLossReport customerLossReportVo = result.get(i);
            int firstRechargeNum = 0;
            int secondRechargeNum = 0;
            List<Long> firstRechargePersonIds = new ArrayList<>();
            List<Long> secondRechargePersonIds = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : customerLossReportVo.getRechargePersonIds().entrySet()) {
                Long key = entry.getKey();
                Integer value = entry.getValue();
                if (value.equals(1)){
                    firstRechargePersonIds.add(key);
                    firstRechargeNum = firstRechargeNum + 1;
                }else if (value > 1){
                    secondRechargePersonIds.add(key);
                    secondRechargeNum = secondRechargeNum + 1;
                }
            }
            customerLossReportVo.setFirstRechargeNum(firstRechargeNum);
            customerLossReportVo.setFirstRechargePersonIds(firstRechargePersonIds);
            customerLossReportVo.setSecondRechargeNum(secondRechargeNum);
            customerLossReportVo.setSecondRechargePersonIds(secondRechargePersonIds);
            if (Integer.valueOf(1).equals(customerLossReportVo.getAgentOrUserFlag())){
                customerLossReportVo.setRechargePersonNum(1);
                customerLossReportVo.setWithdrawPersonNum(1);
            }
        }
        return getDataTable(result);
    }

    /**
     * 用户客损报表
     */
    @PreAuthorize("@ss.hasPermi('system:customerLossReport:userCustomerLossReport')")
    @GetMapping(value = "/userCustomerLossReport")
    public TableDataInfo userCustomerLossReport(CustomerLossReportNew customerLossReport) {
        List<CustomerLossReportNew> customerLossReportNews = customerLossReportService.userCustomerLossReport(customerLossReport);
        return getDataTable(customerLossReportNews);
    }

    /**
     * 用户客损报表
     */
    @GetMapping(value = "/userCustomerLossReport2")
    public void userCustomerLossReport2(CustomerLossReportNew customerLossReport, HttpServletResponse response) throws Exception{
        List<CustomerLossReportNew> customerLossReportNews = customerLossReportService.userCustomerLossReport(customerLossReport);
        List<List<CustomerLossReportNew>> partition = ListUtil.partition(customerLossReportNews, 50);
        String result = "[";
        for (int i = 0; i < partition.size(); i++) {
            String s = JSONObject.toJSONString(partition.get(i));
            s = s.substring(1,s.length()-1);
            result = result + s + ",";
        }
        result = result.substring(0,result.length()-1) + "]";
        response.addHeader("Content-Encoding", "gzip");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos)){
            gzipOutputStream.write(result.getBytes(StandardCharsets.UTF_8));
        }
        byte[] bytes = baos.toByteArray();
        response.getOutputStream().write(bytes);
    }
    //换成map，没有数据的币种全部去掉，数据为0的也全部去掉
}
