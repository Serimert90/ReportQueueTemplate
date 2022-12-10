package definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportRequestProperties {

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

    public ReportRequestProperties() {}

    public ReportRequestProperties(long waitInTimeUnits, boolean waitTimeInSeconds) {
        this.waitInTimeUnits = waitInTimeUnits;
        this.waitTimeInSeconds = waitTimeInSeconds;
        reportName =  waitTimeInSeconds + "_" + waitInTimeUnits + "_" + new Random().nextInt(100000);
    }

    public ReportRequestProperties(String startDate, String endDate, List<String> siteNames, ReportType reportType, long waitInTimeUnits) {
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

    public ReportRequestProperties deepClone() {
        return new ReportRequestProperties(this.waitInTimeUnits, this.waitTimeInSeconds);
    }

    @Override
    public String toString() {
        return "reportName=" + reportName;
    }
}
