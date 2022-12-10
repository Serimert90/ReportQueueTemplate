package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LogUtils {

    private LogUtils() {}

    public static void log(String log) {
        DateFormat formatter =  new SimpleDateFormat("dd-M-yyyy hh:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(new Date(System.currentTimeMillis()));
        System.out.println(dateFormatted + ":" + log);
    }
}
