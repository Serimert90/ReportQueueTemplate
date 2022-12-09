import definition.ReportRequestProperties;
import definition.SystemProperties;
import queue.LongReportQueueHandler;
import queue.ReportQueueOrchestrator;
import queue.ReportQueueProcessScheduler;
import queue.ShortReportQueueHandler;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final ShortReportQueueHandler shortReportQueueHandler = new ShortReportQueueHandler(new SystemProperties());
    private static final LongReportQueueHandler longReportQueueHandler = new LongReportQueueHandler(new SystemProperties());

    private static final ReportQueueOrchestrator reportQueueOrchestrator =
            new ReportQueueOrchestrator(longReportQueueHandler, shortReportQueueHandler);
    private static final ReportQueueProcessScheduler reportQueueProcessScheduler =
            new ReportQueueProcessScheduler(longReportQueueHandler, shortReportQueueHandler);

    public static void main(String[] args) {

        // this part is simulating cron scheduled job 10 sec
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                reportQueueProcessScheduler.doCronJob();
            }
        };
        timer.scheduleAtFixedRate(timerTask,1, 10000);

        // this part is checking remaining not started queueList and finishing if all finished
        Timer timer2 = new Timer();
        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                List<ReportRequestProperties> notStartedList =  reportQueueOrchestrator.getAllNotStartedReportRequestQueueList();
                if (!notStartedList.isEmpty()) {
                    System.out.println("Not started all queue size - list: "
                            + notStartedList.size() + " - " + notStartedList);
                } else {
                    System.out.println("There are no report requests in queue");

                    if(shortReportQueueHandler.getInProgressReportRequestCount() == 0
                    && longReportQueueHandler.getInProgressReportRequestCount() == 0) {
                        System.out.println("All simulated jobs are finished");
                        System.exit(0);
                    }
                }
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
        return new ReportRequestProperties(waitInTimeUnits, true);
    }

    private static ReportRequestProperties generateLongReportRequest() {
        Random randomGenerator = new Random();
        // 5 minutes or less
        long waitInTimeUnits = randomGenerator.nextInt(3) + 1;
        return new ReportRequestProperties(waitInTimeUnits, false);
    }

    public static void sendRequestsToCreateReportRequestOrchestrator(List<ReportRequestProperties> reportRequestPropertiesList) {
        for (ReportRequestProperties reportRequestProperties : reportRequestPropertiesList) {
            reportQueueOrchestrator.createReportRequest(reportRequestProperties);
        }
    }
}