package queue;

import definition.SystemProperties;

public class ShortReportQueueHandler extends AbstractReportQueueHandler {

    public ShortReportQueueHandler(SystemProperties systemProperties) {
        super(systemProperties);
    }

    @Override
    protected boolean haveAvailableThreadForProcessingReportRequest() {
        return systemProperties.getMaxShortQueueProcessingCount() - currentRunningCount.get() > 0;
    }
}
