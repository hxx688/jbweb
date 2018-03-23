package com.rrgy.person.model;

import java.math.BigDecimal;

import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.SeqID;
import org.beetl.sql.core.annotatoin.Table;

import com.rrgy.core.base.model.BaseModel;

@Table(name = "dt_user_groups")
@SuppressWarnings("serial")
public class PersonGroup extends BaseModel{
	private Integer id; //自增ID
	private String title; //联系地址
	private Integer grade; //会员等级值
	private Integer upgrade_exp; //升级经验值
	private BigDecimal amount; //默认预存款
	private Integer point; //默认积分
	private Integer discount; //购物折扣
	private Integer is_default; //是否注册用户组
	private Integer is_upgrade; //是否自动升级
	private Integer is_lock; //是否禁用
	
	@AutoID
	@SeqID(name = "SEQ_USERGROUP")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public Integer getUpgrade_exp() {
		return upgrade_exp;
	}
	public void setUpgrade_exp(Integer upgrade_exp) {
		this.upgrade_exp = upgrade_exp;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public Integer getIs_default() {
		return is_default;
	}
	public void setIs_default(Integer is_default) {
		this.is_default = is_default;
	}
	public Integer getIs_upgrade() {
		return is_upgrade;
	}
	public void setIs_upgrade(Integer is_upgrade) {
		this.is_upgrade = is_upgrade;
	}
	public Integer getIs_lock() {
		return is_lock;
	}
	public void setIs_lock(Integer is_lock) {
		this.is_lock = is_lock;
	}
	
	
}
