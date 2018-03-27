package com.rrgy.industry.model;


import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.Table;
import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2017-01-04 14:59:34
 */
@Table(name = "dt_hangye")
@SuppressWarnings("serial")
public class Industry extends BaseModel {

	private Integer id;
	private Integer sort_id;
	private String title; //行业名称
	
	@AutoID
	@SeqID(name = "SEQ_INDS")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getSort_id() {
		return sort_id;
	}
	
	public void setSort_id(Integer sort_id) {
		this.sort_id = sort_id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	

}