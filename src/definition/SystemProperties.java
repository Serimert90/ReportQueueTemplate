package definition;

public class SystemProperties {

    private int maxShortQueueParallelProcessingCount = 3;
    private int maxShortQueueListSize = 100;

    private int maxLongQueueProcessingCount = 3;
    private int maxLongQueueListSize = 100;

    public SystemProperties() {}

    public SystemProperties(int maxShortQueueParallelProcessingCount, int maxLongQueueProcessingCount) {
        this.maxShortQueueParallelProcessingCount = maxShortQueueParallelProcessingCount;
        this.maxLongQueueProcessingCount = maxLongQueueProcessingCount;
    }

    public SystemProperties(int maxShortQueueParallelProcessingCount, int maxShortQueueListSize, int maxLongQueueProcessingCount, int maxLongQueueListSize) {
        this.maxShortQueueParallelProcessingCount = maxShortQueueParallelProcessingCount;
        this.maxShortQueueListSize = maxShortQueueListSize;
        this.maxLongQueueProcessingCount = maxLongQueueProcessingCount;
        this.maxLongQueueListSize = maxLongQueueListSize;
    }

    public int getMaxShortQueueParallelProcessingCount() {
        return maxShortQueueParallelProcessingCount;
    }

    public void setMaxShortQueueParallelProcessingCount(int maxShortQueueParallelProcessingCount) {
        this.maxShortQueueParallelProcessingCount = maxShortQueueParallelProcessingCount;
    }

    public int getMaxLongQueueProcessingCount() {
        return maxLongQueueProcessingCount;
    }

    public void setMaxLongQueueProcessingCount(int maxLongQueueProcessingCount) {
        this.maxLongQueueProcessingCount = maxLongQueueProcessingCount;
    }

    public int getMaxShortQueueListSize() {
        return maxShortQueueListSize;
    }

    public void setMaxShortQueueListSize(int maxShortQueueListSize) {
        this.maxShortQueueListSize = maxShortQueueListSize;
    }

    public int getMaxLongQueueListSize() {
        return maxLongQueueListSize;
    }

    public void setMaxLongQueueListSize(int maxLongQueueListSize) {
        this.maxLongQueueListSize = maxLongQueueListSize;
    }
}
