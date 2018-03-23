package com.rrgy.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.sms.model.SmsConfig;
import com.rrgy.system.controller.LoginController;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 短信工具类
 * 
 * @author zhanggw
 *
 */
public class SmsUtil {

    private static Logger log = LoggerFactory.getLogger(SmsUtil.class);
//	public static final String SMS_CODE = "SMS_105655080";
    public static final String SMS_CODE = "SMS_128495161";
	//产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
	
	/**
	 * 阿里大于短信发送
	 * 
	 * @param extend
	 *            1.类型：String 2.是否必须：可选 3.示例值：123456
	 *            4.公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，
	 *            在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
	 * @param sms_type
	 *            1.类型：String 2.是否必须：必须 3.示例值：normal 4.短信类型，传入值请填写normal
	 * @param sms_free_sign_name
	 *            1.类型：String 2.是否必须：必须 3.示例值：阿里大于
	 *            4.短信签名，传入的短信签名必须是在阿里大于“管理中心-短信签名管理”中的可用签名。
	 *            如“阿里大于”已在短信签名管理中通过审核，则可传入”阿里大于“（传参时去掉引号）作为短信签名。
	 *            短信效果示例：【阿里大于】欢迎使用阿里大于服务。
	 * @param sms_param
	 *            1.类型：Json 2.是否必须：可选 3.示例值：{"code":"1234","product":"alidayu"}
	 *            4.短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，
	 *            多个变量之间以逗号隔开。示例：针对模板“验证码${code}，
	 *            您正在进行${product}身份验证，打死不要告诉别人哦！”，
	 *            传参时需传入{"code":"1234","product":"alidayu"}
	 * @param rec_num
	 *            1.类型：String 2.是否必须：必须 3.示例值：13000000000
	 *            4.短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。
	 *            群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。
	 *            示例：18600000000,13911111111,13322222222
	 * @param sms_template_code
	 *            1.类型：String 2.是否必须：必须 3.示例值：SMS_585014
	 *            4.短信模板ID，传入的模板必须是在阿里大于“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
	 * @return 成功返回：
	 *         {"alibaba_aliqin_fc_sms_num_send_response":{"result":{"err_code":
	 *         "0","model":"105804118292^1107871110829","success":true},
	 *         "request_id":"10wc8v7eed73u"}} 失败返回：
	 *         {"error_response":{"request_id":"16eco51f46wkz","code":29,
	 *         "sub_code":"isv.appkey-not-exists","msg":"Invalid app Key"}}
	 * @throws ApiException
	 */
	public static String send(String extend, String sms_type, String sms_param, String rec_num,
			String sms_template_code) throws ApiException {
		Blade blade = Blade.create(SmsConfig.class);
		SmsConfig sc = blade.findById(2);
		String url = sc.getRemark();
		String appkey = sc.getUcode();
		String secret = sc.getPwd();
		String sms_free_sign_name = sc.getQianming();
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend(extend);
		req.setSmsType(sms_type);
		req.setSmsFreeSignName(sms_free_sign_name);// 短信签名
		req.setSmsParamString(sms_param);// 短信参数
		req.setRecNum(rec_num);// 手机号码
		req.setSmsTemplateCode(sms_template_code);// 短信模板id
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		return rsp.getBody();
	}

	public static SendSmsResponse sendT(String extend, String sms_type, String sms_param, String rec_num) throws ClientException {
		Blade blade = Blade.create(SmsConfig.class);
		SmsConfig sc = blade.findById(2);
		String appkey = sc.getUcode();
		String secret = sc.getPwd();


		String sms_free_sign_name = sc.getQianming();
		String temp_code = sc.getRemark();
		 //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", appkey, secret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(rec_num);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(sms_free_sign_name);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(temp_code);

        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(sms_param);

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(extend);

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        log.info(" Process in SmsUtil.sendT method. response: " + sendSmsResponse.getCode()+ ", message: " + sendSmsResponse.getMessage());
        return sendSmsResponse;
	}
	
}