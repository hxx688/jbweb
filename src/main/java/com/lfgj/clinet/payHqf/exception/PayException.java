package com.lfgj.clinet.payHqf.exception;

import javax.servlet.ServletException;

/**
 * 
 * Description： 支付异常类<br>
 * 
 * ClassName：PayException <br>
 * 
 * Date：2016年6月26日上午12:08:30 <br>
 * 
 * Version：v1.0 <br>
 * 
 *
 */
public class PayException extends ServletException {

	/**
	*
	**/
	private static final long serialVersionUID = 1L;

	public PayException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PayException(String message, Throwable rootCause) {
		super(message, rootCause);
		// TODO Auto-generated constructor stub
	}

	public PayException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PayException(Throwable rootCause) {
		super(rootCause);
		// TODO Auto-generated constructor stub
	}



	
}
