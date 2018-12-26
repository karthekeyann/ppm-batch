package com.cft.hogan.platform.ppm.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.cft.hogan.platform.ppm.batch.bean.ScheduleBatchBean;

public class ImportExportTaskProcessor implements ItemProcessor<ScheduleBatchBean, ScheduleBatchBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportTaskProcessor.class);

    @Override
    public ScheduleBatchBean process(ScheduleBatchBean item) throws Exception {
        LOGGER.info("Processor");
        return item;
    }
}
