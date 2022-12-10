package reportgenerator;

import definition.ReportRequestParams;
import queue.ReportQueueManager;

public abstract class ReportGenerator implements Runnable {

    protected ReportRequestParams reportRequestParams;
    protected String threadName;

    public ReportGenerator(ReportRequestParams reportRequestParams) {
        this.reportRequestParams = reportRequestParams;
        setThreadName();
    }
    public ReportRequestParams getReportRequestParams() {
        return this.reportRequestParams;
    }
    @Override
    public void run() {
        try {
            generateReport();
        } catch (Exception ex) {

        } finally {

        }
    }
    private void setThreadName() {
        String shortnessValue = ReportQueueManager.isLongReportRequest(reportRequestParams) ? "(LONG)" : "(SHORT)";
        threadName = this.getClass().getSimpleName() + shortnessValue + " - " + reportRequestParams.toString();
        Thread.currentThread().setName(threadName);
    }

    // All db , csv related stuff will be done here (maybe not common ones)
    public abstract void generateReport();
}
