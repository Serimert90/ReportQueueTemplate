package reportgenerator;

import definition.ReportRequestProperties;

public abstract class ReportGenerator implements Runnable {

    protected ReportRequestProperties reportRequestProperties;

    public ReportGenerator(ReportRequestProperties reportRequestProperties) {
        this.reportRequestProperties = reportRequestProperties;
    }
}
