package com.lfgj.clinet.payYida;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.payFactory.IPayService;
import com.lfgj.clinet.payHqf.exception.PayException;
import com.lfgj.clinet.payLida.PaymentForOnlineService;
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
 * 支付通道：易达付
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_yida")
public class PrePayYidaClient extends RequestMethod implements IPayService{

	@Autowired
	MemberService service;
	
	public ResultVo url() {
		ResultVo rv = new ResultVo();
		String person_id = getParams("id","");	
		String pay_type = "9"; // 易达付
		String money = getParams("money","0");
        String payModel = getParams("payModel", "yida_bank");
		
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
            String payUrl = ConstConfig.pool.get("pay.yida.url");
			requestParams.put("pay_url", payUrl);
//			String domain = ConstConfig.pool.get("config.domain");
//			int i = domain.indexOf("//");
//			if(i >= 0){
//				domain = domain.substring(i+2, domain.length());
//			}
//			requestParams.put("http_referer", domain); // 这个域名，要和支付通道后台绑定的域名一样，不加http://
	        rv.setReturnCode("0");
			rv.setReturnParams(requestParams);
			log.info("易达支付请求参数："+requestParams);
			return rv;
		}catch(Exception e){
			e.printStackTrace();
			rv.setReturnCode("1");
			rv.setReturnMsg(e.toString());
		}
		return rv;
	}
	
	private Map<String,Object> getParameter(String money, String orderNo, String... extendStrs) throws PayException{

        String payUrl = ConstConfig.pool.get("pay.yida.url");
        String platformID = ConstConfig.pool.get("pay.yida.account");
        String sceret = ConstConfig.pool.get("pay.yida.sceret");
        String callBackUrl = ConstConfig.pool.get("config.domain")
                + "/payfront/notifyYdf";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("apiName", Mobo360Config.MOBAOPAY_APINAME_PAY);
        paramsMap.put("apiVersion", Mobo360Config.MOBAOPAY_API_VERSION);
        paramsMap.put("platformID", platformID);
        paramsMap.put("merchNo", platformID);
        paramsMap.put("orderNo", orderNo);
        paramsMap.put("tradeDate", sdf.format(new Date()));
        paramsMap.put("amt", money);
        paramsMap.put("merchUrl", callBackUrl);
        paramsMap.put("merchParam", "abcd");
        paramsMap.put("tradeSummary", "pay");
        /**
         * bankCode为空，提交表单后浏览器在新窗口显示支付系统收银台页面，在这里可以通过账户余额支付或者选择银行支付；
         * bankCode不为空，取值只能是接口文档中列举的银行代码，提交表单后浏览器将在新窗口直接打开选中银行的支付页面。
         * 无论选择上面两种方式中的哪一种，支付成功后收到的通知都是同一接口。
         **/
        String choosePayType = "1"; // 默认网银
        if (extendStrs != null && extendStrs.length > 0) {
            if(null != extendStrs[0] && !"".equals(extendStrs[0].trim())) {
                if("yida_easy".equals(extendStrs[0])) {
                    choosePayType = "12";
                }
            }
        }
        paramsMap.put("bankCode", "null");
        paramsMap.put("choosePayType", choosePayType);

        String paramsStr = null;	// 签名源数据
        String signMsg = null;	// 签名数据
        try {
            paramsStr = Mobo360Merchant.generatePayRequest(paramsMap);
            signMsg = Mobo360SignUtil.signData(paramsStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PayException("签名异常!");
        }
        paramsMap.put("signMsg", signMsg);
        log.info("(网关支付  签名后数据) => "+paramsMap);


        Map<String,Object> parameter = new HashMap<String,Object>();

        for(String key : paramsMap.keySet()) {
            parameter.put(key, paramsMap.get(key));
        }


		return parameter;
	}
	
}
