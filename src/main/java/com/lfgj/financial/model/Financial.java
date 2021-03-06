package com.lfgj.financial.model;

import java.util.Date;
import java.math.BigDecimal;

import org.beetl.sql.core.annotatoin.Table;
import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2017-09-18 21:29:22
 */
@Table(name = "dt_financial")
@SuppressWarnings("serial")
public class Financial extends BaseModel {

	private Integer id;
	private Integer financial_type; //1收入，２支出
	private Integer user_id; //ID
	private BigDecimal amount; //金额
	private String desc;
	private String phone; //电话
	private String source_type; //来源
	private BigDecimal user_amount; //帐户金额
	private String user_name; //姓名
	private Date create_time;
	private String orderNo;
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getFinancial_type() {
		return financial_type;
	}
	
	public void setFinancial_type(Integer financial_type) {
		this.financial_type = financial_type;
	}
	
	public Integer getUser_id() {
		return user_id;
	}
	
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getSource_type() {
		return source_type;
	}
	
	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}
	
	public BigDecimal getUser_amount() {
		return user_amount;
	}
	
	public void setUser_amount(BigDecimal user_amount) {
		this.user_amount = user_amount;
	}
	
	public String getUser_name() {
		return user_name;
	}
	
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public Date getCreate_time() {
		return create_time;
	}
	
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	

}
