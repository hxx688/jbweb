package com.lfgj.financial.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.pay.payment.utils.PayUtil;
import com.lfgj.financial.model.Financial;
import com.lfgj.financial.service.PayInfoService;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.CommKit;
import com.lfgj.util.LfConstant;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.MathKit;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generated by Blade.
 * 2017-09-18 21:29:22
 */
@Service
public class PayInfoServiceImpl extends BaseService<Financial> implements PayInfoService{

	@Autowired
	MemberService memberService;

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 强制刷新状态
     * @param id
     * @return
     */
    @Override
	public boolean forceStatus(String id) throws Exception {

		// 获取订单信息
		PayInfo payInfo = Blade.create(PayInfo.class).findById(id);

		if (payInfo != null) {
			if ("0".equals(payInfo.getRespcode())) { // 非已支付状态
				try {

                    payInfo.setRespcode("6");
                    payInfo.setBusinesstime(DateKit.getTime());
                    payInfo.setRespmsg("刷新成功");
                    payInfo.setRespname(LfConstant.PAY_RESPCODE.get(payInfo.getRespcode()));
                    Blade.create(PayInfo.class).update(payInfo);

                    boolean temp = addRecharge(payInfo.getUser_id(),
                            new BigDecimal(payInfo.getAmount()), payInfo.getOrdernumber());
                    if (!temp) {
                        throw new RuntimeException("添加支付记录出错!");
                    }

				} catch (Exception e) {
                    log.error(e.getMessage(), e);
					throw new RuntimeException(e.toString());
				}
			} else {
				throw new RuntimeException("该支付订单已同步!");
			}
		} else {
            throw new RuntimeException("未查询到该支付订单记录!");
		}

        return true;
    }


