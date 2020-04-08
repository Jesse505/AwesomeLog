package com.android.jesse.log.stragety;

public class PrettyFormatStrategy implements LogStrategy {


  private static final int CHUNK_SIZE = 4000;

  private static final char TOP_LEFT_CORNER = '┌';
  private static final char BOTTOM_LEFT_CORNER = '└';
  private static final char HORIZONTAL_LINE = '│';
  private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
  private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
  private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;

  private final LogStrategy logStrategy;

  private PrettyFormatStrategy(Builder builder) {
    logStrategy = builder.logStrategy;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public void log(int priority, String onceOnlyTag, String message) {

    logTopBorder(priority, onceOnlyTag);

    byte[] bytes = message.getBytes();
    int length = bytes.length;
    if (length <= CHUNK_SIZE) {
      logContent(priority, onceOnlyTag, message);
      logBottomBorder(priority, onceOnlyTag);
      return;
    }
    for (int i = 0; i < length; i += CHUNK_SIZE) {
      int count = Math.min(length - i, CHUNK_SIZE);
      logContent(priority, onceOnlyTag, new String(bytes, i, count));
    }
    logBottomBorder(priority, onceOnlyTag);
  }

  @Override
  public void flush() {
    logStrategy.flush();
  }

  private void logTopBorder(int logType, String tag) {
    logChunk(logType, tag, TOP_BORDER);
  }

  private void logBottomBorder(int logType, String tag) {
    logChunk(logType, tag, BOTTOM_BORDER);
  }


  private void logContent(int logType, String tag, String chunk) {
    String[] lines = chunk.split(System.getProperty("line.separator"));
    for (String line : lines) {
      logChunk(logType, tag, HORIZONTAL_LINE + " " + line);
    }
  }

  private void logChunk(int priority, String tag, String chunk) {
    logStrategy.log(priority, tag, chunk);
  }


  public static class Builder {
    LogStrategy logStrategy;

    private Builder() {
    }

    public PrettyFormatStrategy build() {
      if (logStrategy == null) {
        logStrategy = new LogcatLogStrategy();
      }
      return new PrettyFormatStrategy(this);
    }
  }

}
