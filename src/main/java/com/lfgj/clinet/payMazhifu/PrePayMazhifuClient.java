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
 * 支付通道：立达付
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
        String pay_type = "10";
        String money = getParams("money","0");
        String payModel = getParams("payModel", "kuaijie");
        String payCode = getParams("payCode", "ICBC");

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

        return getPayUrl(money, orderNo, payModel, payCode);
    }

    @Override
    public ResultVo getPayUrl(String money, String orderNo, String... extendStrs){
        ResultVo rv = new ResultVo();
        try{
            Map<String,Object> requestParams = getParameter(money, orderNo, extendStrs);
            String payUrl = ConstConfig.pool.get("pay.shoujie.url") + "/GateWay/ReceiveBank.aspx";
            requestParams.put("pay_url", payUrl);
            rv.setReturnCode("0");
            rv.setReturnParams(requestParams);
            log.info("首捷支付请求参数："+requestParams);
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
        String token = ConstConfig.pool.get("pay.mazhifu.key"); // 商家密钥
        String codepay_id = ConstConfig.pool.get("pay.mazhifu.account"); // 商家账号

        String price= money; //表单提交的价格
        String type=request.getParameter("type"); //支付类型  1：支付宝 2：QQ钱包 3：微信
        String pay_id=orderNo; //支付人的唯一标识
        String param= null; //自定义一些参数 支付后返回

        String notify_url=ConstConfig.pool.get("config.domain")
                + "/payfront/notifyMazhifu"; // 异步通知URL
        String return_url=notify_url;//支付后同步跳转地址





        String remark = ""; // 订单备注说明
        String getCode = "0"; // 微信选项, 0:默认, 1: 仅获取二维码
        StringBuffer sb = new StringBuffer();
        sb.append("Ver=").append(ver).append("&partner=").append(partner).append("&paymoney=")
                .append(paymoney).append("&ordernumber=").append(ordernumber).append("&notifyurl=").append(notifyurl)
                .append("&returnurl=").append(returnurl).append("&").append(userkey);
        System.out.println(sb.toString());
        String sign = MD5Util
                .string2MD5(sb.toString());

        System.out.println(sign);
        parameter.put("ver", ver);
        parameter.put("partner", partner);
        parameter.put("ordernumber", ordernumber);
        parameter.put("paymoney", paymoney);
        parameter.put("paytype", paytype);
        parameter.put("notifyurl", notifyurl);
        parameter.put("returnurl", returnurl);
        parameter.put("remark", remark);
        parameter.put("bankcode", bankcode);
        parameter.put("sign", sign);



        return parameter;
    }

}
