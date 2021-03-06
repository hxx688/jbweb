package com.rrgy.person.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rrgy.auditconf.model.AuditConf;
import com.rrgy.auditconf.model.AuditLog;
import com.rrgy.auditconf.service.AuditLogService;
import com.rrgy.common.vo.ShiroUser;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.shiro.ShiroKit;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.DateKit;
import com.rrgy.core.toolbox.kit.DateTimeKit;
import com.rrgy.core.toolbox.kit.RandomKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.service.PersonService;
import com.lfgj.tixian.model.Tixianjilu;

/**
 * Generated by Blade.
 * 2016-12-29 12:54:00
 */
@Service
public class PersonServiceImpl extends BaseService<Person> implements PersonService{

	@Autowired
	AuditLogService auditLogService;
	
	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("person.findOne", Paras.create().set("id", id), Map.class);
	}

	@Override
	@Transactional
	public int auditPerson(ShiroUser u, String ids) {
		boolean flag = false;
		int userId = (Integer) u.getId();
		Blade auditConfBlade = Blade.create(AuditConf.class);
		List<AuditConf> auditConfList = auditConfBlade.findBy("model_type = #{modelType}",
				Paras.create().set("modelType", "1"));
		Blade auditLogBlade = Blade.create(AuditLog.class);
		List<AuditLog> auditLogList = auditLogBlade.findBy(
				" model_type = #{modelType} and model_id = #{modelId}",
				Paras.create().set("modelType", "1").set("modelId", ids));
		
		//判断是否所有审核人都已审核
		boolean isAll = false;
		//是否当前审核人审核后，就已全部审核完
		boolean isCurrAll = false;
		if(auditConfList.size() > 0){
			int count = 0;
			int _count = 0;
			for (AuditConf auditConf : auditConfList) {
				if(auditLogList.size() > 0){
					for (AuditLog auditLog : auditLogList) {
						if(auditLog.getUser_id() == auditConf.getUser_id()){
							count ++;
						}
						
						if(auditLog.getUser_id() == auditConf.getUser_id() || auditConf.getUser_id() == userId){
							_count ++;
						}
					}
				}else{
					if(auditConf.getUser_id() == userId){
						_count ++;
					}
				}
				
			}
			//如果审核人数量等于已审核人数量，说明所有审核人都已经审核过
			if(auditConfList.size() == count){
				isAll = true;
			}
			
			if(auditConfList.size() == _count){
				isCurrAll = true;
			}
			
		}

		//如果有审核人并且还还有人未审核
		if (auditConfList.size() > 0 && !isAll) {
			AuditConf auditConf = auditConfBlade.findFirstBy(" user_id = #{userId} and model_type = #{modelType}",
					Paras.create().set("userId", userId).set("modelType", "1"));
			// 查询是否有审核权限
			if (null != auditConf) {
				//循环判断当前用户是否已审核
				for (AuditLog auditLog : auditLogList) {
					//当前用户已审核
					if(auditLog.getUser_id() == userId){
						return 3;
					}
				}
				
				//插入审核记录
				AuditLog auditLog = new AuditLog();
				auditLog.setModel_id(Integer.valueOf(ids));
				auditLog.setModel_type("1");
				auditLog.setUser_id(Integer.valueOf(userId));
				auditLog.setAdd_time(DateKit.parseTime(DateKit.getTime()));
				auditLogService.insert(auditLog);
				
				//如果全部审核人都审核完，就改变审核状态
				if(isCurrAll){
					//审核
					flag = updateStatus(ids, "0");
				}else{
					flag = true;
				}
				
			} else {
				return 2;
			}
		} else {
			// 如果没有审核人只要随便一个人审核通过即可
			flag = updateStatus(ids, "0");
			if(flag){
				//插入审核记录
				AuditLog auditLog = new AuditLog();
				auditLog.setModel_id(Integer.valueOf(ids));
				auditLog.setModel_type("1");
				auditLog.setUser_id(Integer.valueOf(userId));
				auditLog.setAdd_time(DateKit.parseTime(DateKit.getTime()));
				flag = auditLogService.insert(auditLog);
			}
		}
		if (flag) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int auditLevelPerson(ShiroUser u, Person person)throws Exception {
		Blade confBlade = Blade.create(AuditConf.class);
		Blade personBlade = Blade.create(Person.class);
		Person p = personBlade.findById(person.getId());
		AuditConf ac = confBlade.findFirstBy(" user_id = #{user_id} and model_type = #{model_type} and audit_level = #{audit_level}",
				Paras.create().set("user_id", u.getId()).set("model_type", 2).set("audit_level", p.getAudit_level()));
		//判断是否有权限
		if(null != ac){
			Blade logBlade = Blade.create(AuditLog.class);
			AuditLog al = logBlade.findFirstBy(" model_id = #{model_id} and model_type = #{model_type} and user_id = #{user_id} and audit_level = #{audit_level} and audit_status = #{audit_status}",
					Paras.create().set("model_id", person.getId()).set("model_type", 2).set("user_id", u.getId()).set("audit_level", p.getAudit_level()).set("audit_status", 1));
			//判断是否已审核过
			if(null == al){
				//审核通过
				if(person.getStatus() == 1){
					AuditLog auditLog = new AuditLog();
					auditLog.setAdd_time(DateTimeKit.date());
					auditLog.setAudit_level(p.getAudit_level()+"");
					auditLog.setAudit_remark(person.getYuanyin());
					auditLog.setAudit_status(person.getStatus());
					auditLog.setModel_id(person.getId());
					auditLog.setModel_type("2");
					auditLog.setUser_id(Integer.valueOf(u.getId().toString()));
					logBlade.save(auditLog);
					int level = p.getAudit_level();
					int status = 2;
					//如果还未审核到三级，当前审核级别加1
					if(level < 3){
						level ++;
					}else{
						//三级都审核完，人员状态更新为正常
						status = 0;
					}
					
					Md.update("person.updateStatus", Paras.create().set("status", status).set("audit_level", level).set("id", person.getId()));
					
				}else{
					AuditLog auditLog = new AuditLog();
					auditLog.setAdd_time(DateTimeKit.date());
					auditLog.setAudit_level(p.getAudit_level()+"");
					auditLog.setAudit_remark(person.getYuanyin());
					auditLog.setAudit_status(person.getStatus());
					auditLog.setModel_id(person.getId());
					auditLog.setModel_type("2");
					auditLog.setUser_id(Integer.valueOf(u.getId().toString()));
					logBlade.save(auditLog);
					
					Md.update("person.updateStatus", Paras.create().set("status", 3).set("audit_level", p.getAudit_level()).set("id", person.getId()));
				}
			}else{
				return 3;
			}
		}else{
			return 2;
		}
		return 1;
	}
	
	
	@Override
	public boolean savebuydou(Tixianjilu tixian) {
		tixian.setTixianNum("T"+DateKit.getAllTime()+RandomKit.randomNumbers(4)); // 订单号
		boolean rs = Blade.create(Tixianjilu.class).save(tixian);
		Person person = new Person();
		person.setId(tixian.getUserId());
		Person user = Blade.create(Person.class).findById(tixian.getUserId());
		BigDecimal remain = user.getDou().subtract(tixian.getAmount());
		person.setDou(remain);
		person.setStatus(user.getStatus());
		rs &= Blade.create(Person.class).update(person);
		return rs;
	}

	@Override
	public boolean checkMobile(Person person) {
		Person p = Blade.create(Person.class).findFirstBy(" id <> #{id} and mobile = #{mobile} and group_id = #{group_id}", 
				Paras.create().set("id", person.getId()).set("mobile", person.getMobile()).set("group_id", person.getGroup_id()));
		
		if(null != p){
			return false;
		}
		
		return true;
	}

}
