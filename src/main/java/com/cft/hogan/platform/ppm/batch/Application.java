package com.cft.hogan.platform.ppm.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cft.hogan.platform.ppm.batch.context.SystemContext;
import com.cft.hogan.platform.ppm.batch.util.Utils;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		try {
			SystemContext.initilizeSystemContext(args);
			SystemContext.readRESTURI();
			SpringApplication.run(Application.class, args);
		}catch(Exception e) {
			Utils.handleException(e);
		}
	}
	
	
	
}
