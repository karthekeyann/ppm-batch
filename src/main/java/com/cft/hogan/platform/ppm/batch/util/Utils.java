package com.cft.hogan.platform.ppm.batch.util;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import com.cft.hogan.platform.ppm.batch.context.BatchContext;

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
		headers.set("X-region", BatchContext.getRegion());
		headers.set("X-user", user);
		headers.set("Authorization", getAuth());
		return headers;
	}
	
	private static String getAuth() {
		 String auth = BatchContext.getBatchUserId() + ":" + BatchContext.getBatchPassword();
         byte[] encodedAuth = Base64.encodeBase64(
        		 auth.getBytes(Charset.forName("US-ASCII")) );
         String authHeader = "Basic " + new String( encodedAuth );
         return authHeader;
	}
}