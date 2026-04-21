//package com.ruoyi.system.task.FlexibleInvestment;
//
//import com.ruoyi.common.constant.Constants;
//import com.ruoyi.common.core.domain.entity.UserAmount;
//import com.ruoyi.common.exception.ServiceException;
//import com.ruoyi.common.utils.DateUtils;
//import com.ruoyi.common.utils.cache.CacheUtil;
//import com.ruoyi.system.mapper.UserAmountMapper;
//import com.ruoyi.system.mapper.UserBillDetailMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * 灵活投资
// */
//@Component
//public class FlexibleInvestmentTask {
//
//    private static final Logger log = LoggerFactory.getLogger(FlexibleInvestmentTask.class);
//
//    @Resource
//    private UserAmountMapper userAmountMapper;
//
//    @Resource
//    private UserBillDetailMapper userBillDetailMapper;
//
//    /**
//     * 灵活投资派发收益（每日凌晨两点固定派息）
//     */
//    @Scheduled(cron = "0 0 2 * * ?")
//    public void task(){
//        UserAmount userAmount = new UserAmount();
//        userAmount.getParams().put("haveFlexibleInvestmentFunds", 0);
//        List<UserAmount> userAmounts = userAmountMapper.selectUserAmountList(userAmount);
//        if (userAmounts.size() > 0){
//            //用户今日存入的灵活投资资金
//            Map<Long, Map<String, Object>> userTransferredLessThan24HoursFlexibleInvestmentFunds = userBillDetailMapper.getUserTransferredLessThan24HoursFlexibleInvestmentFunds();
//            //灵活投资收益率(%)
//            BigDecimal flexibleInvestmentRate = CacheUtil.getOtherValueByKey("flexible_investment_rate", BigDecimal.class);
//            //线程池
//            ExecutorService executorService = Executors.newSingleThreadExecutor();
//            //遍历
//            for (int i = 0; i < userAmounts.size(); i++) {
//                //用户钱包信息
//                UserAmount userAmountVo = userAmounts.get(i);
//                executorService.execute(()->{
//                    //灵活投资资金
//                    BigDecimal flexibleInvestmentFunds = userAmountVo.getFlexibleInvestmentFunds();
//                    //存入未满24小时的投资资金
//                    BigDecimal subtractAmount = null;
//                    Map<String, Object> map = userTransferredLessThan24HoursFlexibleInvestmentFunds.get(userAmountVo.getUserId());
//                    if (map != null){
//                        subtractAmount = (BigDecimal) map.get("transferredFlexibleInvestmentFunds");
//                    }
//                    //如果有未满24小时的投资资金
//                    if (subtractAmount != null){
//                        //扣除未满24小时的投资资金
//                        flexibleInvestmentFunds = flexibleInvestmentFunds.subtract(subtractAmount);
//                    }
//                    if (flexibleInvestmentFunds.compareTo(BigDecimal.ZERO) <= 0){
//                        return;
//                    }
//                    //收益
//                    BigDecimal profit = flexibleInvestmentFunds.multiply(flexibleInvestmentRate).divide(new BigDecimal("100"), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
//                    //如果有收益
//                    if (profit.compareTo(BigDecimal.ZERO) > 0){
//                        //更新用户钱包信息
//                        userAmountVo.setFlexibleInvestmentFunds(userAmountVo.getFlexibleInvestmentFunds().add(profit));
//                        int updateUserAmount = userAmountMapper.updateUserAmount(userAmountVo);
//                        if (updateUserAmount ==0 ){
//                            log.error("灵活投资派发收益异常，用户：" + userAmountVo.getUserId() + "，灵活投资资金：" + flexibleInvestmentFunds + "，时间：" + DateUtils.getTime());
//                            throw new ServiceException("更新用户钱包信息异常");
//                        }
//                    }else {
//                        log.error("灵活投资派发收益异常，用户：" + userAmountVo.getUserId() + "，灵活投资资金：" + flexibleInvestmentFunds + "，时间：" + DateUtils.getTime() + "，原因：四舍五入后收益为0");
//                    }
//                });
//            }
//            executorService.shutdown();
//        }
//    }
//}
