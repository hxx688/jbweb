package com.lfgj.clinet.payLida;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.payFactory.IPayService;
import com.lfgj.clinet.payHqf.exception.PayException;
import com.lfgj.clinet.payHqf.util.ConstantHqf;
import com.lfgj.clinet.payHqf.util.ShanPayUtil;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;

/**
 * 支付通道：立达付
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_lida")
public class PrePayLidaClient extends RequestMethod implements IPayService{

	@Autowired
	MemberService service;
	
	public ResultVo url() {
		ResultVo rv = new ResultVo();
		String person_id = getParams("id","");	
		String pay_type = "8"; // 环球付
		String money = getParams("money","0");
        String payModel = getParams("payModel", "wxcode");
		
		Member person = Blade.create(Member.class).findById(person_id);
		
		if(person == null){
			rv.setReturnCode("1");
			rv.setReturnMsg("查询不到用户信息！");
			return rv;
		}
		
		if(Float.valueOf(money) <= 0){
			rv.setReturnCode("1");
			rv.setReturnMsg("金额应大于0！");
			return rv;
		}
		
		String orderNo = Func.orderNo("P");
		
		PayInfo payInfo = new PayInfo();
		payInfo.setAmount(money);
		payInfo.setOrdernumber(orderNo);
		payInfo.setCreate_time(new Date());
		payInfo.setUser_id(Integer.valueOf(person_id));
		payInfo.setReal_name(person.getReal_name());
		payInfo.setMobile(person.getMobile());
		payInfo.setPay_acount(person.getBank_acount());
		payInfo.setPay_type(pay_type);
		payInfo.setPay_type_name(LfConstant.PAY_TYPE.get(pay_type));
		payInfo.setRespcode("0"); // 未提交
		payInfo.setRespname(LfConstant.PAY_RESPCODE.get("0"));
		
		boolean rs = Blade.create(PayInfo.class).save(payInfo);
		if(!rs){
			rv.setReturnCode("1");
			rv.setReturnMsg("保存支付订单出错!");
			return rv;
		}
		
		return getPayUrl(money, orderNo, payModel);
	}
	
	@Override
    public ResultVo getPayUrl(String money, String orderNo, String... extendStrs){
		ResultVo rv = new ResultVo();
		try{
			Map<String,Object> requestParams = getParameter(money, orderNo, extendStrs);
            String payUrl = ConstConfig.pool.get("pay.lida.url") + "/GateWay/ReceiveBank.aspx";
			requestParams.put("pay_url", payUrl);
//			String domain = ConstConfig.pool.get("config.domain");
//			int i = domain.indexOf("//");
//			if(i >= 0){
//				domain = domain.substring(i+2, domain.length());
//			}
//			requestParams.put("http_referer", domain); // 这个域名，要和支付通道后台绑定的域名一样，不加http://
	        rv.setReturnCode("0");
			rv.setReturnParams(requestParams);
			log.info("立达支付请求参数："+requestParams);
			return rv;
		}catch(Exception e){
			e.printStackTrace();
			rv.setReturnCode("1");
			rv.setReturnMsg(e.toString());
		}
		return rv;
	}
	
	private Map<String,Object> getParameter(String money, String orderNo, String... extendStrs) throws PayException{

        Map<String,Object> parameter = new HashMap<String,Object>();
		String keyValue = ConstConfig.pool.get("pay.lida.secret"); // 商家密钥
		// 商家设置用户购买商品的支付信息

		String p0_Cmd = "Buy"; // 在线支付请求，固定值 ”Buy”
		String p1_MerId = ConstConfig.pool.get("pay.lida.key"); // 商户编号
		String p2_Order = orderNo; // 商户订单号
		String p3_Amt = money; // 支付金额
		String p4_Cur = "CNY"; // 交易币种
		String p5_Pid = "productname"; // 商品名称
		String p6_Pcat = "producttype"; // 商品种类
		String p7_Pdesc = "productdesc"; // 商品描述
		String p8_Url = ConstConfig.pool.get("config.domain")
						+ "/payfront/notifyLdf"; // 商户接收支付成功数据的地址
		String p9_SAF = "0"; // 需要填写送货信息 0：不需要  1:需要
		String pa_MP = "6252"; // 商户扩展信息
		String pd_FrpId = "wxcode";
		if (extendStrs != null && extendStrs.length > 0) {
		    if(null != extendStrs[0] && !"".equals(extendStrs[0].trim())) {
                pd_FrpId = extendStrs[0];
            }
		}
		String pr_NeedResponse = "1"; // 默认为"1"，需要应答机制

		// 获得MD5-HMAC签名
		String hmac = PaymentForOnlineService.getReqMd5HmacForOnlinePayment(
			p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat,
			p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse,
			keyValue);


        parameter.put("p0_Cmd", p0_Cmd);
		parameter.put("p1_MerId", p1_MerId);
		parameter.put("p2_Order", p2_Order);
		parameter.put("p3_Amt", p3_Amt);
		parameter.put("p4_Cur", p4_Cur);
		parameter.put("p5_Pid", p5_Pid);
        parameter.put("p6_Pcat", p6_Pcat);
        parameter.put("p7_Pdesc", p7_Pdesc);
        parameter.put("p8_Url", p8_Url);
        parameter.put("p9_SAF", p9_SAF);
        parameter.put("pa_MP", pa_MP);
        parameter.put("pd_FrpId", pd_FrpId);
        parameter.put("pr_NeedResponse", pr_NeedResponse);
        parameter.put("hmac", hmac);



		return parameter;
	}
	
}
