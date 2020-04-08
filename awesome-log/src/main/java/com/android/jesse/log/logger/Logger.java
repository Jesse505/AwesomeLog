package com.android.jesse.log.logger;



import com.android.jesse.log.Printer.Printer;

import java.util.List;

public interface Logger {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
    public static final int NET = 8;

    void d(String tag, String message, Object... args);

    void d(String tag, Object object);

    void e(String tag, String message, Object... args);

    void e(String tag, Throwable throwable, String message, Object... args);

    void w(String tag, String message, Object... args);

    void i(String tag, String message, Object... args);

    void v(String tag, String message, Object... args);

    void wtf(String tag, String message, Object... args);

    void json(String tag, String json);

    void xml(String tag, String xml);

    void net(String tag, String message, Object... args);

    void log(int priority, String tag, String message, Throwable throwable);

    void flush();

    void addPrinter(Printer printer);

    List<Printer> getPrinters();

    void clearLogPrinters();
}
