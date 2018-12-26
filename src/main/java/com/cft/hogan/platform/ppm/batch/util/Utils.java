package com.cft.hogan.platform.ppm.batch.util;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import com.cft.hogan.platform.ppm.batch.context.PropertyContext;
import com.cft.hogan.platform.ppm.batch.exception.BusinessException;
import com.cft.hogan.platform.ppm.batch.exception.SystemException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {

	public static java.sql.Date convertStringToSQLDate(String str_date) throws ParseException {
		DateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = (Date) formatter.parse(str_date);
		return  new java.sql.Date(date.getTime());
	}

	
	public static java.sql.Date getCurrentDate() {
		return  new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	}
	
	public static String getTimeStampForFileName() {
		DateFormat formatter = new SimpleDateFormat(Constants.YYYY_MM_DD_HHMMSS);
		return formatter.format(new Date());
	}
	
	public static HttpHeaders getHeader(String user) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-region", PropertyContext.region);
		headers.set("X-user", user);
		headers.set("Authorization", getAuth());
		return headers;
	}
	
	private static String getAuth() {
		 String auth = PropertyContext.restURI.getProperty("ppm.batch.user") + ":" + PropertyContext.restURI.getProperty("ppm.batch.password");
         byte[] encodedAuth = Base64.encodeBase64(
        		 auth.getBytes(Charset.forName("US-ASCII")) );
         String authHeader = "Basic " + new String( encodedAuth );
         return authHeader;
	}
	
	
	public static void handleException(Exception e) {
		StringBuffer msg = new StringBuffer();
		msg.append(" --Region :").append(PropertyContext.region).append(e.getMessage());
		log.error(msg.toString(), e);
		if(e instanceof BusinessException) {
			//DO NOTHING
		}else {
			throw new SystemException();
		}
	}
}