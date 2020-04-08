package com.android.jesse.log.stragety;

public interface LogStrategy {

  void log(int priority, String tag, String message);

  void flush();
}
