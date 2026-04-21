package com.ruoyi.system.service;

import com.ruoyi.system.domain.CustomerLossReport;
import com.ruoyi.system.domain.CustomerLossReportNew;

import java.util.List;

public interface ICustomerLossReportService {

    /**
     * 获取代理客损信息列表
     * @param customerLossReport
     * @return
     */
    List<CustomerLossReport> getBillAnalysis(CustomerLossReport customerLossReport);

    /**
     * 获取代理客损总统计
     * @param customerLossReport
     * @return
     */
    List<CustomerLossReport> getAllAnalysis(CustomerLossReport customerLossReport);


    /**
     *  用户客损报表
     * @param customerLossReport
     * @return
     */
    List<CustomerLossReportNew> userCustomerLossReport(CustomerLossReportNew customerLossReport);
}
