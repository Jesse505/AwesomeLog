package com.android.jesse.log.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Pair;

import com.android.jesse.log.ALog;
import com.android.jesse.log.LogEventType;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;

public final class LogUtils {
    private LogUtils() {
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        } else {
            for (Throwable t = tr; t != null; t = t.getCause()) {
                if (t instanceof UnknownHostException) {
                    return "";
                }
            }

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            tr.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        }
    }

    public static String logLevel(int value) {
        switch (value) {
            case 2:
                return "VERBOSE";
            case 3:
                return "DEBUG";
            case 4:
                return "INFO";
            case 5:
                return "WARN";
            case 6:
                return "ERROR";
            case 7:
                return "ASSERT";
            case 8:
                return "NET";
            default:
                return "UNKNOWN";
        }
    }

    public static String logEventType(int logPriority) {
        switch (logPriority) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return LogEventType.USER_EVENT.getEventType();
            case 8:
                return LogEventType.NET_EVENT.getEventType();
            default:
                return LogEventType.USER_EVENT.getEventType();
        }
    }

    public static Pair<String, String> getClassAndMethodName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements != null && elements.length != 0) {
            int classHierarchy = -1;

            for (int i = 0; i < elements.length; ++i) {
                StackTraceElement element = elements[i];
                if (TextUtils.equals(element.getClassName(), ALog.class.getName())) {
                    classHierarchy = i + 1;
                    break;
                }
            }

            return classHierarchy != -1 && elements.length > classHierarchy ? new Pair(elements[classHierarchy].getClassName(), elements[classHierarchy].getMethodName()) : new Pair((Object) null, (Object) null);
        } else {
            return new Pair((Object) null, (Object) null);
        }
    }

    public static String toString(Object object) {
        if (object == null) {
            return "null";
        } else if (!object.getClass().isArray()) {
            return object.toString();
        } else if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) ((boolean[]) object));
        } else if (object instanceof byte[]) {
            return Arrays.toString((byte[]) ((byte[]) object));
        } else if (object instanceof char[]) {
            return Arrays.toString((char[]) ((char[]) object));
        } else if (object instanceof short[]) {
            return Arrays.toString((short[]) ((short[]) object));
        } else if (object instanceof int[]) {
            return Arrays.toString((int[]) ((int[]) object));
        } else if (object instanceof long[]) {
            return Arrays.toString((long[]) ((long[]) object));
        } else if (object instanceof float[]) {
            return Arrays.toString((float[]) ((float[]) object));
        } else if (object instanceof double[]) {
            return Arrays.toString((double[]) ((double[]) object));
        } else {
            return object instanceof Object[] ? Arrays.deepToString((Object[]) ((Object[]) object)) : "Couldn'tag find a correct type for the object";
        }
    }

    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.versionName : null;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;

        try {
            PackageManager packageManager = context.getPackageManager();
            info = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return info;
    }
}
