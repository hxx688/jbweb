package com.lfgj.clinet.payGate.payBaoBao;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.pay.payment.utils.MD5Util;
import com.lfgj.clinet.payFactory.IPayService;
import com.lfgj.clinet.payHqf.exception.PayException;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.LfConstant;
import com.lfgj.util.PayTypeEnum;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支付通道：宝宝支付
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_baobao")
public class PrePayBaoBaoClient extends RequestMethod implements IPayService{

    @Autowired
    MemberService service;

    public ResultVo url(HttpServletRequest request) {
        ResultVo rv = new ResultVo();
        String person_id = request.getParameter("id");
        String money = request.getParameter("money");
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
        payInfo.setPay_type(PayTypeEnum.BAOBAO_PAY.getPayCode());
        payInfo.setPay_type_name(PayTypeEnum.BAOBAO_PAY.getPayName());
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
            String payUrl = ConstConfig.pool.get("pay.baobao.url");
            requestParams.put("pay_url", payUrl);
            rv.setReturnCode("0");
            rv.setReturnParams(requestParams);
            log.info("宝宝支付请求参数："+requestParams);
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
        String account = ConstConfig.pool.get("pay.baobao.account"); // 商家账号
        String key = ConstConfig.pool.get("pay.baobao.key"); // 商家密钥
        String pid = ConstConfig.pool.get("pay.baobao.pid"); // 商家PID

        params.put("body", "jubao");
        String notify_url=ConstConfig.pool.get("config.domain")
                + "/payfront/notifyBaoBao"; // 异步通知URL
        params.put("notify_url", notify_url);
        params.put("out_order_no", orderNo);
        params.put("partner", pid);
        params.put("return_url", notify_url);
        params.put("subject", "jubao_subject");
        Double dbMoney = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String paymoney = String.format("%.2f", dbMoney); //  String.valueOf(); // 支付金额
        params.put("total_fee", paymoney);
        params.put("user_seller", account);

        StringBuffer sb = new StringBuffer();
        for(String paramKey: params.keySet()) {
            sb.append(paramKey).append("=").append(params.get(paramKey)).append("&");
        }
        String signContent = sb.toString().replaceAll("&$", "") + key;
        log.info("baobao pay signContent: " + signContent);
        params.put("sign", MD5Util.string2MD5(signContent));
        params.put("http_referer", ConstConfig.pool.get("config.domain"));
        return params;
    }



}
