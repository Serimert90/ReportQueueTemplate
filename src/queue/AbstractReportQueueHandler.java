package queue;

import definition.ReportRequestProperties;
import definition.SystemProperties;
import reportgenerator.EventReportGenerator;
import reportgenerator.ObservationReportGenerator;
import reportgenerator.ReportGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractReportQueueHandler {

    private ThreadPoolExecutor executorService;

    protected SystemProperties systemProperties;

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

    public int getActiveThreadCount() {
        return executorService.getActiveCount();
    }

    public int getQueueSize() {
        return executorService.getQueue().size();
    }

    public long getTotalCompletedSize() {
        return executorService.getCompletedTaskCount();
    }

    public int getRemainingQueueCapacity() {
        return executorService.getQueue().remainingCapacity();
    }

    public List<ReportRequestProperties> getWaitingReportRequests() {
        List<ReportRequestProperties> waitingReportRequestPropertiesList = new ArrayList<>();

        BlockingQueue<Runnable> waitingReportGenerators = executorService.getQueue();
        for (Runnable runnable : waitingReportGenerators) {
            if (runnable instanceof ReportGenerator) {
                waitingReportRequestPropertiesList.add(
                        ((ReportGenerator)runnable).getReportRequestProperties().deepClone());
            }
        }

        return waitingReportRequestPropertiesList;
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
