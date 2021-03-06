package com.cft.hogan.platform.ppm.batch.reader;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cft.hogan.platform.ppm.batch.bean.ScheduleBatchBean;
import com.cft.hogan.platform.ppm.batch.context.PropertyContext;
import com.cft.hogan.platform.ppm.batch.exception.BusinessException;
import com.cft.hogan.platform.ppm.batch.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public  class ScheduledImportTaskReader implements ItemReader<ScheduleBatchBean> {

	private int nextTaskID = 0;
	private List<ScheduleBatchBean> importTasks;

	@Override
	public ScheduleBatchBean read() throws Exception {
		if (importTasks == null) {
			getScheduledImportTasks();
		}

		ScheduleBatchBean nextTask = null;

		if (importTasks!=null && nextTaskID < importTasks.size()) {
			nextTask = importTasks.get(nextTaskID);
			nextTaskID++;
		}
		return nextTask;
	}

	private void getScheduledImportTasks() {
		StringBuffer uri = new StringBuffer(PropertyContext.restURI.getProperty("schedule.uri")+"/batch");

		if(PropertyContext.bpDate != null) {
			uri.append("?bp-date=").append(PropertyContext.bpDate);
			uri.append("&type=").append("Import");
		}else {
			uri.append("?type=").append("Import");
		}
		
		HttpHeaders headers =Utils.getHeader("batch");
		
		HttpEntity<Object> requestEntity	 = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ScheduleBatchBean[]> response = null;
		try {
			response = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestEntity, ScheduleBatchBean[].class);
			importTasks = Arrays.asList(response.getBody());
		}catch(Exception e) {
			new BusinessException("Error getting the Scheduled Export tasks. Proceeding with batch.");
			log.error("Job Parameters :--region :"+PropertyContext.region+"  --BP Date :"+PropertyContext.bpDate);
			log.error(e.getMessage(), e);
		}
	}
}