    private boolean addRecharge(int id, BigDecimal amout, String orderNo) {

        String sql = "select * from dt_financial where orderNo=#{ordernumber}";
        Financial exist = Blade.create(Financial.class).findFirst(sql, Paras.create().set("ordernumber", orderNo));
        if(exist!=null){
            throw new RuntimeException("财务流水已存在!");
        }

        String sqlMember = "select * from lf_member where id=#{id}";
        Member m = Blade.create(Member.class).findFirst(sqlMember, Paras.create().set("id", id));
        if(m.getAmount() == null){
            m.setAmount(new BigDecimal("0"));
        }
        if(m.getRecharge() == null){
            m.setRecharge(new BigDecimal("0"));
        }

        m.setAmount(m.getAmount().add(amout));
        m.setRecharge(m.getRecharge().add(amout));

        boolean temp = Blade.create(Member.class).update(m);

        if(!temp){
            throw new RuntimeException("更新账户余额异常!");
        }

        Financial f = new Financial();
        f.setSource_type(LfConstant.Source.A.value);
        f.setUser_id(m.getId());
        f.setFinancial_type(1);
        f.setPhone(m.getMobile());
        f.setUser_name(m.getReal_name());
        f.setAmount(amout);
        f.setCreate_time(new Date());
        f.setUser_amount(m.getAmount());
        f.setDesc("0");
        f.setOrderNo(orderNo);
        temp = Blade.create(Financial.class).save(f);
        if(!temp){
            throw new RuntimeException("保存交易记录异常!");
        }

        return temp;

    }
	@Override
	public boolean syncStatus(String id) {
		// 获取订单信息
		PayInfo payInfo = Blade.create(PayInfo.class).findById(id);
		
		if(payInfo != null){
			String canRefPayType = "," + LfConstant.REFRESH_PAY_TYPE + ",";
			if(canRefPayType.indexOf(","+payInfo.getPay_type()+",")>=0){
				if(!"2".equals(payInfo.getRespcode())){ // 非已支付状态
					// 查询接口
					String appid = CommKit.getParameter("300").getPara();
					String key = CommKit.getParameter("301").getPara();
					String session = CommKit.getParameter("302").getPara();
					
					Map<String,Object> info = new HashMap<String,Object>();
					info.put("orderNumber",payInfo.getOrdernumber());
					String url = "ReqOrderInfo";
					String result = "";
					try{
						result = PayUtil.Invoke(url, appid, key, session, info);
						
						JSONObject json = JsonKit.parse(result);
						String ret = json.getString("ret");
						if("0".equals(ret)){
							JSONArray data =  json.getJSONArray("data");
							JSONObject order = data.getJSONObject(0);
							
							String ordernumber = order.getString("ordernumber"); // 商户订单号
							String amount = order.getString("amount"); // 交易金额
							String payorderid = order.getString("payorderid"); // 交易流水号
							String businesstime = order.getString("businesstime"); // 交易时间yyyy-MM-dd hh:mm:ss
							String respcode = order.getString("respcode"); // 交易状态:1-待支付 2-支付完成 3-已关闭 4-交易撤销
							String extraparams = order.getString("extraparams"); // 扩展内容 原样返回
							String respmsg = ""; // 状态说明
							
							payInfo.setRespcode(respcode);
							payInfo.setBusinesstime(businesstime);
							payInfo.setRespmsg(respmsg);
							payInfo.setPayorderid(payorderid);
							payInfo.setRespname(LfConstant.PAY_RESPCODE.get(respcode));
							Blade.create(PayInfo.class).update(payInfo);
							
							if("2".equals(respcode)){ // 支付完成
								boolean temp = memberService.addRecharge(payInfo.getUser_id(), MathKit.div(amount, "100", 2), false,payInfo.getOrdernumber());
								if(!temp){
									throw new RuntimeException("添加支付记录出错!");
								}
							}
						}else{
							System.out.println("result:"+result);
							throw new RuntimeException(json.getString("message"));
						}
						
					}catch(Exception e){
						System.out.println("result:"+result);
						throw new RuntimeException(e.toString());
					}
				}else{
					throw new RuntimeException("该支付订单已同步!");
				}
			}else{
				throw new RuntimeException("该支付通道不支持刷新功能!");
			}
		}else{
			throw new RuntimeException("未查询到该支付订单记录!");
		}
		
		
		return true;
	}

	@Override
	public boolean editStatus(Integer person_id) {
		// 获取订单信息
		
		Paras p = Paras.create().set("user_id",person_id);
		String today = DateKit.getDay();
		List<PayInfo> payInfos = Blade.create(PayInfo.class).findBy(" user_id=#{user_id} and create_time like '%"+today+"%' and pay_type=5 and respcode in(0,1)", p);
		
		for(PayInfo payInfo:payInfos){
			// 查询接口
			String appid = CommKit.getParameter("300").getPara();
			String key = CommKit.getParameter("301").getPara();
			String session = CommKit.getParameter("302").getPara();
			
			Map<String,Object> info = new HashMap<String,Object>();
			info.put("orderNumber",payInfo.getOrdernumber());
			String url = "ReqOrderInfo";
			String result = "";
			try{
				result = PayUtil.Invoke(url, appid, key, session, info);
				
				JSONObject json = JsonKit.parse(result);
				String ret = json.getString("ret");
				if("0".equals(ret)){
					JSONArray data =  json.getJSONArray("data");
					JSONObject order = data.getJSONObject(0);
					
					String ordernumber = order.getString("orderNumber"); // 商户订单号
					if(Func.isEmpty(ordernumber)){
						ordernumber = order.getString("ordernumber"); // 商户订单号
					}
					String amount = order.getString("amount"); // 交易金额
					String payorderid = order.getString("payorderid"); // 交易流水号
					String businesstime = order.getString("businesstime"); // 交易时间yyyy-MM-dd hh:mm:ss
					String respcode = order.getString("respcode"); // 交易状态:1-待支付 2-支付完成 3-已关闭 4-交易撤销
					String extraparams = order.getString("extraparams"); // 扩展内容 原样返回
					String respmsg = ""; // 状态说明
					
					payInfo.setRespcode(respcode);
					payInfo.setBusinesstime(businesstime);
					payInfo.setRespmsg(respmsg);
					payInfo.setPayorderid(payorderid);
					payInfo.setRespname(LfConstant.PAY_RESPCODE.get(respcode));
					Blade.create(PayInfo.class).update(payInfo);
					
					if("2".equals(respcode)){ // 支付完成
						boolean temp = memberService.addRecharge(payInfo.getUser_id(), MathKit.div(amount, "100", 2), false,ordernumber);
						if(!temp){
							throw new RuntimeException("添加支付记录出错!");
						}
					}
				}else{
					System.out.println("result:"+result);
				}
				
			}catch(Exception e){
				System.out.println("result:"+result);
				throw new RuntimeException(e.toString());
			}
		}
		
		return true;
	}
	
