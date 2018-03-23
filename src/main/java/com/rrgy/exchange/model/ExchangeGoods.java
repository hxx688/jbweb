package com.rrgy.exchange.model;

import java.util.Date;
import java.math.BigDecimal;

import org.beetl.sql.core.annotatoin.Table;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2017-03-09 13:54:49
 */
@Table(name = "exchange_goods")
@SuppressWarnings("serial")
public class ExchangeGoods extends BaseModel {

	private Integer id;
	private Integer category_id; //类别id
	private Integer sort; //排序
	@Excel(name="状态",orderNum="5",replace={"已下架_0","已上架_1"})
	private Integer status; //状态(0:下架,1:上架;2:删除)
	@Excel(name="兑换积分",orderNum="4")
	private BigDecimal exchange_credits; //兑换积分
	private String goods_details; //商品详情
	private String goods_img1; //商品图片1(主图,不能为空)
	private String goods_img2; //商品图片2
	private String goods_img3; //商品图片3
	private String goods_img4; //商品图片4
	private String goods_img5; //商品图片5
	@Excel(name="商品名称",orderNum="1")
	private String goods_name; //商品名称
	@Excel(name="商品编码",orderNum="2")
	private String goods_number; //商品编码
	@Excel(name="库存量",orderNum="3")
	private Long stock; //库存量
	@Excel(name="日期",orderNum="6",exportFormat="yyyy-MM-dd HH:mm:ss")
	private Date create_time; //添加日期
	
	//扩展属性
	private String  statusName;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getCategory_id() {
		return category_id;
	}
	
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	
	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public BigDecimal getExchange_credits() {
		return exchange_credits;
	}
	
	public void setExchange_credits(BigDecimal exchange_credits) {
		this.exchange_credits = exchange_credits;
	}
	
	public String getGoods_details() {
		return goods_details;
	}
	
	public void setGoods_details(String goods_details) {
		this.goods_details = goods_details;
	}
	
	public String getGoods_img1() {
		return goods_img1;
	}
	
	public void setGoods_img1(String goods_img1) {
		this.goods_img1 = goods_img1;
	}
	
	public String getGoods_img2() {
		return goods_img2;
	}
	
	public void setGoods_img2(String goods_img2) {
		this.goods_img2 = goods_img2;
	}
	
	public String getGoods_img3() {
		return goods_img3;
	}
	
	public void setGoods_img3(String goods_img3) {
		this.goods_img3 = goods_img3;
	}
	
	public String getGoods_img4() {
		return goods_img4;
	}
	
	public void setGoods_img4(String goods_img4) {
		this.goods_img4 = goods_img4;
	}
	
	public String getGoods_img5() {
		return goods_img5;
	}
	
	public void setGoods_img5(String goods_img5) {
		this.goods_img5 = goods_img5;
	}
	
	public String getGoods_name() {
		return goods_name;
	}
	
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	
	public String getGoods_number() {
		return goods_number;
	}
	
	public void setGoods_number(String goods_number) {
		this.goods_number = goods_number;
	}
	
	public Long getStock() {
		return stock;
	}
	
	public void setStock(Long stock) {
		this.stock = stock;
	}
	
	public Date getCreate_time() {
		return create_time;
	}
	
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	

}
