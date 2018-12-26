package com.cft.hogan.platform.ppm.batch.exception;

public class SystemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemException() {
		super("System error occurred. Please try again or contact system administrator.");
	}
	
	
	public SystemException(String message) {
		super(message);
	}

}
