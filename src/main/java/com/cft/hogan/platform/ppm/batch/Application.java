package com.cft.hogan.platform.ppm.batch;

import java.text.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import com.cft.hogan.platform.ppm.batch.context.PropertyContext;
import com.cft.hogan.platform.ppm.batch.exception.BusinessException;
import com.cft.hogan.platform.ppm.batch.exception.SystemException;
import com.cft.hogan.platform.ppm.batch.util.Constants;
import com.cft.hogan.platform.ppm.batch.util.Utils;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		try {
			if(args.length>1) {
				PropertyContext.environment = args[0];
				PropertyContext.region = args[1];
				if(PropertyContext.region==null || StringUtils.isEmpty(PropertyContext.region) || 
						!(PropertyContext.region.equalsIgnoreCase(Constants.REGION_COR) || PropertyContext.region.equalsIgnoreCase(Constants.REGION_TDA) || 
								PropertyContext.region.equalsIgnoreCase(Constants.REGION_PASCOR) || PropertyContext.region.equalsIgnoreCase(Constants.REGION_PASTDA))) {
					throw new SystemException("Invalid region :"+PropertyContext.region);
				}
				if(args.length>2) {
					try {
						PropertyContext.bpDate = Utils.convertStringToSQLDate(args[2]);
					} catch (ParseException e) {
						PropertyContext.bpDate = Utils.getCurrentDate();
						new BusinessException("Invalid BP date. Proceeding the batch with current system date :"+PropertyContext.bpDate);
					}
				}else {
					PropertyContext.bpDate = Utils.getCurrentDate();
				}
			}else {
				throw new SystemException("Invalid environment and region");
			}
			PropertyContext.readRESTURI();
			SpringApplication.run(Application.class, args);
		}catch(Exception e) {
			Utils.handleException(e);
		}
	}
}
