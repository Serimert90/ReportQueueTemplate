package queue;

import definition.ReportRequestParams;
import definition.SystemProperties;
import reportgenerator.EventReportGenerator;
import reportgenerator.ObservationReportGenerator;
import reportgenerator.AbstractReportGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractReportQueueHandler {
    private ThreadPoolExecutor executorService;

    protected SystemProperties systemProperties;

    AbstractReportQueueHandler(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
        initThreadExecutor();
    }
    void updateSystemProperties(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;

        int maxParallelThreadSize = getMaxParallelRunningSize(systemProperties);
        executorService.setCorePoolSize(maxParallelThreadSize);
        executorService.setMaximumPoolSize(maxParallelThreadSize);
    }
    void addToQueue(ReportRequestParams reportRequestParams) {
        executorService.submit(getRelatedReportGenerator(reportRequestParams));
    }
    void addManyToQueue(List<ReportRequestParams> reportRequestParamsList) {
        List<AbstractReportGenerator> abstractReportGenerators = getRelatedReportGenerators(reportRequestParamsList);
        abstractReportGenerators.forEach(abstractReportGenerator -> executorService.submit(abstractReportGenerator));
    }
    List<AbstractReportGenerator> getRelatedReportGenerators(List<ReportRequestParams> reportRequestParamsList) {
        List<AbstractReportGenerator> abstractReportGenerators = new ArrayList<>();
        for (ReportRequestParams reportRequestParams : reportRequestParamsList) {
            abstractReportGenerators.add(getRelatedReportGenerator(reportRequestParams));
        }
        return abstractReportGenerators;
    }
    AbstractReportGenerator getRelatedReportGenerator(ReportRequestParams reportRequestParams) {
        if (reportRequestParams.getReportType() == ReportRequestParams.ReportType.OBSERVATION) {
            return new ObservationReportGenerator(reportRequestParams);
        } else if(reportRequestParams.getReportType() == ReportRequestParams.ReportType.EVENT) {
            return new EventReportGenerator(reportRequestParams);
        } else {
            throw new RuntimeException("not defined");
        }
    }
    protected int getQueueSize() {
        return executorService.getQueue().size();
    }
    protected int getRemainingQueueCapacity() {
        return executorService.getQueue().remainingCapacity();
    }
    protected void initThreadExecutor() {
        int maxParallelRunningThreadCount = getMaxParallelRunningSize(systemProperties);
        int maxQueueSize = getMaxQueueListSize(systemProperties);
        executorService = new ThreadPoolExecutor(maxParallelRunningThreadCount, maxParallelRunningThreadCount,
                0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(maxQueueSize));
    }
    protected int getMaxParallelRunningSize(SystemProperties systemProperties) {
        return (this instanceof ShortReportQueueHandler)
                ? systemProperties.getMaxShortQueueParallelProcessingCount()
                : systemProperties.getMaxLongQueueParallelProcessingCount();
    }
    protected int getMaxQueueListSize(SystemProperties systemProperties) {
        return (this instanceof ShortReportQueueHandler)
                ? systemProperties.getMaxShortQueueListSize()
                : systemProperties.getMaxLongQueueListSize();
    }
}
