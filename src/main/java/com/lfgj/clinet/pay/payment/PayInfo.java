package com.lfgj.clinet.pay.payment;

import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

@Table(name = "dt_payinfo")
@SuppressWarnings("serial")
public class PayInfo extends BaseModel {

	private Integer id;
	private Integer user_id;
	private String ordernumber; // 商户订单号
	private String amount; // 交易金额
	private String payorderid; // 交易流水号
	private String businesstime; // 交易时间yyyy-MM-dd hh:mm:ss
	private String respcode; // 交易状态:1-待支付 2-支付完成 3-已关闭 4-交易撤销
	private String respmsg; // 状态说明
	private Date create_time; // 创建时间
	private String real_name; // 姓名
	private String mobile; // 手机号
	private String pay_acount; // 支付账号
	private String pay_type; // 通道类型
	private String pay_type_name; // 支付类型名称
	private String respname; // 支付状态名称
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getOrdernumber() {
		return ordernumber;
	}
	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPayorderid() {
		return payorderid;
	}
	public void setPayorderid(String payorderid) {
		this.payorderid = payorderid;
	}
	public String getBusinesstime() {
		return businesstime;
	}
	public void setBusinesstime(String businesstime) {
		this.businesstime = businesstime;
	}
	public String getRespcode() {
		return respcode;
	}
	public void setRespcode(String respcode) {
		this.respcode = respcode;
	}
	public String getRespmsg() {
		return respmsg;
	}
	public void setRespmsg(String respmsg) {
		this.respmsg = respmsg;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPay_acount() {
		return pay_acount;
	}
	public void setPay_acount(String pay_acount) {
		this.pay_acount = pay_acount;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getPay_type_name() {
		return pay_type_name;
	}
	public void setPay_type_name(String pay_type_name) {
		this.pay_type_name = pay_type_name;
	}
	public String getRespname() {
		return respname;
	}
	public void setRespname(String respname) {
		this.respname = respname;
	}

}
