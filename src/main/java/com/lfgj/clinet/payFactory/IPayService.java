package com.lfgj.clinet.payFactory;

import com.rrgy.common.iface.ResultVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: </p>
 * @date 2018/3/27
 * @author 贺锟 
 * @version 1.0
 * <p>Company:Mopon</p>
 * <p>Copyright:Copyright(c)2017</p>
 */
public interface IPayService {

    public ResultVo getPayUrl(String money, String orderNo, String... extendStrs);

    public ResultVo url(HttpServletRequest request);
}
