package com.cft.hogan.platform.ppm.batch.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import com.cft.hogan.platform.ppm.batch.context.PropertyContext;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
	public static List<StringBuffer> report = new ArrayList<StringBuffer>();

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED!");
			log.info("==============================================================================");
			log.info("**********************************JOB REPORT**********************************");
			log.info("==============================================================================");
			PropertyContext.logDetails();
			report.forEach(msg->{
				log.info(msg.toString());
			});
			log.info("==============================================================================");
			log.info("*************************************END**************************************");
			log.info("==============================================================================");
		}
	}
}