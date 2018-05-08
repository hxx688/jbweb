package com.lfgj.clinet.payWish;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.pay.payment.utils.MD5Util;
import com.lfgj.clinet.payFactory.IPayService;
import com.lfgj.clinet.payHqf.exception.PayException;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.LfConstant;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支付通道：网逸支付
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_wish")
public class PrePayWishClient extends RequestMethod implements IPayService{

    @Autowired
    MemberService service;

    public ResultVo url() {
        ResultVo rv = new ResultVo();
        String person_id = getParams("id","");
        String pay_type = "12";
        String money = getParams("money","0");
        String payModel = "quickbank";
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
            String payModel = "quickbank";
            Map<String,Object> requestParams = getParameter(money, orderNo, payModel);
            String payUrl = ConstConfig.pool.get("pay.wish.url");
            requestParams.put("pay_url", payUrl);
            rv.setReturnCode("0");
            rv.setReturnParams(requestParams);
            log.info("网逸支付请求参数："+requestParams);
            return rv;
        }catch(Exception e){
            e.printStackTrace();
            rv.setReturnCode("1");
            rv.setReturnMsg(e.toString());
        }
        return rv;
    }

    private Map<String,Object> getParameter(String money, String orderNo, String... extendStrs) throws PayException{

        Map<String,Object> params = new LinkedHashMap<>();
        String account = ConstConfig.pool.get("pay.wish.account"); // 商家账号
        String key = ConstConfig.pool.get("pay.wish.key"); // 商家密钥

        params.put("version", "1.0");
        params.put("customerid", account);
        params.put("sdorderno", orderNo);
        Double dbMoney = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String paymoney = String.format("%.2f", dbMoney); //  String.valueOf(); // 支付金额
        params.put("total_fee", paymoney);

        String payType = extendStrs[0]; //支付类型  alipay：支付宝 tenpay：财付通 weixin：微信扫码 quickbank:快捷支付
        params.put("payType", payType);
        params.put("bankcode", "");

        String notify_url=ConstConfig.pool.get("config.domain")
                + "/payfront/notifyWish"; // 异步通知URL
        params.put("notifyurl", notify_url );

        params.put("returnurl", "" );

        StringBuffer sb = new StringBuffer("version=").append(params.get("version"))
                .append("customer-id=").append(params.get("customerid"))
                .append("total_fee=").append(params.get("total_fee"))
                .append("sdorderno=").append(params.get("sdorderno"))
                .append("notifyurl=").append(params.get("notifyurl"))
                .append("returnurl=").append(params.get("returnurl"))
                .append("&").append(key)
                ;

        params.put("sign", MD5Util.string2MD5(sb.toString()));

        return params;
    }

}
