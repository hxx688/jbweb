package com.rrgy.person.model;

import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade. 2017-03-09 17:17:11
 */
@Table(name = "dt_delivery_address")
@SuppressWarnings("serial")
public class DeliveryAddress extends BaseModel {

	private Integer id; // 主键id
	private Integer is_default; // 是否默认(0:非,1:是)
	private Integer user_id;// 用户id
	private String address; // 地址
	private String area; // 区/镇/县
	private String city; // 市
	private String consignee; // 收货人
	private String phone; // 联系电话
	private String postcode; // 邮编
	private String province; // 省
	private String province_name;
	private String city_name;
	private String area_name;
	private Date create_time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIs_default() {
		return is_default;
	}

	public void setIs_default(Integer is_default) {
		this.is_default = is_default;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
