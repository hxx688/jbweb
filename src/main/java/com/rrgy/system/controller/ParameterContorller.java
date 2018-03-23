/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (cbjr@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rrgy.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rrgy.core.base.controller.CurdController;
import com.rrgy.core.interfaces.IMeta;
import com.rrgy.core.plugins.dao.Blade;
import com.rrgy.system.meta.factory.ParameterFactory;
import com.rrgy.system.model.Parameter;

@Controller
@RequestMapping("/parameter")
public class ParameterContorller extends CurdController<Parameter>{

	@Override
	protected Class<? extends IMeta> metaFactoryClass() {
		
		return ParameterFactory.class;
	}
	
	@RequestMapping(KEY_EDIT + "/{id}")
	public ModelAndView edit(@PathVariable String id) {
		Parameter p = Blade.create(Parameter.class).findById(id);
		if(p.getType() != null && p.getType() == 1){ // 附件
			this.getRenderMap().put(KEY_EDIT, "/system/parameter/parameter_edit2.html");
		}else{
			this.getRenderMap().put(KEY_EDIT, "/system/parameter/parameter_edit.html");
		}
		return super.edit(id);
	}
	
}
