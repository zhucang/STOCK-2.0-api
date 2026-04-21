package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.CustomerLossReport;
import com.ruoyi.system.domain.CustomerLossReportNew;

import java.util.List;

public interface CustomerLossReportMapper {

    /**
     * 获取报表信息
     * @param customerLossReport
     * @return
     */
    List<CustomerLossReport> getCustomerLossReport(CustomerLossReport customerLossReport);

    /**
     * 获取报表信息
     * @param customerLossReportNew
     * @return
     */
    List<CustomerLossReportNew> getCustomerLossReportNew(CustomerLossReportNew customerLossReportNew);


    Integer getRegNum(CustomerLossReport customerLossReport);

}
