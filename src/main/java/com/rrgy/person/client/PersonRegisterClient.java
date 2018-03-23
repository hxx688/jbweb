package com.rrgy.person.client;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rrgy.common.iface.Des3;
import com.rrgy.common.iface.RegisterCodeImpl;
import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;
import com.rrgy.sms.service.SmsService;

@Service
@Client(name = "rrgy_2017_1.0_personRegister")
public class PersonRegisterClient extends RequestAbs{
	
	@Autowired
	PersonService service;
	@Autowired
	SmsService smsService;
	
	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		String super_id = getParams("super_id","");
		String username = getParams("username","");
		String mobile = getParams("mobile","");
		String code = getParams("code","");
		String password = getParams("password","");
		String group_id = getParams("group_id","1");
		String nick_name = getParams("real_name","");
		String birthday = getParams("birthday","");
		String sex = getParams("sex","");
		String email = getParams("email","");
		String payPassword = getParams("payPassword","");
		String prodvinceId = getParams("prodvinceId","");
		String cityId = getParams("cityId","");
		String industryId = getParams("industryId","");
		String cardTypes = getParams("cardTypes","");
		String idCard = getParams("idCard","");
		String photo = getParams("photo","");
		
		if(Func.isEmpty(mobile)){
			rv.setReturnMsg("手机号不能为空");
			rv.setReturnCode("1");
			return JsonKit.toJson(rv);
		}
		if(!Func.isMobile(mobile)){
			rv.setReturnMsg("手机号码无效");
			rv.setReturnCode("3");
			return JsonKit.toJson(rv);
		}
		if(!Func.isNum(code)){
			rv.setReturnMsg("无效验证码");
			rv.setReturnCode("2");
			return JsonKit.toJson(rv);
		}
		
//		boolean vlc = smsService.findValidSms(mobile, code);
//		
//		if(!vlc){
//			rv.setReturnMsg("无效验证码");
//			rv.setReturnCode("2");
//			return JsonKit.toJson(rv);
//		}
		
		if(Func.isEmpty(super_id)){
			rv.setReturnMsg("推荐人不能为空");
			rv.setReturnCode("3");
			return JsonKit.toJson(rv);
			
		}
		
		Person superPerson = service.findById(super_id);
		if(superPerson==null){
			rv.setReturnMsg("推荐人不存在");
			rv.setReturnCode("3");
			return JsonKit.toJson(rv);
		}
		
		Person person = new Person();
		person.setMobile(mobile);
		person.setGroup_id(Integer.valueOf(group_id));
		long l = service.count(person);	
		if(l>0){
			rv.setReturnMsg("该手机号码已存在");
			rv.setReturnCode("4");
			return JsonKit.toJson(rv);
		}
		
		person.setUser_name(username);
		String reg_ip = request.getRemoteAddr();
		person.setReg_ip(reg_ip);
		String salt = ShiroKit.getRandomSalt(6);
		person.setSalt(salt);
		password = ShiroKit.md5(password, salt);// 加密后的密码
		person.setPassword(password);
		payPassword = ShiroKit.md5(payPassword, salt);// 加密后的密码
		
		person.setReg_time(new Date());
		person.setStatus(0);
		person.setAmount(new BigDecimal(0));
		person.setPoint(new BigDecimal(0));
		person.setExp(0);
		person.setXin(BigDecimal.ZERO);
		person.setDou(BigDecimal.ZERO);
		person.setYushu(BigDecimal.ZERO);
		person.setHeimingdan(0);
		person.setSheng("0");
		person.setShi("0");
		person.setNick_name(nick_name);
		person.setBirthday(DateTimeKit.parse(birthday, DateTimeKit.NORM_DATE_PATTERN));
		person.setSex(sex);
		person.setEmail(email);
		person.setProvince(prodvinceId);
		person.setCity(cityId);
		person.setHangye(Integer.valueOf(industryId));
		person.setZhengjianleixing(cardTypes);
		person.setZhengjianhaoma(idCard);
		person.setPassword2(payPassword);
		person.setAvatar(photo);
		person.setShangjiId(Integer.valueOf(super_id));
		person.setAudit_level(1);
		
		int id = service.saveRtId(person);
		boolean flag = false;
		if (id > 0) {
			// 设置用户的所有上级id(包含自己)
			// 查找上级id
			Map shangjilistMap = Blade.create(Person.class).findOneColBy("shangjilist", "id = #{id}",
					Paras.create().put("id", person.getShangjiId()));
			String shangjilist = shangjilistMap.get("shangjilist").toString();
			if(StrKit.isBlank(shangjilist)){//上级是否为空
				shangjilist += ","+ id + "," ;
			}else{
				shangjilist +=  id + "," ;
			}
			
			flag = service.updateBy("shangjilist = #{shangjilist}", "id = #{id}",
					Paras.create().put("shangjilist", shangjilist).put("id", id));
		}
		
		if(!flag){
			rv.setReturnCode("1");
			rv.setReturnMsg("注册失败");
		}else{
			rv.setReturnCode("0");
			rv.setReturnMsg("注册成功");
		}
		return JsonKit.toJson(rv);
	}

	public static void main(String[] args) throws Exception {
		String url = "http://localhost:8281/rrgy/clientService";
		String param = "{\"serviceId\":\"rrgy_2017_1.0_personRegister\","
				+ "\"params\":{\"super_id\":\"2211\",\"username\":\"demo\",\"mobile\":\"10102919224\",\"code\":\"651615\",\"password\":\"123456\"}}";
		param = Des3.encode(param);
		String rs = RegisterCodeImpl.requestPage(url,param);
		rs = Des3.decode(rs);
		System.out.println(rs);
	}
}
