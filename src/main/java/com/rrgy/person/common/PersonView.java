package com.rrgy.person.common;


import java.math.BigDecimal;

import org.springframework.ui.ModelMap;

import com.rrgy.benefit.model.Benefit;
import com.rrgy.city.model.City;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.rrgy.core.toolbox.kit.JsonKit;
import com.rrgy.core.toolbox.kit.MathKit;
import com.rrgy.person.model.Person;
import com.rrgy.person.model.PersonGroup;
import com.rrgy.person.service.PersonService;

public class PersonView {

	
	/**
	 * 查看
	 * @param mm
	 * @param service
	 */
	public static void getPersonView(ModelMap mm, PersonService service,String id) {
		Blade blade = Blade.create(City.class);
		Person person = service.findById(id);
		City province = blade.findById(person.getProvince());
		City city = blade.findById(person.getCity());
		if (province != null) {
			person.setProvince_name(province.getName());
		}
		if (city != null) {
			person.setCity_name(city.getName());
		}

		Blade groupblade = Blade.create(PersonGroup.class);
		PersonGroup pg = groupblade.findById(person.getGroup_id());
		if (pg != null) {
			person.setGroup_name(pg.getTitle());
		}
		if(null != person.getBili()){
			String[] bilis = person.getBili().split(",");
			String biliName = "";
			for (String bili : bilis) {
				Benefit benefit = Blade.create(Benefit.class).findById(bili);
				if(null != benefit){
					biliName += (biliName.equals("")?"":",") + benefit.getTitle();
				}
			}
			person.setBili(biliName);
		}
		Paras para = Paras.create().set("userId", id);
		BigDecimal huode_xin = Md.queryDecimal("xiaofeijilu.getAixinAllTotal", para);
		BigDecimal fanhuan_xin = Md.queryDecimal("xiaofeijilu.getFanhuanxinAllTotal", para);
		BigDecimal aixin = MathKit.sub(huode_xin, fanhuan_xin);
		mm.put("huode_xin", huode_xin);
		mm.put("fanhuan_xin", fanhuan_xin);
		mm.put("aixin", aixin);
		mm.put("model", JsonKit.toJson(person));
		mm.put("person", person);
		mm.put("id", id);
	}

}
