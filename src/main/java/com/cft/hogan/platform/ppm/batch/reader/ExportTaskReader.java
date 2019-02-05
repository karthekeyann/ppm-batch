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
import com.cft.hogan.platform.ppm.batch.context.EnvironmentContext;
import com.cft.hogan.platform.ppm.batch.exception.BusinessException;
import com.cft.hogan.platform.ppm.batch.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportTaskReader implements ItemReader<ScheduleBatchBean> {

	private int nextTaskID = 0;
	private List<ScheduleBatchBean> exportTasks;

	@Override
	public ScheduleBatchBean read() throws Exception {
		if (exportTasks == null) {
			getScheduledImportTasks();
		}

		ScheduleBatchBean nextTask = null;

		if (exportTasks != null && nextTaskID < exportTasks.size()) {
			nextTask = exportTasks.get(nextTaskID);
			nextTaskID++;
		}
		return nextTask;
	}

	private void getScheduledImportTasks() {
		StringBuffer uri = new StringBuffer(EnvironmentContext.getScheduleApiURI()+"/batch");

		if(EnvironmentContext.bpDate != null) {
			uri.append("?bp-date=").append(EnvironmentContext.bpDate);
			uri.append("&type=").append("Export");
		}else {
			uri.append("?type=").append("Export");
		}
		HttpHeaders headers =Utils.getHeader("batch");
		
		HttpEntity<Object> requestEntity	 = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ScheduleBatchBean[]> response = null;
		try {
			response = restTemplate.exchange(uri.toString(), HttpMethod.GET, requestEntity, ScheduleBatchBean[].class);
			exportTasks = Arrays.asList(response.getBody());
		}catch(Exception e) {
			new BusinessException("Error getting the Scheduled Export tasks. Proceeding with batch.");
			log.error("URI:"+uri.toString());
			log.error(e.getMessage(), e);
		}
	}
}
