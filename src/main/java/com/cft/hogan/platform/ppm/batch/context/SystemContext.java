package com.cft.hogan.platform.ppm.batch.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.Properties;

import org.springframework.util.StringUtils;

import com.cft.hogan.platform.ppm.batch.exception.BusinessException;
import com.cft.hogan.platform.ppm.batch.exception.SystemException;
import com.cft.hogan.platform.ppm.batch.util.Constants;
import com.cft.hogan.platform.ppm.batch.util.Utils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SystemContext {

	public static  Date bpDate = null;
	public static String region = null;
	public static String environment = null;
	public static Properties restURI = null;


	public static void initilizeSystemContext(String[] args) {
		if(args.length>1) {
			environment = args[0];
			region = args[1];
			if(region==null || StringUtils.isEmpty(region) || 
					!(region.equalsIgnoreCase(Constants.REGION_COR) || region.equalsIgnoreCase(Constants.REGION_TDA) || 
							region.equalsIgnoreCase(Constants.REGION_PASCOR) || region.equalsIgnoreCase(Constants.REGION_PASTDA))) {
				throw new SystemException("Invalid region :"+region);
			}
			if(args.length>2) {
				try {
					bpDate = Utils.convertStringToSQLDate(args[2]);
				} catch (ParseException e) {
					bpDate = Utils.getCurrentDate();
					new BusinessException("Invalid BP date. Proceeding the batch with current system date :"+bpDate);
				}
			}else {
				bpDate = Utils.getCurrentDate();
			}
		}else {
			throw new SystemException("Invalid environment and region");
		}
	}

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
