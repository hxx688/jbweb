package com.lfgj.clinet.payMazhifu;

import com.lfgj.member.model.Member;
import com.rrgy.common.iface.RequestMethod;

import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

/**
 * 支付通道：码支付
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_mazhifu")
public class PrePayMazhifuClient extends RequestMethod implements IPayService{

    @Autowired
    MemberService service;

    public ResultVo url() {
        ResultVo rv = new ResultVo();
        String person_id = getParams("id","");
        String pay_type = "11";
        String money = getParams("money","0");
        String payModel = getParams("payModel", "1");

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
            String payUrl = ConstConfig.pool.get("pay.mazhifu.url");
            requestParams.put("pay_url", payUrl);
            rv.setReturnCode("0");
            rv.setReturnParams(requestParams);
            log.info("码支付请求参数："+requestParams);
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
        String key = ConstConfig.pool.get("pay.mazhifu.key"); // 商家密钥
        String codepay_id = ConstConfig.pool.get("pay.mazhifu.account"); // 商家账号

        String price= money; //表单提交的价格
        String type = extendStrs[0]; //支付类型  1：支付宝 2：QQ钱包 3：微信
        String pay_id= orderNo; //支付人的唯一标识
        String param= orderNo; //自定义一些参数 支付后返回

        String notify_url=ConstConfig.pool.get("config.domain")
                + "/payfront/notifyMazhifu"; // 异步通知URL
        String return_url=notify_url;//支付后同步跳转地址

        StringBuffer url =  new StringBuffer("id=").append(codepay_id)
                .append("&notify_url=").append(notify_url)
                .append("&param=").append(param)
                .append("&pay_id=").append(pay_id)
                .append("&price=").append(price)
                .append("&return_url=").append(return_url)
                .append("&type=").append(type)
                .append(key);

        parameter.put("id", codepay_id);
        parameter.put("notify_url", notify_url);
        parameter.put("param", param);
        parameter.put("pay_id", pay_id);
        parameter.put("price", price);
        parameter.put("return_url", return_url);
        parameter.put("type", type);
        parameter.put("sign", MD5Util.string2MD5(url.toString()));

        return parameter;
    }

}
