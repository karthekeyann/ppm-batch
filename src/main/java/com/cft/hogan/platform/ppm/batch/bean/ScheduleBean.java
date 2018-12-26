package com.cft.hogan.platform.ppm.batch.bean;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScheduleBean {

	private String uuid;

	private String templateUUID;
	
	private String templateName;

	@NotNull (message = "Invalid name")
	private String name;

	@NotNull (message = "Invalid type")
	private String type;

	@NotNull (message = "Invalid effective date")
	private String effectiveDate;

	@NotNull (message = "Invalid filepath")
	private String filePath;

	@NotNull (message = "Invalid frequency")
	private String frequency;

	private String freqPattern;

	private boolean singleTab; 

	private String status;

	private String  remarks;

	private String createdBy;

	private String createdOn;

	private String modifiedBy;

	private String modifiedOn;
	
}
