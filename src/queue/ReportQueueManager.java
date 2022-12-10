package queue;

import definition.ReportRequestParams;
import definition.SystemProperties;

import java.util.ArrayList;
import java.util.List;

// This class determines whether long ,and sending it to queue handlers
public class ReportQueueManager {

    private SystemProperties systemProperties;
    private final ShortReportQueueHandler shortReportQueueHandler;
    private final LongReportQueueHandler longReportQueueHandler;

    public ReportQueueManager(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
        this.longReportQueueHandler = new LongReportQueueHandler(systemProperties);
        this.shortReportQueueHandler = new ShortReportQueueHandler(systemProperties);
        readDb();
        createReportRequests(new ArrayList<>());
    }
    public void readDb(){}
    public void createReportRequests(List<ReportRequestParams> reportRequestParamsList) {
        for(ReportRequestParams reportRequestParams : reportRequestParamsList) {
            createReportRequest(reportRequestParams);
        }
    }
    public void createReportRequest(ReportRequestParams reportRequestParams) {
        if (isLongReportRequest(reportRequestParams)) {
            longReportQueueHandler.addToQueue(reportRequestParams);
        } else {
            shortReportQueueHandler.addToQueue(reportRequestParams);
        }
    }
    public static boolean isLongReportRequest(ReportRequestParams reportRequestParams) {
        return !reportRequestParams.isWaitTimeInSeconds() || reportRequestParams.getWaitInTimeUnits() > 30;
    }

    public int getTotalWaitingQueueSize() {
        return shortReportQueueHandler.getQueueSize() + longReportQueueHandler.getQueueSize();
    }

    public int getTotalRemainingQueueCapacity() {
        return shortReportQueueHandler.getRemainingQueueCapacity() + longReportQueueHandler.getRemainingQueueCapacity();
    }
    public void updateSystemProperties(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
        longReportQueueHandler.updateSystemProperties(systemProperties);
        shortReportQueueHandler.updateSystemProperties(systemProperties);
    }
}
