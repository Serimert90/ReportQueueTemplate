package definition;

public class SystemProperties {

    private int maxShortQueueProcessingCount = 3;
    private int maxLongQueueProcessingCount = 3;

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
