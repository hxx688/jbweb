package com.lfgj.clinet.payFactory;

import com.lfgj.util.PayTypeEnum;

/**
 * <p>Description: </p>
 * @date 2018/3/27
 * @author 贺锟 
 * @version 1.0
 * <p>Company:Mopon</p>
 * <p>Copyright:Copyright(c)2017</p>
 */
public interface IPayFactory {

    public IPayService generatePayService(PayTypeEnum payTypeEnum) throws Exception;

}
