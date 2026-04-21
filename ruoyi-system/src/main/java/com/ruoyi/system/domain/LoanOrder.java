package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 贷款订单对象 loan_order
 * 
 * @author ruoyi
 * @date 2024-05-22
 */
public class LoanOrder extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderCode;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 贷款产品id */
    @Excel(name = "贷款产品id")
    private Long loanProductId;

    /** 贷款产品名称 */
    @Excel(name = "贷款产品名称")
    private String productName;

    /** 产品名称多语言 */
    @Excel(name = "产品名称多语言")
    private LangMgr productNameLang;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal orderPrice;

    /** 实际放款金额 */
    @Excel(name = "实际放款金额")
    private BigDecimal realLoanAmount;

    /** 借贷天数 */
    @Excel(name = "借贷天数")
    private Integer loanDays;

    /** 免息天数 */
    @Excel(name = "免息天数")
    private Integer interestFreeDays;

    /** 贷款每日利息率 */
    @Excel(name = "贷款每日利息率")
    private BigDecimal loanDailyRate;

    /** 违约每日利息率 */
    @Excel(name = "违约每日利息率")
    private BigDecimal breakContractDailyRate;

    /** 贷款开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "贷款开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loanStartTime;

    /** 贷款结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "贷款结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loanEndTime;

    /** 已贷款天数 **/
    @Excel(name = "已贷款天数")
    private Integer alreadyLoanDays;

    /** 需要支付利息 **/
    @Excel(name = "需要支付利息")
    private BigDecimal needPayInterest;

    /** 利息结算方式 0：每日余额扣除利息 1：到期还款结清总利息 */
    @Excel(name = "利息结算方式 0：每日余额扣除利息 1：到期还款结清总利息")
    private Integer interestSettlementMethod;

    /** 还款方式 0：联系客服 1：提交还款订单 */
    @Excel(name = "还款方式 0：联系客服 1：提交还款订单")
    private Integer repaymentMethod;

    /** 订单状态：0：待审核  1：进行中 2：已完成 3：已驳回 */
    @Excel(name = "订单状态：0：待审核  1：进行中 2：已完成 3：已驳回")
    private Integer orderStatus;

    /** 货币id */
    @Excel(name = "货币id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 国家 */
    @Excel(name = "国家")
    private String userInfoCountry;

    /** 直系亲属姓名 */
    @Excel(name = "直系亲属姓名")
    private String immediateFamilyMemberName;

    /** 直系亲属电话号码 */
    @Excel(name = "直系亲属电话号码")
    private String immediateFamilyMemberPhoneNumber;

    /** 居住地址 */
    @Excel(name = "居住地址")
    private String residentialAddress;

    /** 证件类型 0：身份证 1：驾驶证 2：护照 */
    @Excel(name = "证件类型 0：身份证 1：驾驶证 2：护照")
    private Integer idType;

    /** 证件号码 */
    @Excel(name = "证件号码")
    private String idNumber;

    /** 证件图片（正面） */
    @Excel(name = "证件图片", readConverterExp = "正=面")
    private String img1Key;

    /** 证件图片（背面） */
    @Excel(name = "证件图片", readConverterExp = "背=面")
    private String img2Key;

    /** 证件图片（手持） */
    @Excel(name = "证件图片", readConverterExp = "手=持")
    private String img3Key;

    /** 银行流水清单 */
    @Excel(name = "银行流水清单")
    private String img4Key;

    /** 担保人ID */
    @Excel(name = "担保人ID")
    private Long guarantorId;

    /** 担保人名称 */
    @Excel(name = "担保人名称")
    private String guarantorName;

    /** 是否统计报表 0：是 1：否 */
    @Excel(name = "是否统计报表 0：是 1：否")
    private Integer statisticalReport;


    public Integer getStatisticalReport() {
        return statisticalReport;
    }

    public void setStatisticalReport(Integer statisticalReport) {
        this.statisticalReport = statisticalReport;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderCode(String orderCode) 
    {
        this.orderCode = orderCode;
    }

    public String getOrderCode() 
    {
        return orderCode;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setLoanProductId(Long loanProductId) 
    {
        this.loanProductId = loanProductId;
    }

    public Long getLoanProductId() 
    {
        return loanProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LangMgr getProductNameLang() {
        return productNameLang;
    }

    public void setProductNameLang(LangMgr productNameLang) {
        this.productNameLang = productNameLang;
    }

    public void setOrderPrice(BigDecimal orderPrice)
    {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderPrice() 
    {
        return orderPrice;
    }

    public BigDecimal getRealLoanAmount() {
        return realLoanAmount;
    }

    public void setRealLoanAmount(BigDecimal realLoanAmount) {
        this.realLoanAmount = realLoanAmount;
    }

    public void setLoanDays(Integer loanDays)
    {
        this.loanDays = loanDays;
    }

    public Integer getLoanDays() 
    {
        return loanDays;
    }
    public void setInterestFreeDays(Integer interestFreeDays) 
    {
        this.interestFreeDays = interestFreeDays;
    }

    public Integer getInterestFreeDays() 
    {
        return interestFreeDays;
    }
    public void setLoanDailyRate(BigDecimal loanDailyRate) 
    {
        this.loanDailyRate = loanDailyRate;
    }

    public BigDecimal getLoanDailyRate() 
    {
        return loanDailyRate;
    }
    public void setBreakContractDailyRate(BigDecimal breakContractDailyRate) 
    {
        this.breakContractDailyRate = breakContractDailyRate;
    }

    public BigDecimal getBreakContractDailyRate() 
    {
        return breakContractDailyRate;
    }
    public void setLoanStartTime(Date loanStartTime) 
    {
        this.loanStartTime = loanStartTime;
    }

    public Date getLoanStartTime() 
    {
        return loanStartTime;
    }
    public void setLoanEndTime(Date loanEndTime) 
    {
        this.loanEndTime = loanEndTime;
    }

    public Date getLoanEndTime() 
    {
        return loanEndTime;
    }

    public Integer getAlreadyLoanDays() {
        return alreadyLoanDays;
    }

    public void setAlreadyLoanDays(Integer alreadyLoanDays) {
        this.alreadyLoanDays = alreadyLoanDays;
    }

    public BigDecimal getNeedPayInterest() {
        return needPayInterest;
    }

    public void setNeedPayInterest(BigDecimal needPayInterest) {
        this.needPayInterest = needPayInterest;
    }

    public void setInterestSettlementMethod(Integer interestSettlementMethod)
    {
        this.interestSettlementMethod = interestSettlementMethod;
    }

    public Integer getInterestSettlementMethod() 
    {
        return interestSettlementMethod;
    }

    public Integer getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(Integer repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public void setOrderStatus(Integer orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus() 
    {
        return orderStatus;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setUserInfoCountry(String userInfoCountry)
    {
        this.userInfoCountry = userInfoCountry;
    }

    public String getUserInfoCountry() 
    {
        return userInfoCountry;
    }
    public void setImmediateFamilyMemberName(String immediateFamilyMemberName) 
    {
        this.immediateFamilyMemberName = immediateFamilyMemberName;
    }

    public String getImmediateFamilyMemberName() 
    {
        return immediateFamilyMemberName;
    }
    public void setImmediateFamilyMemberPhoneNumber(String immediateFamilyMemberPhoneNumber) 
    {
        this.immediateFamilyMemberPhoneNumber = immediateFamilyMemberPhoneNumber;
    }

    public String getImmediateFamilyMemberPhoneNumber() 
    {
        return immediateFamilyMemberPhoneNumber;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public void setIdType(Integer idType)
    {
        this.idType = idType;
    }

    public Integer getIdType() 
    {
        return idType;
    }
    public void setIdNumber(String idNumber) 
    {
        this.idNumber = idNumber;
    }

    public String getIdNumber() 
    {
        return idNumber;
    }
    public void setImg1Key(String img1Key) 
    {
        this.img1Key = img1Key;
    }

    public String getImg1Key() 
    {
        return img1Key;
    }
    public void setImg2Key(String img2Key) 
    {
        this.img2Key = img2Key;
    }

    public String getImg2Key() 
    {
        return img2Key;
    }
    public void setImg3Key(String img3Key) 
    {
        this.img3Key = img3Key;
    }

    public String getImg3Key() 
    {
        return img3Key;
    }
    public void setImg4Key(String img4Key) 
    {
        this.img4Key = img4Key;
    }

    public String getImg4Key() 
    {
        return img4Key;
    }

    public Long getGuarantorId() {
        return guarantorId;
    }

    public void setGuarantorId(Long guarantorId) {
        this.guarantorId = guarantorId;
    }

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderCode", getOrderCode())
            .append("userId", getUserId())
            .append("loanProductId", getLoanProductId())
            .append("orderPrice", getOrderPrice())
            .append("loanDays", getLoanDays())
            .append("interestFreeDays", getInterestFreeDays())
            .append("loanDailyRate", getLoanDailyRate())
            .append("breakContractDailyRate", getBreakContractDailyRate())
            .append("loanStartTime", getLoanStartTime())
            .append("loanEndTime", getLoanEndTime())
            .append("createTime", getCreateTime())
            .append("interestSettlementMethod", getInterestSettlementMethod())
            .append("orderStatus", getOrderStatus())
            .append("currencyId", getCurrencyId())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("sqlVersion", getSqlVersion())
            .append("userInfoCountry", getUserInfoCountry())
            .append("immediateFamilyMemberName", getImmediateFamilyMemberName())
            .append("immediateFamilyMemberPhoneNumber", getImmediateFamilyMemberPhoneNumber())
            .append("idType", getIdType())
            .append("idNumber", getIdNumber())
            .append("img1Key", getImg1Key())
            .append("img2Key", getImg2Key())
            .append("img3Key", getImg3Key())
            .append("img4Key", getImg4Key())
            .toString();
    }
}
