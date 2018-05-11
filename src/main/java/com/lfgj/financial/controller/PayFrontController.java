package com.lfgj.financial.controller;

import com.lfgj.clinet.pay.payment.utils.MD5Util;
import com.lfgj.clinet.payHqf.exception.PayException;
import com.lfgj.clinet.payHqf.util.ConstantHqf;
import com.lfgj.clinet.payHqf.util.Md5Util;
import com.lfgj.clinet.payLida.PaymentForOnlineService;
import com.lfgj.clinet.paySZ.util.SzPayUtil;
import com.lfgj.clinet.payYida.Mobo360SignUtil;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.CommKit;
import com.rrgy.common.base.BaseController;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.toolbox.kit.HttpKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Generated by Blade.
 * 2017-09-18 21:29:22
 */
@Controller
@RequestMapping("/payfront")
public class PayFrontController extends BaseController {
	private static String BASE_PATH = "/payfront/";
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping("/result")
	public String index(ModelMap mm) {
		return BASE_PATH + "showresult.html";
	}
	
	/**
	 * 深圳
	 * @param mm
	 * @return
	 */
	@RequestMapping("/notifySz")
	public String notifySz(ModelMap mm){
		System.out.println("通道（深圳）支付异步通知:"+this.getParas());
		
		// 获取支付平台返回的数据
		String pay_order_id = this.getParameter("pay_order_id","");
		String pay_amount = this.getParameter("pay_amount","");
		String pay_remark = this.getParameter("pay_remark","");
		String pay_product_name = this.getParameter("pay_product_name","");
		String sign = this.getParameter("sign","");
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pay_order_id", pay_order_id);
		map.put("pay_amount", pay_amount);
		map.put("pay_remark", pay_remark);
		map.put("pay_product_name", pay_product_name);
		
		String key = CommKit.getParameter("311").getPara();
		String chesign = SzPayUtil.createSign(map, key);
		
		System.out.println("chesign:"+chesign);
		
		String result = "error";
		if(sign.equals(chesign)){
			boolean b = memberService.updatePayInfo(pay_order_id, pay_order_id, pay_amount, true);
			if(b){
				result = "ok";
			}
		}
		
		mm.put("result_info", result);
		return BASE_PATH + "notify_url.html";
	}
	
	/**
	 * 环球付
	 * @param mm
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws PayException
	 */
	@RequestMapping("/notifyHqf")
	public String notifyHqf(ModelMap mm) throws UnsupportedEncodingException, PayException{
		System.out.println("通道（环球付）支付异步通知:"+this.getParas());
		
		boolean shanNotify = md5VerifyShan(this.getRequest());
		String result = "fail";
		if(shanNotify){
			if("TRADE_SUCCESS".equals(this.getParameter("trade_status"))){
				//商户订单号
				String out_order_no = this.getParameter("out_order_no","");
				//环球汇交易号
				String trade_no = this.getParameter("trade_no","");
				//价格
				String total_fee = this.getParameter("total_fee","");
				
				boolean b = memberService.updatePayInfo(out_order_no, trade_no, total_fee, false);
				if(b){
					result = "ok";
				}
			}
		}
		
		mm.put("result_info", result);
		return BASE_PATH + "notify_url.html";
	}



    /**
     * 首捷支付回调
     * @param mm
     * @return
     * @throws UnsupportedEncodingException
     * @throws PayException
     */
    @RequestMapping("/notifyShouJie")
    public String notifyShouJie(ModelMap mm, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, PayException{
        log.info("通道（首捷支付）支付异步回调通知:"+this.getParas());

        String result = "fail";

        try {
            PrintWriter out = response.getWriter();

            String status =request.getParameter("status");
            String partner		=request.getParameter("partner");
            String ordernumber		=request.getParameter("ordernumber");
            String paymoney		=request.getParameter("paymoney");
            String paytype			=request.getParameter("paytype");
            String sdpayno			=request.getParameter("sdpayno");
            String remark			=request.getParameter("remark");
            String sign			=request.getParameter("sign");

            String userkey = ConstConfig.pool.get("pay.shoujie.key"); // 商家密钥
            String acceptParams = "partner="+partner+"&status="+status+"&sdpayno="+sdpayno+"&ordernumber="+ordernumber+"&paymoney="+paymoney+"&paytype="+paytype+"&"+userkey;
            log.info("accept params => " + acceptParams);
            String mysign = MD5Util.string2MD5(acceptParams);


            if (sign.equals(mysign)){

                if(status.equals("1")){

                    boolean b = memberService.updatePayInfo(ordernumber, sdpayno, paymoney, false);

                    out.print("success");

                }else {
                    out.print("fail");

                }
            }else {
                out.print("signerr");
            }

            return null;


        }catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new PayException("系统出现错误!");
        }

    }


