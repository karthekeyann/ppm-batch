package com.cft.hogan.platform.ppm.batch.writer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cft.hogan.platform.ppm.batch.bean.ExportTaskBean;
import com.cft.hogan.platform.ppm.batch.bean.ImportTaskBean;
import com.cft.hogan.platform.ppm.batch.bean.ScheduleBatchBean;
import com.cft.hogan.platform.ppm.batch.config.JobCompletionNotificationListener;
import com.cft.hogan.platform.ppm.batch.context.SystemContext;
import com.cft.hogan.platform.ppm.batch.exception.SystemException;
import com.cft.hogan.platform.ppm.batch.util.Constants;
import com.cft.hogan.platform.ppm.batch.util.Utils;

public class ImportExportTaskWriter implements ItemWriter<ScheduleBatchBean> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportTaskWriter.class);
	StringBuffer msg = null;
	@Override
	public void write(List<? extends ScheduleBatchBean> items) throws Exception {
		items.forEach(task -> {
			LOGGER.info("Processing Task ID:"+task.getUuid()+" --Name:"+task.getName()+" --Type:"+task.getType());
			msg = new StringBuffer();
			JobCompletionNotificationListener.report.add(msg);
			msg.append("TaskID:").append(task.getUuid());
			if(task.getType().equalsIgnoreCase("import")){
				try {
					importTask(task);
					task.setTemplateUUID(null);
					updateSchedule(task);
				} catch (Exception e) {
					msg.append("	--Type:IMPORT	--Result:FAILED");
					LOGGER.error(e.getMessage(), e);
				}
			}else {
				try {
					exportTask(task);
					updateSchedule(task);
				} catch (Exception e) {
					msg.append("	--Type:EXPORT	--Result:FAILED");
					LOGGER.error(e.getMessage(), e);
				}
			}
			msg.append(" --Frequency:").append(task.getFrequency())
			.append("	--Name:").append(task.getName());
		});
	}


	private void importTask(ScheduleBatchBean bean) {
		StringBuffer uri = new StringBuffer();
		uri.append(SystemContext.restURI.getProperty("import.uri")).
		append("?taskType=batch&taskName=").
		append(bean.getName()).
		append("-BATCH");
		RestTemplate restTemplate = new RestTemplate();
		File file = new File(bean.getFilePath());
		if(!file.exists()) {
			throw new SystemException("Invalid input file :"+bean.getFilePath());
		}
		HttpHeaders headers =Utils.getHeader(bean.getCreatedBy());
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		FileSystemResource fileResource = new FileSystemResource(file);

		MultiValueMap<String, Object> body  = new LinkedMultiValueMap<>();
		body.add("file",fileResource );

		HttpEntity<MultiValueMap<String, Object>> requestEntity	 = new HttpEntity<>(body, headers);

		ResponseEntity<ImportTaskBean> result = restTemplate.postForEntity(uri.toString(), requestEntity, ImportTaskBean.class);

		if(Constants.COMPLETE.equalsIgnoreCase(result.getBody().getStatus())) {
			msg.append("	--Type:IMPORT	--Result:SUCCESSFUL");
			LOGGER.info("PCD Imported successfully. ImportTask ID :"+result.getBody().getUuid() +" Name :"+result.getBody().getName()+" Status :"+result.getBody().getStatus());
		}else {
			msg.append("	--Type:IMPORT	--Result:PROCESSED WITH ERRORS");
			LOGGER.info("URI:"+uri.toString());
			LOGGER.info("PCD Imported created with errors. ImportTask ID :"+result.getBody().getUuid() +" Name :"+result.getBody().getName()+" Status :"+result.getBody().getStatus());
		}
	}


	private void exportTask(ScheduleBatchBean bean) throws IOException {

		String filename = bean.getFilePath();
		File file = new File(filename);
		if(!filename.endsWith(".xls")) {
			throw new SystemException("Invalid File location/format."+filename);
		}

		int count = 1;
		while(file.exists()) {
			filename = bean.getFilePath().replace(".xls", "_"+Utils.getCurrentDate()+"("+count+").xls");
			file = new File(filename);
			count++;
		}
		FileOutputStream outputStream = new FileOutputStream(filename);

		ExportTaskBean input = new ExportTaskBean();
		input.setPsets(bean.getTemplate().getPsets());
		input.setSingleTab(bean.isSingleTab());
		String uri = SystemContext.restURI.getProperty("export.uri");
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers =Utils.getHeader(bean.getCreatedBy());
		
		HttpEntity<ExportTaskBean> requestEntity	 = new HttpEntity<ExportTaskBean>(input, headers);
		ResponseEntity<byte[]> result = restTemplate.postForEntity(uri, requestEntity, byte[].class);
		if(result!=null && result.getStatusCodeValue() == 200 && result.getBody()!=null && result.getBody().length>0)
		{
			ByteArrayInputStream inputStream = new ByteArrayInputStream(result.getBody());
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			workbook.write(outputStream);
			inputStream.close();
			workbook.close();
			outputStream.close();
			msg.append("	--Type:EXPORT	--Result:SUCCESSFUL");
		}else {
			msg.append("	--Type:EXPORT	--Result:PROCESSED WITH ERRORS");
		}

	}

	private void updateSchedule(ScheduleBatchBean task) throws Exception {
		StringBuffer url = new StringBuffer();
		try {
		if("ONLY ONCE".equalsIgnoreCase(task.getFrequency())) {
			url.append(SystemContext.restURI.getProperty("schedule.uri")).append("/").append(task.getUuid());
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers =Utils.getHeader(task.getCreatedBy());

			task.setStatus("InActive");
			task.setRemarks("Task with frequency ONLY ONCE processed by batch and status set to in-active");

			HttpEntity<ScheduleBatchBean> requestEntity	 = new HttpEntity<ScheduleBatchBean>(task, headers);
			ResponseEntity<ScheduleBatchBean> result = restTemplate.exchange(url.toString(), HttpMethod.PUT, requestEntity, ScheduleBatchBean.class);
			if(result.getStatusCode()==HttpStatus.OK) {
				LOGGER.info("(ONLY ONCE)Schedule task status set to in active after process.");
			}
		}
		}catch (Exception e) {
			msg.append("--ERROR OCCURED WHILE SCHEDULE TASK STATUS UPDATE--");
			LOGGER.info("URI:"+url.toString());
			Utils.handleException(e);
		}
	}
}
