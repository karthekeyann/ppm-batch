package com.cft.hogan.platform.ppm.batch.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.Properties;

import com.cft.hogan.platform.ppm.batch.exception.SystemException;
import com.cft.hogan.platform.ppm.batch.util.Constants;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PropertyContext {

	public static  Date bpDate = null;
	public static String region = null;
	public static String environment = null;
	public static Properties restURI = null;


	public static void readRESTURI() throws IOException {
		restURI = new Properties();
		File file = null;
		if(environment.equalsIgnoreCase(Constants.ENV_TEST)) {
			file = new File(Constants.PPM_REST_URI_TEST);
		}else if(environment.equalsIgnoreCase(Constants.ENV_QA)) {
			file = new File(Constants.PPM_REST_URI_QA);
		}else if(environment.equalsIgnoreCase(Constants.ENV_PROD)) {
			file = new File(Constants.PPM_REST_URI_PROD);
		}
		if(file.exists()) {
			BufferedReader inputStream = new BufferedReader(new FileReader(file));
			try {
				restURI.load(inputStream);
			}
			finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}else {
			throw new SystemException("REST Service URI properties file does not exists: "+file.getAbsolutePath());
		}
	}
	
	
	public static void logDetails() {
		log.info("Environment :"+environment);
		log.info("BP Date :"+bpDate);
		log.info("Region :"+region);
		restURI.forEach((key, value)->{
			log.info(key+":"+value);
		});
	}

}
