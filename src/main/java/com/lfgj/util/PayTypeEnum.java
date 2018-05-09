package com.lfgj.util;

/**
 * <p>Description: </p>
 *
 * @author 贺锟
 * @version 1.0
 *          <p>Company:Mopon</p>
 *          <p>Copyright:Copyright(c)2017</p>
 * @date 2018/5/9
 */
public enum PayTypeEnum {

    UNION_BANK_QUICK("0", "银联快捷支付"),
    UNION_BANK_ONLINE("1", "银联在线支付"),
    UNION_BANK_GATEWAY("5", "银联网关支付"),
    GLOBAL_WORLD_PAY("6", "环球付"),
    GLOBAL_WORLD_GATEWAY("7", "环球付网关支付"),
    LIDA_PAY("8", "立达支付"),
    YIDA_PAY("9", "易达支付"),
    SHOUJIE_PAY("10", "首捷支付"),
    MA_PAY("11", "码支付"),
    WISH_PAY("12", "网逸支付"),
    YIKUAI_PAY("13", "易快支付"),

    ;
    private String payCode;
    private String payName;

    private PayTypeEnum(String payCode, String payName) {
        this.payCode = payCode;
        this.payName = payName;
    }

    public static PayTypeEnum parseByCode(String payCode) {
        for(PayTypeEnum payTypeEnum : values()) {
            if(payTypeEnum.getPayCode().equals(payCode)){
                return payTypeEnum;
            }
        }
        return null;
    }

    public String getPayCode() {
        return payCode;
    }

    public String getPayName() {
        return payName;
    }
}
