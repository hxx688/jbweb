package com.lfgj.member.service;

import java.math.BigDecimal;
import java.util.Map;

import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.base.service.IService;
import com.lfgj.member.model.Member;

/**
 * Generated by Blade.
 * 2017-09-12 16:12:39
 */
public interface MemberService extends IService<Member>{
	
	boolean updateStatus(String ids, Object status);
	
	Map<String, Object> findOne(Object id);
	
	public boolean addAgent(Member member, Member pAgent);
	
	public int removeAgent(String ids);
	
	public boolean addAgentRecharge(int id, BigDecimal amout);
	
	public boolean addRecharge(int id, BigDecimal amout, boolean isVirtual, String orderNo);
	
	public boolean auditOK(String ids);
	
	public boolean agentFrozen(String ids);
	
	public ResultVo saveNewMember(String tuijian,String mobile,String real_name,String password);
	
	public boolean updatePayInfo(String ordernumber,String pay_order_id, String pay_amount, boolean isAmountDiv);

}
