package com.rrgy.sms.model;

import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;
import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2017-01-04 16:31:00
 */
@Table(name = "wx_sms_config")
@SuppressWarnings("serial")
public class SmsConfig extends BaseModel {

	private Integer id; //编号
	private Integer sortId; //排序号
	private Integer wid; //微帐号id
	private String pwd; //密码
	private String qianming; //签名
	private String remark; //备注
	private String uName; //姓名
	private String ucode; //帐号
	private Date createDate; //创建时间
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getSortId() {
		return sortId;
	}
	
	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}
	
	public Integer getWid() {
		return wid;
	}
	
	public void setWid(Integer wid) {
		this.wid = wid;
	}
	
	public String getPwd() {
		return pwd;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public String getQianming() {
		return qianming;
	}
	
	public void setQianming(String qianming) {
		this.qianming = qianming;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getuName() {
		return uName;
	}
	
	public void setuName(String uName) {
		this.uName = uName;
	}
	
	public String getUcode() {
		return ucode;
	}
	
	public void setUcode(String ucode) {
		this.ucode = ucode;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	

}
