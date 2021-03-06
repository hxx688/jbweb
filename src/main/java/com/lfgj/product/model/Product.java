package com.lfgj.product.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2017-09-09 11:53:09
 */
@Table(name = "dt_product")
@SuppressWarnings("serial")
public class Product extends BaseModel {

	private Integer id;
	private Integer creator; //创建人
	private Integer group_id; //组ID
	private Integer sort; //排序
	private BigDecimal adjust; //动态调整
	private BigDecimal adjust_bili; //调整比例
	private String code; //代码
	private String product_name; //产品名称
	private String unit; //单位ID
	private Date create_time; //创建时间
	private Integer status;
	private BigDecimal diancs; //点乘数
	
	private String json;
	private String[] xAxis;
	private String[] yAxis;
	private Integer ix;
	private Collection day_json;
	private Collection week_json;
	
	private String sale_time;
	private String week;
	private BigDecimal bei;
	private String isSale;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getCreator() {
		return creator;
	}
	
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
	public Integer getGroup_id() {
		return group_id;
	}
	
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public BigDecimal getAdjust() {
		return adjust;
	}
	
	public void setAdjust(BigDecimal adjust) {
		this.adjust = adjust;
	}
	
	public BigDecimal getAdjust_bili() {
		return adjust_bili;
	}
	
	public void setAdjust_bili(BigDecimal adjust_bili) {
		this.adjust_bili = adjust_bili;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getProduct_name() {
		return product_name;
	}
	
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public Date getCreate_time() {
		return create_time;
	}
	
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String[] getxAxis() {
		return xAxis;
	}

	public void setxAxis(String[] xAxis) {
		this.xAxis = xAxis;
	}

	public String[] getyAxis() {
		return yAxis;
	}

	public void setyAxis(String[] yAxis) {
		this.yAxis = yAxis;
	}

	public Integer getIx() {
		return ix;
	}

	public void setIx(Integer ix) {
		this.ix = ix;
	}

	public Collection getDay_json() {
		return day_json;
	}

	public void setDay_json(Collection day_json) {
		this.day_json = day_json;
	}

	public Collection getWeek_json() {
		return week_json;
	}

	public void setWeek_json(Collection week_json) {
		this.week_json = week_json;
	}

	public String getSale_time() {
		return sale_time;
	}

	public void setSale_time(String sale_time) {
		this.sale_time = sale_time;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public BigDecimal getBei() {
		return bei;
	}

	public void setBei(BigDecimal bei) {
		this.bei = bei;
	}

	public BigDecimal getDiancs() {
		return diancs;
	}

	public void setDiancs(BigDecimal diancs) {
		this.diancs = diancs;
	}

	public String getIsSale() {
		return isSale;
	}

	public void setIsSale(String isSale) {
		this.isSale = isSale;
	}

}
