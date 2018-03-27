package com.lfgj.clinet.payLida;

public class PaymentForOnlineService {




    /**
     * ����hmac����
     * ҵ������
     * @param p0_Cmd
     * �̻����
     * @param p1_MerId
     * �̻�������
     * @param p2_Order
     * ֧�����
     * @param p3_Amt
     * ���ױ���
     * @param p4_Cur
     * ��Ʒ����
     * @param p5_Pid
     * ��Ʒ����
     * @param p6_Pcat
     * ��Ʒ����
     * @param p7_Pdesc
     * �̻�����֧���ɹ����ݵĵ�ַ
     * @param p8_Url
     * �ͻ���ַ
     * @param p9_SAF
     * �̻���չ��Ϣ
     * @param pa_MP
     * ���б���
     * @param pd_FrpId
     * Ӧ�����
     * @param pr_NeedResponse
     * �̻���Կ
     * @param keyValue
     * @return
     */
    public static String getReqMd5HmacForOnlinePayment(String p0_Cmd,String p1_MerId,
                                                       String p2_Order, String p3_Amt, String p4_Cur,String p5_Pid, String p6_Pcat,
                                                       String p7_Pdesc,String p8_Url, String p9_SAF,String pa_MP,String pd_FrpId,
                                                       String pr_NeedResponse,String keyValue) {
        StringBuffer sValue = new StringBuffer();
        // ҵ������
        sValue.append(p0_Cmd);
        // �̻����
        sValue.append(p1_MerId);
        // �̻�������
        sValue.append(p2_Order);
        // ֧�����
        sValue.append(p3_Amt);
        // ���ױ���
        sValue.append(p4_Cur);
        // ��Ʒ����
        sValue.append(p5_Pid);
        // ��Ʒ����
        sValue.append(p6_Pcat);
        // ��Ʒ����
        sValue.append(p7_Pdesc);
        // �̻�����֧���ɹ����ݵĵ�ַ
        sValue.append(p8_Url);
        // �ͻ���ַ
        sValue.append(p9_SAF);
        // �̻���չ��Ϣ
        sValue.append(pa_MP);
        // ���б���
        sValue.append(pd_FrpId);
        // Ӧ�����
        sValue.append(pr_NeedResponse);

        String sNewString = null;

        sNewString = DigestUtil.hmacSign(sValue.toString(), keyValue);
        return (sNewString);
    }


    /**
     * ����У��hmac����
     *
     * @param hmac
     * �̻����
     * @param p1_MerId
     * ҵ������
     * @param r0_Cmd
     * ֧�����
     * @param r1_Code
     * �ױ�֧��������ˮ��
     * @param r2_TrxId
     * ֧�����
     * @param r3_Amt
     * ���ױ���
     * @param r4_Cur
     * ��Ʒ����
     * @param r5_Pid
     * �̻�������
     * @param r6_Order
     * �ױ�֧����ԱID
     * @param r7_Uid
     * �̻���չ��Ϣ
     * @param r8_MP
     * ���׽����������
     * @param r9_BType
     * ���׽����������
     * @param keyValue
     * @return
     */
    public static boolean verifyCallback(String hmac, String p1_MerId,
                                         String r0_Cmd, String r1_Code, String r2_TrxId, String r3_Amt,
                                         String r4_Cur, String r5_Pid, String r6_Order, String r7_Uid,
                                         String r8_MP, String r9_BType, String keyValue) {
        StringBuffer sValue = new StringBuffer();
        // �̻����
        sValue.append(p1_MerId);
        // ҵ������
        sValue.append(r0_Cmd);
        // ֧�����
        sValue.append(r1_Code);
        // �ױ�֧��������ˮ��
        sValue.append(r2_TrxId);
        // ֧�����
        sValue.append(r3_Amt);
        // ���ױ���
        sValue.append(r4_Cur);
        // ��Ʒ����
        sValue.append(r5_Pid);
        // �̻�������
        sValue.append(r6_Order);
        // �ױ�֧����ԱID
        sValue.append(r7_Uid);
        // �̻���չ��Ϣ
        sValue.append(r8_MP);
        // ���׽����������
        sValue.append(r9_BType);
        String sNewString = null;
        sNewString = DigestUtil.hmacSign(sValue.toString(), keyValue);

        if (hmac.equals(sNewString)) {
            return (true);
        }
        return (false);
    }

}