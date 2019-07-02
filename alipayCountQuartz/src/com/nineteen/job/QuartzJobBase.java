package com.nineteen.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public abstract class QuartzJobBase extends QuartzJobBean {

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        this.execute2(jobExecutionContext);
    }
    public abstract void execute2(JobExecutionContext jobExecutionContext);
}
