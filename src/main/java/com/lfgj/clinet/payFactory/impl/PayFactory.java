package com.lfgj.clinet.payFactory.impl;

import com.lfgj.util.PayTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lfgj.clinet.payFactory.IPayFactory;
import com.lfgj.clinet.payFactory.IPayService;
import com.lfgj.clinet.payYiKuai.PrePayYiKuaiClient;

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

    @Autowired
    private IPayService prePayWishClient;

    @Autowired
    private IPayService prePayYiKuaiClient;

    @Autowired
    private IPayService prePayYiFuClient;

    @Autowired
    private IPayService prePayBaoBaoClient;

    @Override
    public IPayService generatePayService(PayTypeEnum payType) throws Exception{

        switch (payType) {
            case GLOBAL_WORLD_GATEWAY:
                return prePayHqfClient;
            case LIDA_PAY:
                return prePayLidaClient;
            case YIDA_PAY:
                return prePayYidaClient;
            case SHOUJIE_PAY:
                return prePayShouJieClient;
            case MA_PAY:
                return prePayMazhifuClient;
            case WISH_PAY:
                return prePayWishClient;
            case YIKUAI_PAY:
                return prePayYiKuaiClient;
            case YIFU_PAY:
                return prePayYiFuClient;
            case BAOBAO_PAY:
                return prePayBaoBaoClient;
            default:
                throw new Exception("支付渠道不存在!");

        }

    }


}
