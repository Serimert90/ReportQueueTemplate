package definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportRequestParams {

    public enum ReportType {
        OBSERVATION("OBSERVATION"),
        EVENT("EVENT"),
        EVENT_SUMMARY("EVENT_SUMMARY");

        private final String reportTypeName;
        ReportType(String reportTypeName) {
            this.reportTypeName = reportTypeName;
        }

        public String getReportTypeName() {
            return this.reportTypeName;
        }
    }

    private String startDate, endDate;
    private List<String> siteNames = new ArrayList<>();
    private ReportType reportType;


    private long waitInTimeUnits = 0;
    private boolean waitTimeInSeconds = true;
    private String reportName;

    public ReportRequestParams() {}

    public ReportRequestParams(long waitInTimeUnits, boolean waitTimeInSeconds) {
        this.waitInTimeUnits = waitInTimeUnits;
        this.waitTimeInSeconds = waitTimeInSeconds;
        reportName =  waitInTimeUnits + "_" + (waitTimeInSeconds ? "seconds" : "minutes") + "_" + new Random().nextInt(100000);
    }

    public ReportRequestParams(ReportType reportType, long waitInTimeUnits, boolean waitTimeInSeconds) {
        this.reportType = reportType;
        this.waitInTimeUnits = waitInTimeUnits;
        this.waitTimeInSeconds = waitTimeInSeconds;
        reportName = reportType + "_" +  waitInTimeUnits + "_"
                + (waitTimeInSeconds ? "seconds" : "minutes") + "_" + new Random().nextInt(100000);
    }

    public ReportRequestParams(String startDate, String endDate, List<String> siteNames, ReportType reportType, long waitInTimeUnits) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.siteNames = siteNames;
        this.reportType = reportType;
        this.waitInTimeUnits = waitInTimeUnits;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getSiteNames() {
        return siteNames;
    }

    public void setSiteNames(List<String> siteNames) {
        this.siteNames = siteNames;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public long getWaitInTimeUnits() {
        return waitInTimeUnits;
    }

    public void setWaitInTimeUnits(long waitInTimeUnits) {
        this.waitInTimeUnits = waitInTimeUnits;
    }

    public boolean isWaitTimeInSeconds() {
        return waitTimeInSeconds;
    }

    public void setWaitTimeInSeconds(boolean waitTimeInSeconds) {
        this.waitTimeInSeconds = waitTimeInSeconds;
    }

    public ReportRequestParams deepClone() {
        return new ReportRequestParams(this.reportType, this.waitInTimeUnits, this.waitTimeInSeconds);
    }

    @Override
    public String toString() {
        return "reportName=" + reportName;
    }
}
