package queue;

import definition.ReportRequestProperties;
import definition.SystemProperties;
import reportgenerator.EventReportGenerator;
import reportgenerator.ObservationReportGenerator;
import reportgenerator.ReportGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractReportQueueHandler {

    protected SystemProperties systemProperties;
    private ExecutorService executorService;

    public AbstractReportQueueHandler(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
        initThreadExecutor();
    }

    public void addToQueue(ReportRequestProperties reportRequestProperties) {
        executorService.submit(getRelatedReportGenerator(reportRequestProperties));
    }

    public void addManyToQueue(List<ReportRequestProperties> reportRequestPropertiesList) {
        List<ReportGenerator> reportGenerators = getRelatedReportGenerators(reportRequestPropertiesList);
        reportGenerators.forEach( reportGenerator -> executorService.submit(reportGenerator));
    }

    public List<ReportGenerator> getRelatedReportGenerators(List<ReportRequestProperties> reportRequestPropertiesList) {
        List<ReportGenerator> reportGenerators = new ArrayList<>();
        for (ReportRequestProperties reportRequestProperties : reportRequestPropertiesList) {
            reportGenerators.add(getRelatedReportGenerator(reportRequestProperties));
        }
        return reportGenerators;
    }

    public ReportGenerator getRelatedReportGenerator(ReportRequestProperties reportRequestProperties) {
        if (reportRequestProperties.getReportType() == ReportRequestProperties.ReportType.OBSERVATION) {
            return new ObservationReportGenerator(reportRequestProperties);
        } else if(reportRequestProperties.getReportType() == ReportRequestProperties.ReportType.EVENT) {
            return new EventReportGenerator(reportRequestProperties);
        } else {
            throw new RuntimeException("not defined");
        }
    }

    public void updateSystemProperties(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
        executorService.shutdown();
    }

    protected void initThreadExecutor() {
        int maxParallelRunningThreadCount = getMaxParallelRunningSize(systemProperties);
        int maxQueueSize = getMaxQueueListSize(systemProperties);
        executorService = new ThreadPoolExecutor(maxParallelRunningThreadCount, maxParallelRunningThreadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(maxQueueSize));
    }

    protected int getMaxParallelRunningSize(SystemProperties systemProperties) {
        return (this instanceof ShortReportQueueHandler)
                ? systemProperties.getMaxShortQueueParallelProcessingCount()
                : systemProperties.getMaxLongQueueProcessingCount();
    }

    protected int getMaxQueueListSize(SystemProperties systemProperties) {
        return (this instanceof ShortReportQueueHandler)
                ? systemProperties.getMaxShortQueueListSize()
                : systemProperties.getMaxLongQueueListSize();
    }
}
