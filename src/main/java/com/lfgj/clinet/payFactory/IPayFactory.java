package com.lfgj.clinet.payFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lfgj.clinet.payHqf.PrePayHqfClient;
import com.rrgy.common.iface.ResultVo;

/**
 * <p>Description: </p>
 * @date 2018/3/27
 * @author 贺锟 
 * @version 1.0
 * <p>Company:Mopon</p>
 * <p>Copyright:Copyright(c)2017</p>
 */
public interface IPayFactory {

    public IPayService generatePayService(String payType) throws Exception;

}
