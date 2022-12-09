package queue;

import definition.SystemProperties;

public class LongReportQueueHandler extends AbstractReportQueueHandler {

    public LongReportQueueHandler(SystemProperties systemProperties) {
        super(systemProperties);
    }

    @Override
    protected boolean haveAvailableThreadForProcessingReportRequest() {
        return systemProperties.getMaxLongQueueProcessingCount() - currentRunningCount.get() > 0;
    }

}
