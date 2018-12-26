package com.cft.hogan.platform.ppm.batch.bean;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ImportTaskBean  {

	private String uuid;

	@NotNull (message = "Invalid name")
	private String name;

	private String status;

	private String inputFileName;

	@NotNull (message = "Invalid type")
	private String type;
	
	private String createdBy;

	private String createdOn;
	
	private String modifiedBy;

	private String modifiedOn;
	
}
