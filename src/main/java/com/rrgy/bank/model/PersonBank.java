package com.rrgy.bank.model;

import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

@Table(name = "dt_yinhang")
@SuppressWarnings("serial")
public class PersonBank extends BaseModel{
	private Integer id; //自增ID
    private String name; //银行名称
    private String img; //logo
    private String sort_id; //排序
    private String jianma; //简码
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getSort_id() {
		return sort_id;
	}
	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}
	public String getJianma() {
		return jianma;
	}
	public void setJianma(String jianma) {
		this.jianma = jianma;
	}
    
    
	
}
