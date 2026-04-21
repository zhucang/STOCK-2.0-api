package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserBillDetail;
import com.ruoyi.system.mapper.UserBillDetailMapper;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户流水记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
@Service
public class UserBillDetailServiceImpl implements IUserBillDetailService 
{
    @Resource
    private UserBillDetailMapper userBillDetailMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询用户流水记录
     * 
     * @param id 用户流水记录主键
     * @return 用户流水记录
     */
    @Override
    public UserBillDetail selectUserBillDetailById(Long id)
    {
        return userBillDetailMapper.selectUserBillDetailById(id);
    }

    /**
     * 查询用户流水记录列表
     * 
     * @param userBillDetail 用户流水记录
     * @return 用户流水记录
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserBillDetail> selectUserBillDetailList(UserBillDetail userBillDetail)
    {
        List<UserBillDetail> userBillDetails = userBillDetailMapper.selectUserBillDetailList(userBillDetail);
        try{
            if (userBillDetail.getParams().get("getLang") != null){
                fillLang(userBillDetails);
            }
        }catch (Exception e){

        }
        return userBillDetails;
    }

    void fillLang(List<UserBillDetail> userBillDetails){
        for (int i = 0; i < userBillDetails.size(); i++) {
            //账单信息
            UserBillDetail userBillDetailVo = userBillDetails.get(i);
            //订单类型
            Integer orderClass = userBillDetailVo.getOrderClass();
            //标题key
            String deTypeKey = "deType_orderClass_" + orderClass;
            //内容key
            String deSummaryKey = "deSummary_orderClass_" + orderClass;
            //values
            List<Object> values = null;
            if (StringUtils.isNotEmpty(userBillDetailVo.getRemark())){
                values = Arrays.asList(userBillDetailVo.getRemark().split("/"));
            }
            //个性化
            if (orderClass.equals(5)){
                //股票平仓
                //是否收取印花税
                Integer isChargeYHS = switchSetService.selectSwitchStatusById(39L);
                if (isChargeYHS != null && isChargeYHS.equals(0)){
                    deSummaryKey = "deSummary_productType_1";
                }else {
                    deSummaryKey = "deSummary_stock_yhs";
                    values.remove(4);
                }
            }else if(orderClass.equals(7)){
                //加密货币平仓
                deSummaryKey = "deSummary_productType_2";
            }else if(orderClass.equals(26)){
                //期货平仓
                deSummaryKey = "deSummary_productType_3";
            }else if(orderClass.equals(44)){
                //外汇平仓
                deSummaryKey = "deSummary_productType_4";
            }
            Map<String, Object> params = userBillDetailVo.getParams();
            params.put("deTypeKey",deTypeKey);
            params.put("deSummaryKey",deSummaryKey);
            params.put("values",values);
        }
    }

    /**
     * 新增用户流水记录
     * 
     * @param userBillDetail 用户流水记录
     * @return 结果
     */
    @Override
    public int insertUserBillDetail(UserBillDetail userBillDetail)
    {
        return userBillDetailMapper.insertUserBillDetail(userBillDetail);
    }

    /**
     * 修改用户流水记录
     * 
     * @param userBillDetail 用户流水记录
     * @return 结果
     */
    @Override
    public int updateUserBillDetail(UserBillDetail userBillDetail)
    {
        return userBillDetailMapper.updateUserBillDetail(userBillDetail);
    }

    /**
     * 批量删除用户流水记录
     * 
     * @param ids 需要删除的用户流水记录主键
     * @return 结果
     */
    @Override
    public int deleteUserBillDetailByIds(Long[] ids)
    {
        return userBillDetailMapper.deleteUserBillDetailByIds(ids);
    }

    /**
     * 删除用户流水记录信息
     * 
     * @param id 用户流水记录主键
     * @return 结果
     */
    @Override
    public int deleteUserBillDetailById(Long id)
    {
        return userBillDetailMapper.deleteUserBillDetailById(id);
    }

    /**
     * 获取用户某币种的可回收彩金金额
     * @param userId 用户id
     * @param currencyId 币种id
     * @return
     */
    @Override
    public BigDecimal getTotalOutWinningsByUserId(Long userId, Long currencyId){
        return userBillDetailMapper.getTotalOutWinningsByUserId(userId,currencyId);
    }

    /**
     * 获取用户某币种的可解冻金额
     * @param userId 用户id
     * @param currencyId 币种id
     * @return
     */
    @Override
    public BigDecimal getAllowUnfreezeAmountByUserId(Long userId,Long currencyId){
        return userBillDetailMapper.getAllowUnfreezeAmountByUserId(userId,currencyId);
    }
}
