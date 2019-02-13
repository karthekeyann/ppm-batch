package com.cft.hogan.platform.ppm.batch.bean;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScheduleBean {

	private String uuid;

	private String templateUUID;
	
	private String templateName;

	private String name;

	private String type;

	private String effectiveDate;

	private String filePath;

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
