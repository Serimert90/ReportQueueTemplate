package queue;

public class ReportQueueProcessScheduler {

    private final LongReportQueueHandler longQueueOrchestrator;
    private final ShortReportQueueHandler shortQueueOrchestrator;

    public ReportQueueProcessScheduler(LongReportQueueHandler longQueueOrchestrator, ShortReportQueueHandler shortQueueOrchestrator) {
        this.longQueueOrchestrator = longQueueOrchestrator;
        this.shortQueueOrchestrator = shortQueueOrchestrator;
    }

    public void doCronJob() {

    }
}
