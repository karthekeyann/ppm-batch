package com.cft.hogan.platform.ppm.batch.bean;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ImportTaskBean  {

	private String uuid;

	private String name;

	private String status;

	private String inputFileName;

	private String type;
	
	private String createdBy;

	private String createdOn;
	
	private String modifiedBy;

	private String modifiedOn;
	
}