	@Override
	public boolean refalsePay() {
		// 获取订单信息
		
		Paras p = Paras.create();
		String today = DateKit.getDay();
		List<PayInfo> payInfos = Blade.create(PayInfo.class).findBy(" pay_type in ("+LfConstant.REFRESH_PAY_TYPE+") and create_time like '%"+today+"%' and respcode in(0,1)", p);
		
		for(PayInfo payInfo:payInfos){
			// 查询接口
			String appid = CommKit.getParameter("300").getPara();
			String key = CommKit.getParameter("301").getPara();
			String session = CommKit.getParameter("302").getPara();
			
			Map<String,Object> info = new HashMap<String,Object>();
			info.put("orderNumber",payInfo.getOrdernumber());
			String url = "ReqOrderInfo";
			String result = "";
			try{
				result = PayUtil.Invoke(url, appid, key, session, info);
				
				JSONObject json = JsonKit.parse(result);
				String ret = json.getString("ret");
				if("0".equals(ret)){
					JSONArray data =  json.getJSONArray("data");
					JSONObject order = data.getJSONObject(0);
					
					String ordernumber = order.getString("orderNumber"); // 商户订单号
					if(Func.isEmpty(ordernumber)){
						ordernumber = order.getString("ordernumber"); // 商户订单号
					}
					String amount = order.getString("amount"); // 交易金额
					String payorderid = order.getString("payorderid"); // 交易流水号
					String businesstime = order.getString("businesstime"); // 交易时间yyyy-MM-dd hh:mm:ss
					String respcode = order.getString("respcode"); // 交易状态:1-待支付 2-支付完成 3-已关闭 4-交易撤销
					String extraparams = order.getString("extraparams"); // 扩展内容 原样返回
					String respmsg = ""; // 状态说明
					
					payInfo.setRespcode(respcode);
					payInfo.setBusinesstime(businesstime);
					payInfo.setRespmsg(respmsg);
					payInfo.setPayorderid(payorderid);
					payInfo.setRespname(LfConstant.PAY_RESPCODE.get(respcode));
					Blade.create(PayInfo.class).update(payInfo);
					
					if("2".equals(respcode)){ // 支付完成
						boolean temp = memberService.addRecharge(payInfo.getUser_id(), MathKit.div(amount, "100", 2), false,ordernumber);
						if(!temp){
							throw new RuntimeException("添加支付记录出错!");
						}
					}
				}else{
					PayInfo pi = new PayInfo();
					pi.setId(payInfo.getId());
					pi.setRespcode("5");
					pi.setRespname("无效");
					Blade.create(PayInfo.class).update(pi);
				}
				
			}catch(Exception e){
				throw new RuntimeException(e.toString());
			}
		}
		
		Paras p1 = Paras.create();
		Blade.create(PayInfo.class).updateBy(" respcode=5,respname='无效' ", " TIMESTAMPDIFF(HOUR,create_time,now())>1 and respcode in(0,1) ", p1);
		
		return true;
	}
}
