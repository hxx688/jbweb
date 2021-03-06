package com.rrgy.exchange.model;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import org.beetl.sql.core.annotatoin.Table;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.rrgy.core.base.model.BaseModel;

/**
 * Generated by Blade.
 * 2017-03-10 14:48:55
 */
@Table(name = "exchange_order")
@SuppressWarnings("serial")
public class ExchangeOrder extends BaseModel {

	private Integer id;
	@Excel(name="订单来源",orderNum="4",replace={"PC端_1","安卓端_2","苹果端_3"})
	private Integer order_source; //订单来源(1:pc,2:android,3:ios)
	@Excel(name="用户ID",orderNum="2")
	private Integer order_user_id; //用户id
	@Excel(name="状态",orderNum="5",replace={"待付款_0","待发货_1","已发货_2","已确认_3"})
	private Integer status; //状态(0:待付款,1:待发货,2:已发货,3:已确认)
	@Excel(name="快递单号",orderNum="11")
	private String courier_number; //快递单号
	private BigDecimal goods_total_amount; //商品总额(包含运费)
	private BigDecimal order_carriage; //运费
	@Excel(name="物品总数量",orderNum="7")
	private Long order_count; //订单物品总数量
	@Excel(name="快递公司",orderNum="12")
	private String order_express; //快递公司
	@Excel(name="订单编号",orderNum="6")
	private String order_number; //订单编号
	@Excel(name="订单总额",orderNum="2")
	private BigDecimal order_total_amount; //订单总额
	@Excel(name="用户名",orderNum="3")
	private String order_user_name; //用户名
	@Excel(name="确认时间",orderNum="13",exportFormat="yyyy-MM-dd HH:mm:ss")
	private Date affirm_time; //确认时间
	@Excel(name="下单时间",orderNum="8",exportFormat="yyyy-MM-dd HH:mm:ss")
	private Date buy_time; //添加日期
	@Excel(name="发货时间",orderNum="10",exportFormat="yyyy-MM-dd HH:mm:ss")
	private Date delivery_time; //发货时间
	@Excel(name="付款时间",orderNum="9",exportFormat="yyyy-MM-dd HH:mm:ss")
	private Date pay_time; //付款时间
	
	//扩展属性
	private String order_source_name;
	private String  statusName;
	
	private String address; // 地址
	private String consignee; // 收货人
	private String phone; // 联系电话
	private String postcode; // 邮编
	private String province_name; // 省
	private String city_name; // 市
	private String area_name;// 区
	
	private ExchangeOrderAddress orderAddress;//订单收货信息
	private List<ExchangeOrderDetail> orderDetailList;//订单商品列表
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOrder_source() {
		return order_source;
	}
	
	public void setOrder_source(Integer order_source) {
		this.order_source = order_source;
	}
	
	public Integer getOrder_user_id() {
		return order_user_id;
	}
	
	public void setOrder_user_id(Integer order_user_id) {
		this.order_user_id = order_user_id;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getCourier_number() {
		return courier_number;
	}
	
	public void setCourier_number(String courier_number) {
		this.courier_number = courier_number;
	}
	
	public BigDecimal getGoods_total_amount() {
		return goods_total_amount;
	}
	
	public void setGoods_total_amount(BigDecimal goods_total_amount) {
		this.goods_total_amount = goods_total_amount;
	}
	
	public BigDecimal getOrder_carriage() {
		return order_carriage;
	}
	
	public void setOrder_carriage(BigDecimal order_carriage) {
		this.order_carriage = order_carriage;
	}
	
	public Long getOrder_count() {
		return order_count;
	}
	
	public void setOrder_count(Long order_count) {
		this.order_count = order_count;
	}
	
	public String getOrder_express() {
		return order_express;
	}
	
	public void setOrder_express(String order_express) {
		this.order_express = order_express;
	}
	
	public String getOrder_number() {
		return order_number;
	}
	
	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	
	public BigDecimal getOrder_total_amount() {
		return order_total_amount;
	}
	
	public void setOrder_total_amount(BigDecimal order_total_amount) {
		this.order_total_amount = order_total_amount;
	}
	
	public String getOrder_user_name() {
		return order_user_name;
	}
	
	public void setOrder_user_name(String order_user_name) {
		this.order_user_name = order_user_name;
	}
	
	public Date getAffirm_time() {
		return affirm_time;
	}
	
	public void setAffirm_time(Date affirm_time) {
		this.affirm_time = affirm_time;
	}
	
	public Date getBuy_time() {
		return buy_time;
	}

	public void setBuy_time(Date buy_time) {
		this.buy_time = buy_time;
	}

	public Date getDelivery_time() {
		return delivery_time;
	}
	
	public void setDelivery_time(Date delivery_time) {
		this.delivery_time = delivery_time;
	}
	
	public Date getPay_time() {
		return pay_time;
	}
	
	public void setPay_time(Date pay_time) {
		this.pay_time = pay_time;
	}

	public String getStatusName() {
		if(status==0){
			statusName = "待付款";
		}else if(status==1){
			statusName = "待发货";
		}else if(status==2){
			statusName = "已发货";
		}else if(status==3){
			statusName = "已确认";
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getOrder_source_name() {
		return order_source_name;
	}

	public void setOrder_source_name(String order_source_name) {
		this.order_source_name = order_source_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public ExchangeOrderAddress getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(ExchangeOrderAddress orderAddress) {
		this.orderAddress = orderAddress;
	}

	public List<ExchangeOrderDetail> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<ExchangeOrderDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}


}
