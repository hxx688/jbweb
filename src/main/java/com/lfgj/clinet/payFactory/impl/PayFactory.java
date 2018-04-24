package com.lfgj.clinet.payFactory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.clinet.payFactory.IPayFactory;
import com.lfgj.clinet.payFactory.IPayService;

/**
 * <p>Description: </p>
 * @date 2018/3/27
 * @author 贺锟 
 * @version 1.0
 * <p>Company:Mopon</p>
 * <p>Copyright:Copyright(c)2017</p>
 */
@Service
public class PayFactory implements IPayFactory {

    @Autowired
    private IPayService prePayHqfClient;

    @Autowired
    private IPayService prePayLidaClient;

    @Autowired
    private IPayService prePayYidaClient;

    @Autowired
    private IPayService prePayShouJieClient;

    @Autowired
    private IPayService prePayMazhifuClient;

    @Override
    public IPayService generatePayService(String payType) throws Exception{
//        put("0", "银联快捷支付");
//        put("1", "银联在线支付");
//        put("5", "银联网关支付");
//        put("6", "环球付");
//        put("7", "环球付网关支付");
//        put("8", "立达支付");
        if("7".equals(payType)){
            return prePayHqfClient;
        }else if("8".equals(payType)){
            return prePayLidaClient;
        }else if("9".equals(payType)) {
            return prePayYidaClient;
        }else if("10".equals(payType)){
            return prePayShouJieClient;
        }else if("11".equals(payType)){
            return prePayMazhifuClient;
        }else{
            throw new Exception("支付渠道不存在!");
        }
    }


}
