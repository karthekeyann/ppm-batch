package com.cft.hogan.platform.ppm.batch.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TemplateBean {
	
	private String uuid;

	private String name;
	
	private List<ParameterBean> psets;

	private String createdBy;

	private String createdOn;

	private String modifiedBy;

	private String modifiedOn;
	
}
