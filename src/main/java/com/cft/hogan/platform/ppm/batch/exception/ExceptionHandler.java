package com.cft.hogan.platform.ppm.batch.exception;

import com.cft.hogan.platform.ppm.batch.context.BatchContext;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ExceptionHandler {

	public static void handleException(Exception e) {
		StringBuffer msg = new StringBuffer();
		msg.append(" --Region :").append(BatchContext.region).append(e.getMessage());
		log.error(msg.toString(), e);
		if(e instanceof BusinessError) {
			//DO NOTHING
		}else {
			throw new SystemError();
		}
	}

}
