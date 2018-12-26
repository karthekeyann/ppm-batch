package com.cft.hogan.platform.ppm.batch.bean;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ParameterBean {
	
	@NotNull (message = "Invalid number")
	private String number;
	
	private String name;
	
	private String companyID;
	
	@NotNull (message = "Invalid applicationID")
	private String applicationID;
	
	private String effectiveDate;

}
