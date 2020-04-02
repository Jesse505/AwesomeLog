package com.example.jesse.log.Printer;


import com.example.jesse.log.stragety.LogStrategy;
import com.example.jesse.log.stragety.PrettyFormatStrategy;

public class AndroidLogPrinter implements Printer {

  private final LogStrategy formatStrategy;

  public AndroidLogPrinter() {
    this.formatStrategy = PrettyFormatStrategy.newBuilder().build();
  }

  public AndroidLogPrinter(LogStrategy formatStrategy) {
    this.formatStrategy = formatStrategy;
  }

  @Override
  public boolean isLoggable(int priority, String tag) {
    return true;
  }

  @Override
  public void log(int priority, String tag, String message) {
    formatStrategy.log(priority, tag, message);
  }

  @Override
  public void flush() {
    formatStrategy.flush();
  }

}