    /**
     * 网逸支付回调
     * @param mm
     * @return
     * @throws UnsupportedEncodingException
     * @throws PayException
     */
    @RequestMapping("/notifyWish")
    public String notifyWish(ModelMap mm, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, PayException{
        log.info("通道（网逸支付）支付异步回调通知:"+this.getParas());
        String result = "fail";
        PrintWriter out = null;
        try {
            out = response.getWriter();

            String status =request.getParameter("status");
            String customerid		=request.getParameter("customerid");
            String sdpayno		=request.getParameter("sdpayno");  // 平台订单号
            String sdorderno		=request.getParameter("sdorderno");  // 商户订单号
            String total_fee			=request.getParameter("total_fee");
            String paytype			=request.getParameter("paytype");
            String remark			=request.getParameter("remark");
            String sign			=request.getParameter("sign");

            String userkey = ConstConfig.pool.get("pay.wish.key"); // 商家密钥
            String acceptParams = "customerid="+customerid+"&status="+status+"&sdpayno="+sdpayno+"&sdorderno="+sdorderno
                    +"&total_fee="+total_fee+"&paytype="+paytype+"&"+userkey;
            log.info("accept params => " + acceptParams);
            String mysign = MD5Util.string2MD5(acceptParams);


            if (sign.equals(mysign)){

                if(status.equals("1")){

                    boolean b = memberService.updatePayInfo(sdorderno, sdpayno, total_fee, false);

                    out.print("success");

                }else {
                    out.print("fail");
                    this.log.info("fail, status: " + status);
                }
            }else {
                out.print("signerr");
                this.log.info("signerr, sign: " + sign + ", mysign: " + mysign);
            }

            return null;


        }catch(Exception e) {
            log.error(e.getMessage(), e);
            out.print(" invalid request ");
            return null;
        }

    }



    /**
     * 码支付回调
     * @param mm
     * @return
     * @throws UnsupportedEncodingException
     * @throws PayException
     */
    @RequestMapping("/notifyMazhifu")
    public String notifyMazhifu(ModelMap mm, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, PayException{
        log.info("通道（码支付）支付异步回调通知:"+this.getParas());
        String result = "fail";
        try {

            PrintWriter out = response.getWriter();

            String key =  ConstConfig.pool.get("pay.mazhifu.key"); // 商家密钥
            Map<String,String> params = new HashMap<String,String>(); //申明hashMap变量储存接收到的参数名用于排序
            Map requestParams = request.getParameterMap(); //获取请求的全部参数
            String valueStr = ""; //申明字符变量 保存接收到的变量
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                valueStr = values[0];
                //乱码解决，这段代码在出现乱码时使用。如果sign不相等也可以使用这段代码转化
                //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                params.put(name, valueStr);//增加到params保存
            }
            List<String> keys = new ArrayList<String>(params.keySet()); //转为数组
            Collections.sort(keys); //重新排序
            String prestr = "";
            String sign= params.get("sign"); //获取接收到的sign 参数

            String third_order_pay_no = params.get("pay_no");
            String paymoney = params.get("money");
            String order_no = params.get("param");

            for (int i = 0; i < keys.size(); i++) { //遍历拼接url 拼接成a=1&b=2 进行MD5签名
                String key_name = keys.get(i);
                String value = params.get(key_name);
                if(value== null || value.equals("") ||key_name.equals("sign")){ //跳过这些 不签名
                    continue;
                }
                if (prestr.equals("")){
                    prestr =  key_name + "=" + value;
                }else{
                    prestr =  prestr +"&" + key_name + "=" + value;
                }
            }

           String mySign = MD5Util.string2MD5(prestr+key);
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update((prestr+key).getBytes());
//            String  mySign = new BigInteger(1, md.digest()).toString(16).toLowerCase();
//            if(mySign.length()!=32)mySign="0"+mySign;

            if(mySign.equals(sign)){
                //编码要匹配 编码不一致中文会导致加密结果不一致
                out.print("ok");
                boolean b = memberService.updatePayInfo(order_no, third_order_pay_no, paymoney, false);
            }else{
                //参数不合法
                out.print("fail");
            }


            return null;


        }catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new PayException("系统出现错误!");
        }

    }


