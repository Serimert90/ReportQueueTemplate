package reportgenerator;

import definition.ReportRequestParams;
import utils.CsvUtils;
import utils.DbUtils;
import utils.LogUtils;

public class EventReportGenerator extends AbstractReportGenerator {

    public EventReportGenerator(ReportRequestParams reportRequestParams) {
        super(reportRequestParams);
    }

    @Override
    public void generateReport() {
        LogUtils.log("EventReportGenerator Started processing: " + reportRequestParams);

        // assume DbUtils prefix methods updating cache too for convenience
        DbUtils.doJob(1, true); // update report request in progress
        DbUtils.doJob(reportRequestParams.getWaitInTimeUnits(), reportRequestParams.isWaitTimeInSeconds()); // get data from db for report
        CsvUtils.doCreateCsv(reportRequestParams.isWaitTimeInSeconds() ? 1 : 5); // create csv
        DbUtils.doJob(1, true); // update report request completed progress

        LogUtils.log("EventReportGenerator Finished processing: " + reportRequestParams);
    }

}
