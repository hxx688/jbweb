package com.lfgj.tixian.model;


import org.beetl.sql.core.annotatoin.Table;
import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2017-01-21 15:02:02
 */
@Table(name = "dt_yinhangka")
@SuppressWarnings("serial")
public class Yinhangka extends BaseModel {

	private Integer id;
	private Integer userid; //用户id
	private String kahao; //卡号
	private String kaihuhang; //开户行
	private String kaihuming; //开户名
	private String xiangxindizhi; //详细地址
	private String sheng; //省
	private String shi; //市
	private String mobile; //手机
	private Integer status;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUserid() {
		return userid;
	}
	
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
	public String getKahao() {
		return kahao;
	}
	
	public void setKahao(String kahao) {
		this.kahao = kahao;
	}
	
	public String getKaihuhang() {
		return kaihuhang;
	}
	
	public void setKaihuhang(String kaihuhang) {
		this.kaihuhang = kaihuhang;
	}
	
	public String getKaihuming() {
		return kaihuming;
	}
	
	public void setKaihuming(String kaihuming) {
		this.kaihuming = kaihuming;
	}
	
	public String getXiangxindizhi() {
		return xiangxindizhi;
	}
	
	public void setXiangxindizhi(String xiangxindizhi) {
		this.xiangxindizhi = xiangxindizhi;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSheng() {
		return sheng;
	}

	public void setSheng(String sheng) {
		this.sheng = sheng;
	}

	public String getShi() {
		return shi;
	}

	public void setShi(String shi) {
		this.shi = shi;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	

}