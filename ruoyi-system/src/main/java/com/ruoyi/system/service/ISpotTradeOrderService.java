package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SpotTradeOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 现货交易订单Service接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface ISpotTradeOrderService 
{
    /**
     * 查询现货交易订单
     * 
     * @param id 现货交易订单主键
     * @return 现货交易订单
     */
    public SpotTradeOrder selectSpotTradeOrderById(Long id);

    /**
     * 查询现货交易订单列表
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 现货交易订单集合
     */
    public List<SpotTradeOrder> selectSpotTradeOrderList(SpotTradeOrder spotTradeOrder);

    /**
     * 填充其他信息
     * @param spotTradeOrders 现货交易订单列表
     */
    public void fillOtherInfo(List<SpotTradeOrder> spotTradeOrders);

    /**
     * 新增现货交易订单
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 结果
     */
    public int insertSpotTradeOrder(SpotTradeOrder spotTradeOrder);

    /**
     * 修改现货交易订单
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 结果
     */
    public int updateSpotTradeOrder(SpotTradeOrder spotTradeOrder);

    /**
     * 批量删除现货交易订单
     * 
     * @param ids 需要删除的现货交易订单主键集合
     * @return 结果
     */
    public int deleteSpotTradeOrderByIds(Long[] ids);

    /**
     * 删除现货交易订单信息
     * 
     * @param id 现货交易订单主键
     * @return 结果
     */
    public int deleteSpotTradeOrderById(Long id);

    /**
     * 用户现货交易下单
     * @param productType 产品类型
     * @param productCode 产品代码
     * @param orderNum 订单数量
     * @return
     */
    public AjaxResult addSpotTradeOrder(Integer productType, String productCode, BigDecimal orderNum);

    /**
     * 卖出现货交易订单
     * @param spotTradeOrderId 现货交易订单id
     * @param doType 平仓类型 0:强制平仓 1:用户平仓
     * @return
     */
    public AjaxResult sellSpotTradeOrder(Long spotTradeOrderId,Integer doType);
}
