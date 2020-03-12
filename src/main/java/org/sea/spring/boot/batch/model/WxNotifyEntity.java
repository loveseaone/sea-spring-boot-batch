package org.sea.spring.boot.batch.model;

import java.math.BigInteger;
import java.util.Date;

/**
 * WxNotifyEntity BaseEntity
 * @author system
 */
public class WxNotifyEntity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 序列
     */
	private BigInteger id;	
	/**
     * 账号配置
     */
	private BigInteger accountid;	
	/**
     * 通信标识
     */
	private String returnCode;	
	/**
     * 业务结果
     */
	private String resultCode;	
	/**
     * 公众账号ID
     */
	private String appid;	
	/**
     * 用户标识
     */
	private String openid;	
	/**
     * 订单金额
     */
	private BigInteger totalFee;	
	/**
     * 微信订单号
     */
	private String transactionId;	
	/**
     * 商户订单号
     */
	private String outTradeNo;	
	/**
     * 支付完成时间
     */
	private String timeEnd;	
	/**
     * 支付通知
     */
	private String wxpayxml;	
	/**
     * 状态
     */
	private Integer state;	
	/**
     * 添加时间
     */
	private Date addtime;	
    
	public BigInteger getId(){
		return id;
	}
	public void setId(BigInteger id){
		this.id = id;
	}
	public BigInteger getAccountid(){
		return accountid;
	}
	public void setAccountid(BigInteger accountid){
		this.accountid = accountid;
	}
	public String getReturnCode(){
		return returnCode;
	}
	public void setReturnCode(String returnCode){
		this.returnCode = returnCode;
	}
	public String getResultCode(){
		return resultCode;
	}
	public void setResultCode(String resultCode){
		this.resultCode = resultCode;
	}
	public String getAppid(){
		return appid;
	}
	public void setAppid(String appid){
		this.appid = appid;
	}
	public String getOpenid(){
		return openid;
	}
	public void setOpenid(String openid){
		this.openid = openid;
	}
	public BigInteger getTotalFee(){
		return totalFee;
	}
	public void setTotalFee(BigInteger totalFee){
		this.totalFee = totalFee;
	}
	public String getTransactionId(){
		return transactionId;
	}
	public void setTransactionId(String transactionId){
		this.transactionId = transactionId;
	}
	public String getOutTradeNo(){
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo){
		this.outTradeNo = outTradeNo;
	}
	public String getTimeEnd(){
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd){
		this.timeEnd = timeEnd;
	}
	public String getWxpayxml(){
		return wxpayxml;
	}
	public void setWxpayxml(String wxpayxml){
		this.wxpayxml = wxpayxml;
	}
	public Integer getState(){
		return state;
	}
	public void setState(Integer state){
		this.state = state;
	}
	public Date getAddtime(){
		return addtime;
	}
	public void setAddtime(Date addtime){
		this.addtime = addtime;
	}
}