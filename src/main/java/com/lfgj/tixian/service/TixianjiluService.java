package com.lfgj.tixian.service;

import java.util.Map;

import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.base.service.IService;
import com.lfgj.member.model.Member;
import com.lfgj.tixian.model.Tixianjilu;

/**
 * Generated by Blade.
 * 2017-01-09 15:38:45
 */
public interface TixianjiluService extends IService<Tixianjilu>{
	
	boolean updateStatus(String ids, Object status);
	
	Map<String, Object> findOne(Object id);

	/**
	 * 提现审核
	 * @param user
	 * @param tixianjilu
	 * @return
	 * @throws Exception
	 */
	public String auditTixian(ShiroUser user, Tixianjilu tixianjilu)throws Exception;
	
	/**
	 * 提现
	 * @param person
	 * @param amount
	 * @return
	 */
	public boolean saveTixain(Member person,String amount,Tixianjilu 提现,String ka_id);
	
	public boolean updateByNum(Tixianjilu tixian);

}