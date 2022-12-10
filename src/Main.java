import definition.ReportRequestParams;
import definition.SystemProperties;
import queue.ReportQueueManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final ReportQueueManager reportQueueManager =
            new ReportQueueManager(new SystemProperties());

    public static void main(String[] args) {

        // this part is checking remaining not started queueList and finishing if all finished
        Timer timer2 = new Timer();
        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Current queue size: " + reportQueueManager.getTotalWaitingQueueSize());
                System.out.println("Remaining capacity: " + reportQueueManager.getTotalRemainingQueueCapacity());
            }
        };
        timer2.scheduleAtFixedRate(timerTask2,10000, 10000);

        List<ReportRequestParams> reportRequestParamsList = generateReportRequests(120);
        sendRequestsToCreateReportRequestOrchestrator(reportRequestParamsList);

        try {
            TimeUnit.SECONDS.sleep(50);
            reportRequestParamsList = generateReportRequests(5);
            sendRequestsToCreateReportRequestOrchestrator(reportRequestParamsList);
        } catch (InterruptedException ignored) {}
    }

    public static List<ReportRequestParams> generateReportRequests(int count) {
        Random randomGenerator = new Random();
        List<ReportRequestParams> reportRequestParamsList = new ArrayList<>();

        int totalLong = 0;
        int totalShort = 0;
        while (reportRequestParamsList.size() < count) {
            ReportRequestParams reportRequestParams;

            // below part is for randomly deciding short or long
            boolean shortRequest = randomGenerator.nextBoolean();
            if (shortRequest) {
                reportRequestParams = generateShortReportRequest();
            } else {
                reportRequestParams = generateLongReportRequest();
            }

            if (ReportQueueManager.isLongReportRequest(reportRequestParams)) {
                totalLong++;
            } else {
                totalShort++;
            }
            System.out.println("Generated Report Request: " + reportRequestParams);
            reportRequestParamsList.add(reportRequestParams);
        }
        System.out.println("Total: "+ (totalLong + totalShort) + " - Total Long: "+ totalLong + " - Total Short: " + totalShort);
        return reportRequestParamsList;
    }

    private static ReportRequestParams generateShortReportRequest() {
        Random randomGenerator = new Random();
        // 30 seconds or less
        long waitInTimeUnits = randomGenerator.nextInt(30) + 1;
        ReportRequestParams.ReportType reportType = randomGenerator.nextInt(10) < 5
                ? ReportRequestParams.ReportType.OBSERVATION
                : ReportRequestParams.ReportType.EVENT;
        return new ReportRequestParams(reportType, waitInTimeUnits, true);
    }

    private static ReportRequestParams generateLongReportRequest() {
        Random randomGenerator = new Random();
        // 5 minutes or less
        long waitInTimeUnits = randomGenerator.nextInt(3) + 1;
        ReportRequestParams.ReportType reportType = randomGenerator.nextInt(10) < 5
                ? ReportRequestParams.ReportType.OBSERVATION
                : ReportRequestParams.ReportType.EVENT;
        return new ReportRequestParams(reportType, waitInTimeUnits, false);
    }

    public static void sendRequestsToCreateReportRequestOrchestrator(List<ReportRequestParams> reportRequestParamsList) {
        for (ReportRequestParams reportRequestParams : reportRequestParamsList) {
            reportQueueManager.createReportRequest(reportRequestParams);
        }
    }
}