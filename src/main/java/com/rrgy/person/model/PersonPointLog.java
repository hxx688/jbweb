package com.rrgy.person.model;

import java.math.BigDecimal;
import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2016-12-29 12:54:25
 */
@Table(name = "dt_user_point_log")
@SuppressWarnings("serial")
public class PersonPointLog extends BaseModel {
	
	
	private Integer id; //自增ID
	private Integer user_id;//用户ID
	private BigDecimal value;//增减积分
	private String remark;//备注说明
	private Date add_time;//时间
	private Integer isadd;//1增加2减少
	private Integer leixing;//类型：1天使2商家
	
	
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
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}
	public Integer getIsadd() {
		return isadd;
	}
	public void setIsadd(Integer isadd) {
		this.isadd = isadd;
	}
	public Integer getLeixing() {
		return leixing;
	}
	public void setLeixing(Integer leixing) {
		this.leixing = leixing;
	}


}
