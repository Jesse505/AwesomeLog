package com.example.jesse.log.stragety;

import android.text.TextUtils;
import android.util.Pair;
import com.example.jesse.log.util.LogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CsvFormatStrategy implements DiskLogStrategy {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String SEPARATOR = ",";
    private static final String LEFT_LABEL = " [ ";
    private static final String RIGHT_LABLE = " ] ";
    private static final String PID = "pid : ";
    private static final String THREAD_NAME = "thread : ";
    private final Date date;
    private final SimpleDateFormat dateFormat;
    private final DiskLogStrategy logStrategy;

    private CsvFormatStrategy(Builder builder) {
        date = new Date();
        dateFormat = builder.dateFormat;
        logStrategy = builder.logStrategy;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    @Override
    public void writeCommonInfo() {
        logStrategy.writeCommonInfo();
    }

    @Override
    public void log(int priority, String onceOnlyTag, String message) {

        date.setTime(System.currentTimeMillis());

        StringBuilder builder = new StringBuilder();
        // time
        builder.append(dateFormat.format(date));
        builder.append(SEPARATOR);
        builder.append(Thread.currentThread().getId());
        builder.append(SEPARATOR);
        builder.append(Thread.currentThread().getName());
        builder.append(SEPARATOR);
        builder.append(LogUtils.logLevel(priority));
        builder.append(SEPARATOR);
        builder.append(onceOnlyTag);
        builder.append(SEPARATOR);
        builder.append(csvFormatHandle(message));
        Pair<String, String> classAndMethodName = LogUtils.getClassAndMethodName();
        if (classAndMethodName.first != null){
            builder.append(SEPARATOR);
            builder.append(classAndMethodName.first);

        }
        if (classAndMethodName.second != null){
            builder.append(SEPARATOR);
            builder.append(classAndMethodName.second);
        }
        builder.append(NEW_LINE);

        logStrategy.log(priority, onceOnlyTag, builder.toString());
    }

    @Override
    public void flush() {
        logStrategy.flush();
    }

    /**
     * csv格式如果有逗号，整体用双引号括起来；如果里面还有双引号就替换成两个双引号，这样导出来的格式就不会有问题了
     * @param message
     * @return
     */
    private String csvFormatHandle(String message){
        if (TextUtils.isEmpty(message)) return message;
        String messageCSV = message;
        // 将逗号转义
        if(message.contains(",")){
            // 先将双引号转义，避免两边加了双引号后转义错误
            if(message.contains("\"")){
                messageCSV = message.replace("\"", "\"\"");
            }
            messageCSV = "\"" + messageCSV + "\"";
        }
        // 消除所有空格换行符
        messageCSV = messageCSV.replaceAll("[\r\n\t]", "").replaceAll(" ", "");
        return messageCSV;
    }

    @Override
    public String getLogPath() {
        return logStrategy.getLogPath();
    }

    public static final class Builder {
        SimpleDateFormat dateFormat;
        DiskLogStrategy logStrategy;
        private Builder() {
        }
        public Builder dateFormat(SimpleDateFormat val) {
            dateFormat = val;
            return this;
        }

        public Builder logStrategy(DiskLogStrategy val) {
            logStrategy = val;
            return this;
        }

        public CsvFormatStrategy build() {
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
            }
            if (logStrategy == null) {
                logStrategy = new DiskDailyLogStrategy.Builder().build();
            }
            return new CsvFormatStrategy(this);
        }
    }
}
