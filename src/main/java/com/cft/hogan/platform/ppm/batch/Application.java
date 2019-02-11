package com.cft.hogan.platform.ppm.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cft.hogan.platform.ppm.batch.context.BatchContext;
import com.cft.hogan.platform.ppm.batch.exception.ExceptionHandler;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		try {
			BatchContext.initilizeSystemContext(args);
			SpringApplication.run(Application.class, args);
		}catch(Exception e) {
			ExceptionHandler.handleException(e);
		}
	}
}
