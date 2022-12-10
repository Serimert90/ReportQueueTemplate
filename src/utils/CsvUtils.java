package utils;

import java.util.concurrent.TimeUnit;

public class CsvUtils {

    private CsvUtils() {}

    public static void doCreateCsv(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException ignored) {}
    }
}
