package com.lfgj.clinet.payYiKuai;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.pay.payment.utils.MD5Util;
import com.lfgj.clinet.payFactory.IPayService;
import com.lfgj.clinet.payHqf.exception.PayException;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.DateUtils;
import com.lfgj.util.LfConstant;
import com.lfgj.util.PayTypeEnum;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;

/**
 * 支付通道：易快支付
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_yikuai")
public class PrePayYiKuaiClient extends RequestMethod implements IPayService{

    @Autowired
    MemberService service;

    public ResultVo url() {
        ResultVo rv = new ResultVo();
        String person_id = getParams("id","");
        String pay_type = "13";
        String money = getParams("money","0");
        String payModel = "WXZF";
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
        payInfo.setPay_type_name(PayTypeEnum.parseByCode(pay_type).getPayName());
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
            String payModel = "WXZF";
            Map<String,Object> requestParams = getParameter(money, orderNo, payModel);
            String payUrl = ConstConfig.pool.get("pay.yikuai.url");
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
        String account = ConstConfig.pool.get("pay.yikuai.account"); // 商家账号
        String key = ConstConfig.pool.get("pay.yikuai.key"); // 商家密钥

        params.put("pay_memberid", account);
        params.put("pay_orderid", orderNo);
        Double dbMoney = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String paymoney = String.format("%.2f", dbMoney); //  String.valueOf(); // 支付金额
        params.put("pay_amount", paymoney);

        params.put("pay_applydate", DateUtils.formatDefault(new Date()));
        params.put("pay_bankcode", "WXZF");
        String notify_url=ConstConfig.pool.get("config.domain")
                + "/payfront/notifyYiKuai"; // 异步通知URL
        params.put("pay_notifyurl", notify_url);
        params.put("pay_callbackurl", notify_url);


        StringBuffer sb = new StringBuffer("pay_amount=").append(params.get("pay_amount"))
                .append("&pay_applydate=").append(params.get("pay_applydate"))
                .append("&pay_bankcode=").append(params.get("pay_bankcode"))
                .append("&pay_callbackurl=").append(params.get("pay_callbackurl"))
                .append("&pay_memberid=").append(params.get("pay_memberid"))
                .append("&pay_notifyurl=").append(params.get("pay_notifyurl"))
                .append("&pay_orderid=").append(params.get("pay_orderid"))
                .append("&key=").append(key)
                ;

        params.put("sign", MD5Util.string2MD5(sb.toString()).toUpperCase());

        return params;
    }

}
