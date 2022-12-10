package reportgenerator;

import definition.ReportRequestProperties;
import utils.CsvUtils;
import utils.DbUtils;
import utils.LogUtils;

public class ObservationReportGenerator extends ReportGenerator {

    public ObservationReportGenerator(ReportRequestProperties reportRequestProperties) {
        super(reportRequestProperties);
    }

    @Override
    public void generateReport() {
        LogUtils.log("ObservationReportGenerator Started processing: " + reportRequestProperties);

        // assume DbUtils prefix methods updating cache too for convenience
        DbUtils.doJob(1, true); // update report request in progress
        DbUtils.doJob(reportRequestProperties.getWaitInTimeUnits(), reportRequestProperties.isWaitTimeInSeconds()); // get data from db for report
        CsvUtils.doCreateCsv(reportRequestProperties.isWaitTimeInSeconds() ? 1 : 5); // create csv
        DbUtils.doJob(1, true); // update report request completed progress

        LogUtils.log("ObservationReportGenerator Finished processing: " + reportRequestProperties);
    }
}
