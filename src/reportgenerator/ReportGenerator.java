package reportgenerator;

import definition.ReportRequestProperties;

public abstract class ReportGenerator implements Runnable {

    protected ReportRequestProperties reportRequestProperties;

    public ReportGenerator(ReportRequestProperties reportRequestProperties) {
        this.reportRequestProperties = reportRequestProperties;
        setThreadName();
    }

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

    // All db , csv related stuff will be done here (maybe not common ones)
    public abstract void generateReport();
    // debug and maintenance purposes forcing
    public void setThreadName() {
        Thread.currentThread().setName(this.getClass().getSimpleName() + "-" + reportRequestProperties.toString());
    }
}
