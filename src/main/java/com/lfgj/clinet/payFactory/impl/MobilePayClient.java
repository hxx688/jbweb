package com.lfgj.clinet.payFactory.impl;

import com.lfgj.clinet.payFactory.IMobilePayClient;
import com.lfgj.clinet.payFactory.IPayFactory;
import com.lfgj.util.ConverterUtil;
import com.lfgj.util.PayTypeEnum;
import com.rrgy.common.iface.RequestMethod;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付通道：集中统一通道
 * @author Administrator
 *
 */

@Service
@Client(name = "lf_prepay_factory")
public class MobilePayClient extends RequestMethod implements IMobilePayClient {

    @Autowired
    private IPayFactory payFactory;

    public ResultVo getPayUrl() {
        ResultVo rv = new ResultVo();
        try{
            String payChannel = getParams("payChannel", "1");
            log.info("移动端支付请求, payChannel："+payChannel);
            PayTypeEnum payTypeEnum = ConverterUtil.cvtPayTypeEnumByChannel(payChannel);
            rv = payFactory.generatePayService(payTypeEnum).url(request);

        }catch(Exception e){
            log.info("移动端支付请求, happen exception："+e.getMessage(), e);
            rv.setReturnCode("1");
            rv.setReturnMsg(e.toString());
        }
        return rv;

    }

}
