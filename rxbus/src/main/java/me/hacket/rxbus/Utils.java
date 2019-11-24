package me.hacket.rxbus;

import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;

final class Utils {

    static boolean logEnable = false;
    static String logTag = "RxBus";

    public static void logi(String anchor, String msg) {
        if (!logEnable) {
            return;
        }
        String message = buildLogMsg(anchor, msg);
        Log.i(logTag, message);
    }

    public static void logw(String anchor, String msg) {
        if (!logEnable) {
            return;
        }
        String message = buildLogMsg(anchor, msg);
        Log.w(logTag, message);
    }

    private static String buildLogMsg(String anchor, String msg) {
        StringBuilder sb = new StringBuilder()
                .append("[")
                .append(anchor)
                .append("] ")
                .append(msg)
                .append("，线程: ")
                .append(getThreadName())
                .append(",")
                .append("日期: ")
                .append(getFormatCurrentDate())
                .append("\n");
        return sb.toString();
    }

    private static String getThreadName() {
        return Thread.currentThread().getName();
    }

    private static String getFormatCurrentDate() {
        return formatDateToString(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss");
    }

    private static String formatDateToString(long inTimeInMillis, String inFormat) {
        return DateFormat.format(inFormat, new Date(inTimeInMillis)).toString();
    }

    /**
     * Verifies if the object is not null and returns it or throws a NullPointerException with the
     * given message.
     *
     * @param <T>     the value type
     * @param object  the object to verify
     * @param message the message to use with the NullPointerException
     * @return the object itself
     * @throws NullPointerException if object is null
     */
    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * Utility class.
     */
    private Utils() {
        throw new IllegalStateException("No instances!");
    }

}
