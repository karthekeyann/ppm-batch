package com.cft.hogan.platform.ppm.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.cft.hogan.platform.ppm.batch.bean.ScheduleBatchBean;

public class ImportExportTaskProcessor implements ItemProcessor<ScheduleBatchBean, ScheduleBatchBean> {
    @Override
    public ScheduleBatchBean process(ScheduleBatchBean item) throws Exception {
        return item;
    }
}