    /**
     * 立达付
     * @param mm
     * @return
     * @throws UnsupportedEncodingException
     * @throws PayException
     */
    @RequestMapping("/notifyLdf")
    public String notifyLdf(ModelMap mm, HttpServletResponse response) throws UnsupportedEncodingException, PayException{
        log.info("通道（立达付）支付异步回调通知:"+this.getParas());

        String result = "fail";
        try {
            PrintWriter out = response.getWriter();

            String keyValue = ConstConfig.pool.get("pay.lida.secret");    // 商家密钥
            String r0_Cmd = this.getParameter("r0_Cmd"); // 业务类型
            String p1_MerId = ConstConfig.pool.get("pay.lida.key");   // 商户编号
            String r1_Code = this.getParameter("r1_Code");// 支付结果
            String r2_TrxId = this.getParameter("r2_TrxId");// API支付交易流水号
            String r3_Amt = this.getParameter("r3_Amt");// 支付金额
            String r4_Cur = this.getParameter("r4_Cur");// 交易币种
            String r5_Pid = new String(this.getParameter("r5_Pid").getBytes("iso-8859-1"), "gbk");// 商品名称
            String r6_Order = this.getParameter("r6_Order");// 商户订单号
            String r7_Uid = this.getParameter("r7_Uid");// API支付会员ID
            String r8_MP = new String(this.getParameter("r8_MP").getBytes("iso-8859-1"), "gbk");// 商户扩展信息
            String r9_BType = this.getParameter("r9_BType");// 交易结果返回类型
            String hmac = this.getParameter("hmac");// 签名数据
            boolean isOK = false;
            // 校验返回数据包
            isOK = PaymentForOnlineService.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code,
                    r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);
            if (isOK) {
                //商户订单号
                String out_order_no = r6_Order;
                //环球汇交易号
                String trade_no = r2_TrxId;
                //价格
                String total_fee = r3_Amt;

                //在接收到支付结果通知后，判断是否进行过业务逻辑处理，不要重复进行业务逻辑处理
                if (r1_Code.equals("1")) {
                    // 产品通用接口支付成功返回-浏览器重定向
                    if (r9_BType.equals("1")) {
                        result = "支付结果: 支付成功!";
                        boolean b = memberService.updatePayInfo(out_order_no, trade_no, total_fee, false);
                        // 产品通用接口支付成功返回-服务器点对点通讯
                    } else if (r9_BType.equals("2")) {
                        // 如果在发起交易请求时	设置使用应答机制时，必须应答以"success"开头的字符串，大小写不敏感
                        boolean b = memberService.updatePayInfo(out_order_no, trade_no, total_fee, false);
                        out.println("SUCCESS");
                        return null;
                        // 产品通用接口支付成功返回-电话支付返回
                    }
                }
            } else {
                result = "交易签名被篡改!";
            }

        }catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new PayException("系统出现错误!");
        }

        mm.put("result_info", result);

        return BASE_PATH + "notify_url.html";
    }


    /**
     * 易快支付回调
     * @param mm
     * @return
     * @throws UnsupportedEncodingException
     * @throws PayException
     */
    @RequestMapping("/notifyYiKuai")
    public String notifyYiKuai(ModelMap mm, HttpServletRequest request,  HttpServletResponse response) throws UnsupportedEncodingException, PayException{
        log.info("通道（易快）支付异步回调通知:"+this.getParas());

        String result = "fail";
        try {
            PrintWriter out = response.getWriter();

            String keyValue = ConstConfig.pool.get("pay.yikuai.key");    // 商家密钥

            String mch_id = request.getParameter("mch_id"); // 分配的商户号
            String out_trade_no = request.getParameter("out_trade_no");   // 提交的订单号
            String total_fee = request.getParameter("total_fee");// 支付结果
            String trade_state = request.getParameter("trade_state"); // 交易状态
            String sign = request.getParameter("sign");// 签名数据
            String checkSign ="mch_id="+mch_id+"&out_trade_no="+out_trade_no+"&total_fee="+total_fee+"&trade_state="+trade_state+"&key="+keyValue;
            log.info("the checkSign info: " + checkSign);
            checkSign = MD5Util.string2MD5(checkSign).toUpperCase();
            if (checkSign.equals(sign)) {

                if (trade_state.equals("2")) {
                    boolean b = memberService.updatePayInfo(out_trade_no, out_trade_no, total_fee, false);
                    out.println("SUCCESS");
                    return null;
                }
                this.log.info("the trade state: " + trade_state);
            } else {
                result = "交易签名被篡改!checkSign: " + checkSign + ", sign: " + sign;
                this.log.info(result);
            }

        }catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new PayException("系统出现错误!");
        }

        mm.put("result_info", result);

        return BASE_PATH + "notify_url.html";
    }




    /**
     * 易达付
     * @param mm
     * @return
     * @throws UnsupportedEncodingException
     * @throws PayException
     */
    @RequestMapping("/notifyYdf")
    public String notifyYdf(ModelMap mm, HttpServletResponse response) throws UnsupportedEncodingException, PayException{
        log.info("通道（易达付）支付异步回调通知:"+this.getParas());

        String result = "fail";
        try {
            PrintWriter out = response.getWriter();

            String apiName = this.getParameter("apiName");
            String notifyTime = this.getParameter("notifyTime");
            String tradeAmt = this.getParameter("tradeAmt");
            String merchNo = this.getParameter("merchNo");
            String merchParam = this.getParameter("merchParam");
            String orderNo = this.getParameter("orderNo");
            String tradeDate = this.getParameter("tradeDate");
            String accNo = this.getParameter("accNo");
            String accDate = this.getParameter("accDate");
            String orderStatus = this.getParameter("orderStatus");
            String signMsg = this.getParameter("signMsg");

            String srcMsg = String
                    .format(
                            "apiName=%s&notifyTime=%s&tradeAmt=%s&merchNo=%s&merchParam=%s&orderNo=%s&tradeDate=%s&accNo=%s&accDate=%s&orderStatus=%s",
                            apiName, notifyTime, tradeAmt, merchNo,
                            merchParam, orderNo, tradeDate, accNo, accDate,
                            orderStatus);

            // 验证签名
            boolean verifyRst = Mobo360SignUtil.verifyData(signMsg, srcMsg);
            if (verifyRst) {
                //商户订单号
                String out_order_no = orderNo;
                //支付平台订单号
                String trade_no = accNo;
                //价格
                String total_fee = tradeAmt;

                        result = "支付结果: 支付成功!";
                        boolean b = memberService.updatePayInfo(out_order_no, trade_no, total_fee, false);
                        out.println("SUCCESS");
                        return null;
            } else {
                result = "交易签名被篡改!";
            }

        }catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new PayException("系统出现错误!");
        }

        mm.put("result_info", result);

        return BASE_PATH + "notify_url.html";
    }

	/**
	 * 环球付验签方法
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws PayException
	 */
	private boolean md5VerifyShan(HttpServletRequest request) throws UnsupportedEncodingException, PayException{
		
		request.setCharacterEncoding("UTF-8");
		String out_order_no = request.getParameter("out_order_no");
		if("".equals(out_order_no)||out_order_no==null){
			throw new PayException("out_order_no不能为空");
		}
		String total_fee = request.getParameter("total_fee");
		if("".equals(total_fee)||total_fee==null){
			throw new PayException("total_fee不能为空");
		}
		String trade_status  = request.getParameter("trade_status");
		if("".equals(total_fee)||total_fee==null){
			throw new PayException("trade_status不能为空");
		}
		String sign = request.getParameter("sign");
		if("".equals(total_fee)||total_fee==null){
			throw new PayException("sign不能为空");
		}
		String key = ConstantHqf.KEY;
		String pid = ConstantHqf.PARTNER;
		Md5Util md5Util = new Md5Util();
		String signMd5 = md5Util.encode(out_order_no+total_fee+trade_status+pid+key, null);
		if(signMd5.equals(sign)){
			return true;
		}
		return false;
	}
	
}