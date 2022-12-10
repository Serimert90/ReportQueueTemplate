package definition;

public class SystemProperties {

    private int maxShortQueueProcessingCount = 2;
    private int maxLongQueueProcessingCount = 2;

    public SystemProperties() {}

    public SystemProperties(int maxShortQueueProcessingCount, int maxLongQueueProcessingCount) {
        this.maxShortQueueProcessingCount = maxShortQueueProcessingCount;
        this.maxLongQueueProcessingCount = maxLongQueueProcessingCount;
    }

    public int getMaxShortQueueProcessingCount() {
        return maxShortQueueProcessingCount;
    }

    public void setMaxShortQueueProcessingCount(int maxShortQueueProcessingCount) {
        this.maxShortQueueProcessingCount = maxShortQueueProcessingCount;
    }

    public int getMaxLongQueueProcessingCount() {
        return maxLongQueueProcessingCount;
    }

    public void setMaxLongQueueProcessingCount(int maxLongQueueProcessingCount) {
        this.maxLongQueueProcessingCount = maxLongQueueProcessingCount;
    }
}
