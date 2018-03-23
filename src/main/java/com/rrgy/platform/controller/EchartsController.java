package com.rrgy.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rrgy.common.base.BaseController;

@Controller
@RequestMapping("/echarts")
public class EchartsController extends BaseController {

	@GetMapping
	public String echarts() {
		return "/platform/echarts/echarts.html";
	}
	
}
