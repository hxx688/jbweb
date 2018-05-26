package com.lfgj.util;
/**
 * <p>Description: </p>
 * @date 2018/5/9
 * @author 贺锟 
 * @version 1.0
 * <p>Company:Mopon</p>
 * <p>Copyright:Copyright(c)2017</p>
 */
public class ConverterUtil {


    public static PayTypeEnum cvtPayTypeEnumByChannel(String payChannel) {
        if("1".equals(payChannel)) {
            return PayTypeEnum.MA_PAY;
        }else if("2".equals(payChannel)){
            return PayTypeEnum.BAOBAO_PAY;
        }else if("3".equals(payChannel)) {
            return PayTypeEnum.BAOBAO_PAY;
        }else {
            // 默认
            return PayTypeEnum.MA_PAY;
        }
    }
}
