package queue;

public class ReportQueueProcessScheduler {

    private final LongReportQueueHandler longQueueOrchestrator;
    private final ShortReportQueueHandler shortQueueOrchestrator;

    public ReportQueueProcessScheduler(LongReportQueueHandler longQueueOrchestrator, ShortReportQueueHandler shortQueueOrchestrator) {
        this.longQueueOrchestrator = longQueueOrchestrator;
        this.shortQueueOrchestrator = shortQueueOrchestrator;
    }

    public void doCronJob() {
        //Since their process method starts new thread, this long one will not block short
        longQueueOrchestrator.processReportRequest();
        shortQueueOrchestrator.processReportRequest();
    }
}
