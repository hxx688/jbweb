package com.rrgy.city.model;

import java.util.ArrayList;
import java.util.List;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.annotation.BindID;
import com.rrgy.core.base.model.BaseModel;


@Table(name = "pre_common_district")
@BindID(name = "id")
@SuppressWarnings("serial")
public class City extends BaseModel {
	
	private Integer id; //主键
	private String name; //全称
	private Integer level; //层级
	private Integer upid; //上级
	
	private List<City> cityList = new ArrayList<City>();
	
	private List<City> districtList = new ArrayList<City>();
	
	@AutoID
	@SeqID(name = "SEQ_CITY")
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
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getUpid() {
		return upid;
	}
	public void setUpid(Integer upid) {
		this.upid = upid;
	}
	public List<City> getCityList() {
		return cityList;
	}
	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
	public List<City> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(List<City> districtList) {
		this.districtList = districtList;
	}
	
}
