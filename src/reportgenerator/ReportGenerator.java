package reportgenerator;

import definition.ReportRequestProperties;
import queue.ReportQueueManager;

public abstract class ReportGenerator implements Runnable {

    protected ReportRequestProperties reportRequestProperties;
    protected String threadName;

    public ReportGenerator(ReportRequestProperties reportRequestProperties) {
        this.reportRequestProperties = reportRequestProperties;
        setThreadName();
    }

    // All db , csv related stuff will be done here (maybe not common ones)
    public abstract void generateReport();

    public ReportRequestProperties getReportRequestProperties() {
        return this.reportRequestProperties;
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
        String shortnessValue = ReportQueueManager.isLongReportRequest(reportRequestProperties) ? "(LONG)" : "(SHORT)";
        threadName = this.getClass().getSimpleName() + shortnessValue + " - " + reportRequestProperties.toString();
        Thread.currentThread().setName(threadName);
    }
}
