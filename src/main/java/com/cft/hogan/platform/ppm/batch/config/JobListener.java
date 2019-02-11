package com.cft.hogan.platform.ppm.batch.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.cft.hogan.platform.ppm.batch.context.BatchContext;

@Component
public class JobListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobListener.class);
	public static List<StringBuffer> report = new ArrayList<StringBuffer>();
	
	@Autowired
	Environment env;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		super.beforeJob(jobExecution);
		BatchContext.env = env;
	}
	
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED!");
			BatchContext.logDetails();
			log.info("==============================================================================");
			log.info("**********************************JOB REPORT**********************************");
			log.info("==============================================================================");
			if(report.size()>0) {
				report.forEach(msg->{
					log.info(msg.toString());
				});
			}else {
				log.info("NO TASK HAS BEEN PROCESSED");
			}
			log.info("==============================================================================");
			log.info("*************************************END**************************************");
			log.info("==============================================================================");
		}
	}
}