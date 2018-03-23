package com.lfgj.product.model;

import java.math.BigDecimal;
import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

@Table(name = "dt_date_adjust")
@SuppressWarnings("serial")
public class DateAdjust extends BaseModel {
	private Integer id;
	private BigDecimal high; // 最高
	private BigDecimal low; // 最低
	private BigDecimal first; // 第一次
	private BigDecimal last; // 最后一次
	private String code;
	private Date create_date; // 创建时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getFirst() {
		return first;
	}

	public void setFirst(BigDecimal first) {
		this.first = first;
	}

	public BigDecimal getLast() {
		return last;
	}

	public void setLast(BigDecimal last) {
		this.last = last;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
