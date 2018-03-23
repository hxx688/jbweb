package com.lfgj.product.model;

import java.math.BigDecimal;
import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

@Table(name = "dt_adjust")
@SuppressWarnings("serial")
public class Adjust extends BaseModel {
	private Integer id;
	private String code;
	private BigDecimal adjust; // 动态调整
	private Date start_time; // 创建时间
	private Date end_time; // 结束时间
	private Integer user_id; // 创建人
	private Integer status;
	private BigDecimal bei;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAdjust() {
		return adjust;
	}

	public void setAdjust(BigDecimal adjust) {
		this.adjust = adjust;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getBei() {
		return bei;
	}

	public void setBei(BigDecimal bei) {
		this.bei = bei;
	}
	
}
