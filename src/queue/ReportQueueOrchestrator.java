package queue;

import definition.ReportRequestProperties;

import java.util.ArrayList;
import java.util.List;

// This class determines whether long and send queue handler's add queue methods
public class ReportQueueOrchestrator {

    private final LongReportQueueHandler longReportQueueHandler;
    private final ShortReportQueueHandler shortReportQueueHandler;

    public ReportQueueOrchestrator(LongReportQueueHandler longReportQueueHandler, ShortReportQueueHandler shortReportQueueHandler) {
        this.longReportQueueHandler = longReportQueueHandler;
        this.shortReportQueueHandler = shortReportQueueHandler;
        readDb();
        createReportRequests(new ArrayList<>());
    }

    public void readDb(){}
    // this is where we call at the start
    public void createReportRequests(List<ReportRequestProperties> reportRequestPropertiesList) {
        for(ReportRequestProperties reportRequestProperties : reportRequestPropertiesList) {
            createReportRequest(reportRequestProperties);
        }
    }

    // this is where we call when there is a new request
    public void createReportRequest(ReportRequestProperties reportRequestProperties) {
        if(isLongReportRequest(reportRequestProperties)) {
            longReportQueueHandler.addToQueue(reportRequestProperties);
        } else {
            shortReportQueueHandler.addToQueue(reportRequestProperties);
        }
    }

    //this is where we decide whether it is long or short, of course it is sample
    public static boolean isLongReportRequest(ReportRequestProperties reportRequestProperties) {
        return !reportRequestProperties.isWaitTimeInSeconds() || reportRequestProperties.getWaitInTimeUnits() > 30;
    }
}
