package com.cft.hogan.platform.ppm.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cft.hogan.platform.ppm.batch.bean.ScheduleBatchBean;
import com.cft.hogan.platform.ppm.batch.processor.ImportExportTaskProcessor;
import com.cft.hogan.platform.ppm.batch.reader.ScheduledExportTaskReader;
import com.cft.hogan.platform.ppm.batch.reader.ScheduledImportTaskReader;
import com.cft.hogan.platform.ppm.batch.writer.ImportExportTaskWriter;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<ScheduleBatchBean> importTaskReader() {
        return new ScheduledImportTaskReader();
    }
    
    @Bean
    public ItemReader<ScheduleBatchBean> exportTaskReader() {
        return new ScheduledExportTaskReader();
    }
    
    @Bean
    public ImportExportTaskProcessor importExportTaskProcessor() {
        return new ImportExportTaskProcessor();
    }


    @Bean
    public ItemWriter<ScheduleBatchBean> importExportTaskWriter() {
        return new ImportExportTaskWriter();
    }
    
    @Bean
    public Job massMaintenanceJob(JobCompletionNotificationListener listener, Step importStep, Step exportStep) {
        return jobBuilderFactory.get("massMaintenanceJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(importStep)
//            .flow(exportStep)
            .next(exportStep)
            .end()
            .build();
    }

    @Bean
    public Step importStep(ItemWriter<ScheduleBatchBean> writer) {
        return stepBuilderFactory.get("importStep")
                .<ScheduleBatchBean, ScheduleBatchBean>chunk(1)
                .reader(importTaskReader())
                .processor(importExportTaskProcessor())
                .writer(importExportTaskWriter())
                .build();
    }
    
    @Bean
    public Step exportStep(ItemWriter<ScheduleBatchBean> writer) {
        return stepBuilderFactory.get("exportStep")
                .<ScheduleBatchBean, ScheduleBatchBean>chunk(1)
                .reader(exportTaskReader())
                .processor(importExportTaskProcessor())
                .writer(importExportTaskWriter())
                .build();
    }

}
