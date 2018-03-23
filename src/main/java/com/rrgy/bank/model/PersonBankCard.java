package com.rrgy.bank.model;

import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

@Table(name = "dt_yinhangka")
@SuppressWarnings("serial")
public class PersonBankCard extends BaseModel{
	private Integer id; //自增ID
    private Integer userid; //用户id
    private String kaihuhang; //自增ID
    private String kaihuming; //开户名
    private String kahao; //开户号
    private String xiangxindizhi; //联系地址
    private Integer status;//状态:0正常,1:删除
    
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
	public String getKahao() {
		return kahao;
	}
	public void setKahao(String kahao) {
		this.kahao = kahao;
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
	
}
