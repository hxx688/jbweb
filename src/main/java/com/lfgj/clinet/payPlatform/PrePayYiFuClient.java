package com.lfgj.clinet.payPlatform;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.clinet.payFactory.IPayService;
import com.lfgj.clinet.payHqf.exception.PayException;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.*;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.constant.ConstConfig;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Func;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支付通道：易付
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_yifu")
public class PrePayYiFuClient extends RequestMethod implements IPayService{

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
        payInfo.setPay_type(PayTypeEnum.YIFU_PAY.getPayCode());
        payInfo.setPay_type_name(PayTypeEnum.YIFU_PAY.getPayName());
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
            String payUrl = ConstConfig.pool.get("pay.yifu.url");
            requestParams.put("pay_url", payUrl);
//            requestParams.put("pageMethod", "get");
            rv.setReturnCode("0");
            rv.setReturnParams(requestParams);
            log.info("易付支付请求参数："+requestParams);
            return rv;
        }catch(Exception e){
            e.printStackTrace();
            rv.setReturnCode("1");
            rv.setReturnMsg(e.toString());
        }
        return rv;
    }

    private Map<String,Object> getParameter(String money, String orderNo, String... extendStrs) throws PayException{
        log.info("Process in PrePayYiFuClient.getParameter method. ");

        Map<String,Object> params = new LinkedHashMap<>();
        String account = ConstConfig.pool.get("pay.yifu.account"); // 商家账号
        String publicKey = ConstConfig.pool.get("pay.yifu.public"); // 商家公钥
        String privateKey = ConstConfig.pool.get("pay.yifu.private"); // 商家私钥


        JSONObject businessHead = new JSONObject();

        businessHead.put("merchantNumber", account);
        businessHead.put("version", "V1.1.0");
        businessHead.put("requestTime", DateUtils.formatyyyyMMddHHmmss(new Date()));

        JSONObject businessContext = new JSONObject();
        businessContext.put("orderNumber", orderNo);//商户订单号
        businessContext.put("payType", "QUICK_SAVINGS");
        Long dbMoney = new BigDecimal(money).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).longValue();
        businessContext.put("amount", dbMoney);//金额 单位 分
        businessContext.put("currency", "CNY");
        businessContext.put("orderCreateIp", "127.0.0.1");
        businessContext.put("vaildTime", "1800");
        businessContext.put("commodityName", "jubao_product");
        businessContext.put("commodityDesc", "jubao_desc");
        businessContext.put("commodityRemark", "jubao_remark");
        String notify_url=ConstConfig.pool.get("config.domain")
                + "/payfront/notifyYiFu"; // 异步通知URL
        businessContext.put("notifyUrl", notify_url);

        System.out.println(System.getProperty("file.encoding"));

        //方法二：中文操作系统中打印GBK
        System.out.println(Charset.defaultCharset());

        String context = null;
        log.info("businessHead: " + businessHead.toString());
        log.info("businessContext: " + businessContext.toString());
        try {
            businessHead = JSONObject.fromObject("{\"merchantNumber\":\"PAY000209000214\",\"version\":\"V1.1.0\",\"requestTime\":\"20180515223316\"}");
            businessContext = JSONObject.fromObject("{\"orderNumber\":\"P18051522331609680\",\"payType\":\"QUICK_SAVINGS\",\"amount\":100,\"currency\":\"CNY\",\"orderCreateIp\":\"127.0.0.1\",\"vaildTime\":\"1800\",\"commodityName\":\"jubao_product\",\"commodityDesc\":\"jubao_desc\",\"commodityRemark\":\"jubao_remark\",\"notifyUrl\":\"http://mobile.octtest.mopon.cn:81/payfront/notifyYiFu\"}");
            context = RSAUtils.verifyAndEncryptionToString(businessContext, businessHead, privateKey, publicKey);
            JSONObject jsonResult = HttpClients.doGet(ConstConfig.pool.get("pay.yifu.url"), context);
            log.info("[Get后结果jsonResult] : {}", jsonResult);

            params.put("context", URLEncoder.encode(context, "UTF-8"));
        } catch (Exception e) {
            log.error("签名加密错误!" + e.getMessage(), e);
            throw new PayException ("签名加密错误!");
        }


        return params;
    }

}
