package queue;

import definition.ReportRequestProperties;
import definition.SystemProperties;
import utils.CsvUtils;
import utils.DbUtils;
import utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractReportQueueHandler {

    private final Queue<ReportRequestProperties> queueList = new ConcurrentLinkedQueue<>();
    protected final AtomicInteger currentRunningCount = new AtomicInteger(0);
    protected SystemProperties systemProperties;

    public AbstractReportQueueHandler(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
    }

    public void addToQueue(ReportRequestProperties reportRequestProperties) {
        addManyToQueue(Collections.singletonList(reportRequestProperties));
    }

    public void addManyToQueue(List<ReportRequestProperties> reportRequestPropertiesList) {
        queueList.addAll(reportRequestPropertiesList);
        processReportRequest();
    }

    public ReportRequestProperties takeFromQueue() {
        return queueList.poll();
    }

    public void removeFromQueue(ReportRequestProperties reportRequestProperties) {
        queueList.remove(reportRequestProperties);
    }

    public void processReportRequest() {

        //this below two ifs are just for logging and maintenance
        if (queueList.isEmpty()) {
            log("Queue list is empty");
            return;
        }

        if (!haveAvailableThreadForProcessingReportRequest()) {
            log("Not available thread, running all ");
            return;
        }

        while (haveAvailableThreadForProcessingReportRequest() && !queueList.isEmpty()) {
            ReportRequestProperties reportRequestProperties = takeFromQueue();
            if (reportRequestProperties == null) { // in very rare cases this can be null (queue.poll()) so extra check
                break;
            }

            currentRunningCount.incrementAndGet();
            new Thread(() -> {
                try {
                    log("Started processing: " + reportRequestProperties);

                    // assume DbUtils prefix methods updating cache too for convenience
                    DbUtils.doJob(1, true); // update report request in progress
                    DbUtils.doJob(reportRequestProperties.getWaitInTimeUnits(), reportRequestProperties.isWaitTimeInSeconds()); // get data from db for report
                    CsvUtils.doCreateCsv(reportRequestProperties.isWaitTimeInSeconds() ? 1 : 5); // create csv
                    DbUtils.doJob(1, true); // update report request completed progress
                } catch (Exception e) {
                    DbUtils.doJob(1, true); // update report request failed
                } finally {
                    currentRunningCount.decrementAndGet();
                    processReportRequest();

                    log("Finished processing: " + reportRequestProperties);
                }
            }).start();
        }
    }

    //This method is just here to see which queue type handler is working for what report request
    public void log(String message){
        String allMessage;
        if (this instanceof LongReportQueueHandler) {
            allMessage = "LONG--> " + message;
        } else {
            allMessage = "SHORT--> " + message;
        }
        LogUtils.log(allMessage);
    }

    public int getInProgressReportRequestCount() {
        return this.currentRunningCount.get();
    }

    public List<ReportRequestProperties> getNotStartedReportRequestQueueList() {
        List<ReportRequestProperties> clonedReportRequestPropertiesList = new ArrayList<>();
        for(ReportRequestProperties reportRequestProperties : queueList) {
            clonedReportRequestPropertiesList.add(reportRequestProperties.deepClone());
        }
        return clonedReportRequestPropertiesList;
    }

    public void updateSystemProperties(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
    }

    protected abstract boolean haveAvailableThreadForProcessingReportRequest();
}
