package com.android.jesse.log.stragety;

import android.text.TextUtils;
import android.util.Pair;
import com.android.jesse.log.util.LogUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvFormatStrategy implements DiskLogStrategy {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String SEPARATOR = ",";
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
     * csv格式如果有逗号或者换行的话整体用双引号括起来；如果里面还有双引号就替换成两个双引号，
     * @param message
     * @return
     */
    private String csvFormatHandle(String message){
        if (TextUtils.isEmpty(message)) return message;
        String messageCSV = message;
        Matcher matcher = Pattern.compile("[\r\n\t]").matcher(messageCSV);
        // 如果包含逗号或者换行的话就在前后添加双引号
        if(message.contains(",") || matcher.find()){
            // 先将双引号转义，避免两边加了双引号后转义错误
            if(message.contains("\"")){
                messageCSV = message.replace("\"", "\"\"");
            }
            messageCSV = "\"" + messageCSV + "\"";
        }
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
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
            logStrategy = new DiskDailyLogStrategy.Builder().build();
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
            return new CsvFormatStrategy(this);
        }
    }
}
