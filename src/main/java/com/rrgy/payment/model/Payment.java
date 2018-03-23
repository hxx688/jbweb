package com.rrgy.payment.model;

import java.math.BigDecimal;

import org.beetl.sql.core.annotatoin.Table;
import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2017-01-04 13:26:54
 */
@Table(name = "dt_payment")
@SuppressWarnings("serial")
public class Payment extends BaseModel {

	private Integer id; //自增ID
	private Integer is_lock; //是否启用
	private Integer pTypeId; //支付类型
	private Integer poundage_type; //手续费类型1百分比2固定金额
	private Integer sort_id; //排序
	private Integer type; //支付类型1线上2线下
	private Integer wid; //微帐号id
	private String api_path; //API目录名称
	private String img_url; //显示图片
	private BigDecimal poundage_amount; //手续费金额
	private String remark; //备注说明
	private String title; //支付名称
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIs_lock() {
		return is_lock;
	}
	
	public void setIs_lock(Integer is_lock) {
		this.is_lock = is_lock;
	}
	
	public Integer getpTypeId() {
		return pTypeId;
	}
	
	public void setpTypeId(Integer pTypeId) {
		this.pTypeId = pTypeId;
	}
	
	public Integer getPoundage_type() {
		return poundage_type;
	}
	
	public void setPoundage_type(Integer poundage_type) {
		this.poundage_type = poundage_type;
	}
	
	public Integer getSort_id() {
		return sort_id;
	}
	
	public void setSort_id(Integer sort_id) {
		this.sort_id = sort_id;
	}
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getWid() {
		return wid;
	}
	
	public void setWid(Integer wid) {
		this.wid = wid;
	}
	
	public String getApi_path() {
		return api_path;
	}
	
	public void setApi_path(String api_path) {
		this.api_path = api_path;
	}
	
	public String getImg_url() {
		return img_url;
	}
	
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	
	public BigDecimal getPoundage_amount() {
		return poundage_amount;
	}
	
	public void setPoundage_amount(BigDecimal poundage_amount) {
		this.poundage_amount = poundage_amount;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	

}
