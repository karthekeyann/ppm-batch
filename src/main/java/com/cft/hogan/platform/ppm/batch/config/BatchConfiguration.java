package com.cft.hogan.platform.ppm.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cft.hogan.platform.ppm.batch.bean.ScheduleBatchBean;
import com.cft.hogan.platform.ppm.batch.processor.ImportExportTaskProcessor;
import com.cft.hogan.platform.ppm.batch.reader.ExportTaskReader;
import com.cft.hogan.platform.ppm.batch.reader.ImportTaskReader;
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
        return new ImportTaskReader();
    }
    
    @Bean
    public ItemReader<ScheduleBatchBean> exportTaskReader() {
        return new ExportTaskReader();
    }
    
    @Bean
    public ImportExportTaskProcessor importExportTaskProcessor() {
        return new ImportExportTaskProcessor();
    }


    @Bean
    public ItemWriter<ScheduleBatchBean> importExportTaskWriter() {
        return new ImportExportTaskWriter();
    }
    
//    @Bean
//    public Job massMaintenanceJob(JobListener listener, Step importStep, Step exportStep) {
//        return jobBuilderFactory.get("massMaintenanceJob")
//            .incrementer(new RunIdIncrementer())
//            .listener(listener)
//            .flow(importStep)
//            .next(exportStep)
//            .end()
//            .build();
//    }
    
    @Bean
    public Job massMaintenanceJob(JobListener listener, Step importStep, Step exportStep) {
        return jobBuilderFactory.get("massMaintenanceJob")
            .listener(listener)
            .start(importStep)
            .next(exportStep)
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
