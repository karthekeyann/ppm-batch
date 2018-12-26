package com.cft.hogan.platform.ppm.batch.bean;

import java.util.List;

import lombok.Data;


@Data
public class ExportTaskBean {
	
	private List<ParameterBean> psets;
	
	private Boolean singleTab = false;

}
