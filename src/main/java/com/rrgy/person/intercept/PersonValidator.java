
package com.rrgy.person.intercept;

import com.rrgy.core.aop.Invocation;
import com.rrgy.core.intercept.BladeValidator;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.person.model.Person;

public class PersonValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		String group_id = request.getParameter("dt_users.group_id");
		String id = request.getParameter("dt_users.id");
		String zhengjianhaoma = request.getParameter("dt_users.zhengjianhaoma");
		//如果不是商家需要验证身份证号码是否存在
		if(!"2".equals(group_id)){
			Blade blade = Blade.create(Person.class);
			Person p = blade.findFirstBy(" group_id = #{group_id} and id <> #{id} and zhengjianhaoma = #{zhengjianhaoma}", 
					Paras.create().set("group_id", group_id).set("id", id).set("zhengjianhaoma", zhengjianhaoma));
			if(null != p){
				validateRequired("dt_users.zhengjianhaoma", "证件号码已存在");
			}
		}
	}
	

}
