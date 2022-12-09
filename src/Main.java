import definition.ReportRequestProperties;
import definition.SystemProperties;
import queue.LongReportQueueHandler;
import queue.ReportQueueOrchestrator;
import queue.ShortReportQueueHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final ShortReportQueueHandler shortReportQueueHandler = new ShortReportQueueHandler(new SystemProperties());
    private static final LongReportQueueHandler longReportQueueHandler = new LongReportQueueHandler(new SystemProperties());

    private static final ReportQueueOrchestrator reportQueueOrchestrator =
            new ReportQueueOrchestrator(longReportQueueHandler, shortReportQueueHandler);

    public static void main(String[] args) {

        // this part is checking remaining not started queueList and finishing if all finished
        Timer timer2 = new Timer();
        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                List<ReportRequestProperties> waitingRequests = shortReportQueueHandler.getWaitingReportRequests();
                waitingRequests.addAll(longReportQueueHandler.getWaitingReportRequests());
                System.out.println("Waiting Report Requests: " + waitingRequests);
            }
        };
        timer2.scheduleAtFixedRate(timerTask2,10000, 10000);

        List<ReportRequestProperties> reportRequestPropertiesList = generateReportRequests(20);
        sendRequestsToCreateReportRequestOrchestrator(reportRequestPropertiesList);

        try {
            TimeUnit.SECONDS.sleep(50);
            reportRequestPropertiesList = generateReportRequests(5);
            sendRequestsToCreateReportRequestOrchestrator(reportRequestPropertiesList);
        } catch (InterruptedException ignored) {}
    }

    public static List<ReportRequestProperties> generateReportRequests(int count) {
        Random randomGenerator = new Random();
        List<ReportRequestProperties> reportRequestPropertiesList = new ArrayList<>();

        int totalLong = 0;
        int totalShort = 0;
        while (reportRequestPropertiesList.size() < count) {
            ReportRequestProperties reportRequestProperties;

            // below part is for randomly deciding short or long
            boolean shortRequest = randomGenerator.nextBoolean();
            if (shortRequest) {
                reportRequestProperties = generateShortReportRequest();
            } else {
                reportRequestProperties = generateLongReportRequest();
            }

            if (ReportQueueOrchestrator.isLongReportRequest(reportRequestProperties)) {
                totalLong++;
            } else {
                totalShort++;
            }
            System.out.println("Generated Report Request: " + reportRequestProperties);
            reportRequestPropertiesList.add(reportRequestProperties);
        }
        System.out.println("Total: "+ (totalLong + totalShort) + " - Total Long: "+ totalLong + " - Total Short: " + totalShort);
        return reportRequestPropertiesList;
    }

    private static ReportRequestProperties generateShortReportRequest() {
        Random randomGenerator = new Random();
        // 30 seconds or less
        long waitInTimeUnits = randomGenerator.nextInt(30) + 1;
        ReportRequestProperties.ReportType reportType = randomGenerator.nextInt(10) < 5
                ? ReportRequestProperties.ReportType.OBSERVATION
                : ReportRequestProperties.ReportType.EVENT;
        return new ReportRequestProperties(reportType, waitInTimeUnits, true);
    }

    private static ReportRequestProperties generateLongReportRequest() {
        Random randomGenerator = new Random();
        // 5 minutes or less
        long waitInTimeUnits = randomGenerator.nextInt(3) + 1;
        ReportRequestProperties.ReportType reportType = randomGenerator.nextInt(10) < 5
                ? ReportRequestProperties.ReportType.OBSERVATION
                : ReportRequestProperties.ReportType.EVENT;
        return new ReportRequestProperties(reportType, waitInTimeUnits, false);
    }

    public static void sendRequestsToCreateReportRequestOrchestrator(List<ReportRequestProperties> reportRequestPropertiesList) {
        for (ReportRequestProperties reportRequestProperties : reportRequestPropertiesList) {
            reportQueueOrchestrator.createReportRequest(reportRequestProperties);
        }
    }
}