package com.lfgj.member.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lfgj.clinet.pay.payment.PayInfo;
import com.lfgj.financial.model.Financial;
import com.lfgj.member.model.Member;
import com.lfgj.member.service.MemberService;
import com.lfgj.util.LfConstant;
import com.lfgj.util.MD5;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Func;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.MathKit;
import com.rrgy.core.toolbox.kit.StrKit;
import com.rrgy.system.model.User;

/**
 * Generated by Blade.
 * 2017-09-12 16:12:39
 */
@Service
public class MemberServiceImpl extends BaseService<Member> implements MemberService{

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("member.findOne", Paras.create().set("id", id), Map.class);
	}
	
	@Override
	public int removeAgent(String ids) {
		int cnt = deleteByIds(ids);
		
		// 删除后台账户
		String[] idArr = ids.split(",");
		String userIds = "";
		for(String id : idArr){
			if(StrKit.isNotEmpty(id)){
				User user = Md.selectUnique("user.findOneMember", Paras.create().set("member_id", id), User.class);
				if(user != null){
					if(StrKit.isNotEmpty(userIds)){
						userIds += ",";
					}
				}
			}
		}
		if(StrKit.isNotEmpty(userIds)){
			Blade.create(User.class).deleteByIds(userIds);
		}
		
		return cnt;
	}
	
	@Override
	public boolean addAgent(Member member, Member pAgent) {
		member.setReg_time(new Date());
		member.setAmount(new BigDecimal("0"));
		member.setPoint(new BigDecimal("0"));
		member.setProfi_loss(new BigDecimal("0"));
		member.setRecharge(new BigDecimal("0"));
		member.setWithdraw(new BigDecimal("0"));
		member.setIs_agent(1);
		
		if(pAgent == null){ // 平台添加一级代理商
			member.setLevel(1);
			member.setStatus(0);
		}else{
			member.setParent_id(pAgent.getId());
			member.setParent_name(pAgent.getReal_name());
			if(pAgent.getLevel()==1){
				member.setFirst_parent(pAgent.getId());
			}else{
				member.setFirst_parent(pAgent.getFirst_parent());
			}
			if(!Func.isEmpty(pAgent.getParent_ids())){
				member.setParent_ids(pAgent.getId()+","+pAgent.getParent_ids());
			}else{
				member.setParent_ids(pAgent.getId().toString());
			}
			
			Integer level = 1;
			if(pAgent.getLevel() != null){
				level = pAgent.getLevel() + 1;
				if(level > 5){
					throw new RuntimeException("您是5级代理，不能再添加下级代理!");
				}
			}else{
				throw new RuntimeException("您的级别未知，不能添加下级代理!");
			}
			member.setLevel(level);
			member.setStatus(2);
		}
		
		
		MD5 md5 = new MD5();
		member.setPassword(md5.enCodeByMD5(member.getMobile(), member.getMobile()));
		member.setPay_password(md5.enCodeByMD5(member.getMobile(), member.getMobile()));
		
		int id = saveRtId(member);
		
		// 添加用户表及权限
		User user = new User();
		user.setAccount(member.getMobile());
		user.setName(member.getReal_name());
		user.setRoleid(LfConstant.agent_role); // 代理商 
		
		String pwd = member.getMobile();
		String salt = ShiroKit.getRandomSalt(5);
		String pwdMd5 = ShiroKit.md5(pwd, salt);
		user.setPassword(pwdMd5);
		user.setSalt(salt);
		if(pAgent == null){
			user.setStatus(1);
		}else{
			user.setStatus(3);
		}
		user.setCreatetime(new Date());
		user.setVersion(0);
		user.setIs_agent(1);
		user.setMember_id(id);
		user.setDeptid(LfConstant.agent_dept);
		User u = Blade.create(User.class).findFirstBy(" account = #{account}",
				Paras.create().set("account", user.getAccount()));
		if(null != u){
			throw new RuntimeException("该账户已存在!");
		}
		boolean temp = false;
		int uid = Blade.create(User.class).saveRtId(user);
		if(uid>0){
			temp = true;
		}	
		if(!temp){
			throw new RuntimeException("同步到后台账号失败!");
		}
		
		Member m = new Member();
		m.setId(id);
		m.setAgent_id(uid);
		m.setAgent_name(user.getName());
		if(pAgent == null){
			m.setFirst_parent(id);
		}
		
		temp = Blade.create(Member.class).update(m);
		if(!temp){
			throw new RuntimeException("更新代理商会员账号失败!");
		}
		
		return temp;
		
	}
	
	@Override
	public boolean addAgentRecharge(int id, BigDecimal amout) {
		boolean temp = addRecharge(id, amout, true, Func.orderNo("P"));
		return temp;
	}
	
	@Override
	public boolean addRecharge(int id, BigDecimal amout, boolean isVirtual, String orderNo) {

		String sql = "select * from dt_financial where orderNo=#{ordernumber}";
		Financial exist = Blade.create(Financial.class).findFirst(sql, Paras.create().set("ordernumber", orderNo));
		if(exist!=null){
			System.out.println(orderNo+":订单已存在");
			return true;
		}
		
		Member m = findById(id);
		if(m.getAmount() == null){
			m.setAmount(new BigDecimal("0"));
		}
		if(m.getRecharge() == null){
			m.setRecharge(new BigDecimal("0"));
		}
		
		m.setAmount(m.getAmount().add(amout));
		m.setRecharge(m.getRecharge().add(amout));
		
		boolean temp = Blade.create(Member.class).update(m);
		
		if(!temp){
			throw new RuntimeException("更新账户余额异常!");
		}
		
		Financial f = new Financial();
		f.setSource_type(LfConstant.Source.A.value);
		f.setUser_id(m.getId());
		f.setFinancial_type(1);
		f.setPhone(m.getMobile());
		f.setUser_name(m.getReal_name());
		f.setAmount(amout);
		f.setCreate_time(new Date());
		f.setUser_amount(m.getAmount());
		f.setDesc(isVirtual ? "后台虚拟充值" : "");
		f.setOrderNo(orderNo);
		temp = Blade.create(Financial.class).save(f);
		if(!temp){
			throw new RuntimeException("保存交易记录异常!");
		}
		
		return temp;
		
	}
	
	@Override
	public boolean auditOK(String ids){
		Blade blade = Blade.create(Member.class);
		Paras updateMap = Paras.create().set("status", 0).set("ids", ids.split(","));
		boolean temp = blade.updateBy("status = #{status}", "id in (#{join(ids)})", updateMap);
		
		if(temp){
			Blade blade2 = Blade.create(User.class);
			Paras updateMap2 = Paras.create().set("status", 1).set("ids", ids.split(","));
			temp = blade2.updateBy("status = #{status}", "MEMBER_ID in (#{join(ids)})", updateMap2);
			
			if(!temp){
				throw new RuntimeException("审核出现异常!");
			}
		}
		return temp;
	}
	
	@Override
	public boolean agentFrozen(String ids){
		Paras updateMap = Paras.create().set("ids", ids.split(","));
		boolean temp = Blade.create(Member.class).updateBy("status = (CASE WHEN STATUS=0 THEN 3 ELSE 0 END)", "id in (#{join(ids)})", updateMap);
		
		if (temp) {
			temp = Blade.create(User.class).updateBy("status = (CASE WHEN STATUS=1 THEN 2 ELSE 1 END)", "member_id in (#{join(ids)})", updateMap);
		} else {
			throw new RuntimeException("冻结出现异常!");
		}
		
		return temp;
	}

	public ResultVo saveNewMember(String tuijian,String mobile,String real_name,String password){
		ResultVo rv = new ResultVo();
		
		Member supermember = Blade.create(Member.class).findById(tuijian);
		if(supermember==null){
			rv.setReturnMsg("推荐人不存在");
			rv.setReturnCode("3");
			return rv;
		}
		
		Member member = new Member();
		member.setMobile(mobile);
		long l = count(member);	
		if(l>0){
			rv.setReturnMsg("该手机号码已存在");
			rv.setReturnCode("4");
			return rv;
		}
		
		if(supermember.getIs_agent()==1){
			User user = new User();
			user.setMember_id(supermember.getId());
			user = Blade.create(User.class).findTopOne(user);
			if(user!=null){
				member.setAgent_id(user.getId());
				member.setAgent_name(user.getName());
			}else{
				rv.setReturnMsg("代理商不存在");
				rv.setReturnCode("3");
				return rv;
			}
		}else{
			member.setAgent_id(supermember.getAgent_id());
			member.setAgent_name(supermember.getAgent_name());
		}
			
		if(supermember.getLevel()!=null&&supermember.getLevel()==1){
			member.setParent_ids(supermember.getId().toString());
			member.setFirst_parent(supermember.getId());		
		}else if(!Func.isEmpty(supermember.getParent_ids())&&supermember.getIs_agent()==1){
			member.setParent_ids(supermember.getId()+","+supermember.getParent_ids());
			member.setFirst_parent(supermember.getFirst_parent());
		}else{
			member.setParent_ids(supermember.getParent_ids());
			member.setFirst_parent(supermember.getFirst_parent());
		}
		
		member.setParent_id(supermember.getId());
		member.setParent_name(supermember.getReal_name());		
		member.setReal_name(real_name);
		member.setBank_mobile(mobile);
		
		MD5 md5 = new MD5();
		password = md5.enCodeByMD5(password,member.getMobile());
		member.setPassword(password);
		member.setPay_password(password);
		
		member.setReg_time(new Date());
		member.setStatus(2);
		member.setAmount(new BigDecimal(0));
		member.setPoint(new BigDecimal(0));
		member.setReg_time(new Date());
		member.setAmount(new BigDecimal("0"));
		member.setPoint(new BigDecimal("0"));
		member.setProfi_loss(new BigDecimal("0"));
		member.setRecharge(new BigDecimal("0"));
		member.setWithdraw(new BigDecimal("0"));
		member.setIs_agent(0);
		
		int id = saveRtId(member);
		
		if(id>0){
			rv.setReturnCode("0");
			rv.setReturnMsg("注册成功");
		}else{
			rv.setReturnCode("1");
			rv.setReturnMsg("注册失败");
		}
		
		return rv;
	}
	
	/**
	 * 支付完成后更新支付订单
	 * @param ordernumber 订单号
	 * @param pay_order_id 支付平台交易流水号
	 * @param pay_amount 实际交易金额
	 * @param isAmountDiv 金额是否要除100
	 * @return
	 */
	public boolean updatePayInfo(String ordernumber,String pay_order_id, String pay_amount, boolean isAmountDiv){
		String sql = "select * from dt_payinfo where ordernumber=#{ordernumber}";
		PayInfo payInfo = Blade.create(PayInfo.class).findFirst(sql, Paras.create().set("ordernumber", ordernumber));
		
		String div = isAmountDiv ? "100" : "1";
		
		boolean result = false;
		if(payInfo != null && "0".equals(payInfo.getRespcode())){
			String respcode = "2";
			payInfo.setRespcode(respcode);
			payInfo.setBusinesstime(DateKit.getTime());
			payInfo.setRespmsg("支付成功");
			payInfo.setPayorderid(pay_order_id);
			payInfo.setRespname(LfConstant.PAY_RESPCODE.get(respcode));
			Blade.create(PayInfo.class).update(payInfo);
			
			if("2".equals(respcode)){
				result = addRecharge(payInfo.getUser_id(), MathKit.div(pay_amount, div, 2), false,payInfo.getOrdernumber());
			}
		}
		return result;
	}
}
