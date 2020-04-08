package com.android.jesse.log.Printer;

public interface Printer {

  boolean isLoggable(int priority, String tag);

  void log(int priority, String tag, String message);

  void flush();
}
