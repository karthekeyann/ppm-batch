package com.cft.hogan.platform.ppm.batch.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
		super(message);
		log.error("Business error occurred. Error information logged and continue processing.");
		log.error(message);
	}
}
