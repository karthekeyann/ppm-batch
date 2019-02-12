package com.cft.hogan.platform.ppm.batch.context;

import java.sql.Date;
import java.text.ParseException;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.cft.hogan.platform.ppm.batch.exception.BusinessError;
import com.cft.hogan.platform.ppm.batch.exception.SystemError;
import com.cft.hogan.platform.ppm.batch.util.Constants;
import com.cft.hogan.platform.ppm.batch.util.Utils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BatchContext {

	private static  Date bpDate = null;
	private static String region = null;
	private static Environment env = null;

	public static void setRegion(String param) {
		region = param;
		if(region==null || StringUtils.isEmpty(region) || 
				!(region.equalsIgnoreCase(Constants.REGION_COR) || region.equalsIgnoreCase(Constants.REGION_TDA) || 
						region.equalsIgnoreCase(Constants.REGION_PASCOR) || region.equalsIgnoreCase(Constants.REGION_PASTDA))) {
			throw new SystemError("Invalid region :"+region);
		}
	}
	
	public static String getRegion() {
		return region;
	}

	public static void setEnv(Environment lEnv) {
		env = lEnv;
	}

	public static void setBpDate(String param) {
		if(param == null) {
			try {
				bpDate = Utils.convertStringToSQLDate(param);
			} catch (ParseException e) {
				bpDate = Utils.getCurrentDate();
				new BusinessError("Invalid BP date param."+param+" .Proceeding the batch with current system date :"+bpDate);
			}
		}else {
			bpDate = Utils.getCurrentDate();
		}
	}
	
	public static Date getBpDate() {
		return bpDate;
	}

	public static void logDetails() {
		log.info("Environment :"+env.getActiveProfiles()[0]);
		log.info("BP Date :"+bpDate);
		log.info("Region :"+region);
		log.info("URI :"+getImportApiURI());
		log.info("URI :"+getExportApiURI() );
		log.info("URI :"+getScheduleApiURI());
	}

	public static String getImportApiURI() {
		return env.getProperty("import.api.uri");
	}

	public static String getExportApiURI() {
		return env.getProperty("export.api.uri");
	}

	public static String getScheduleApiURI() {
		return env.getProperty("schedule.api.uri");
	}

	public static String getBatchUserId() {
		return env.getProperty("ppm.batch.user");
	}

	public static String getBatchPassword() {
		return env.getProperty("ppm.batch.password");
	}
}
