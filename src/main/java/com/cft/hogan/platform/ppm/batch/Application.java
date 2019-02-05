package com.cft.hogan.platform.ppm.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cft.hogan.platform.ppm.batch.context.EnvironmentContext;
import com.cft.hogan.platform.ppm.batch.util.Utils;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		try {
			EnvironmentContext.initilizeSystemContext(args);
			SpringApplication.run(Application.class, args);
		}catch(Exception e) {
			Utils.handleException(e);
		}
	}
}
