package utils;

import java.util.concurrent.TimeUnit;

public class DbUtils {

    private DbUtils() {}

    public static void doJob(long timeUnit, boolean isSeconds) {
        if(isSeconds)
            doJobSeconds(timeUnit);
        else {
            doJobMinutes(timeUnit);
        }
    }

    private static void doJobSeconds(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ignored) {}
    }

    private static void doJobMinutes(long minutes) {
        try {
            TimeUnit.MINUTES.sleep(minutes);
        } catch (InterruptedException ignored) {}
    }
}
