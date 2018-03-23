package com.lfgj.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.lfgj.financial.service.PayInfoService;

/**
 * 
 * @author Administrator
 *
 */
public class PayTask implements Runnable {
	
	@Autowired
	PayInfoService payInfoService;

	@Override
	public void run() {
		payInfoService.refalsePay();
	}
}
