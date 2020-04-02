package com.example.jesse.log.Printer;

import android.content.Context;

import com.example.jesse.log.ALog;
import com.example.jesse.log.logger.Logger;
import com.example.jesse.log.stragety.CsvFormatStrategy;
import com.example.jesse.log.stragety.DiskDailyLogStrategy;
import com.example.jesse.log.stragety.DiskLogStrategy;


public class DiskLogPrinter implements DiskLogStrategy, Printer {

    private DiskLogStrategy formatStrategy;


    public DiskLogPrinter(Context context) {
        formatStrategy = CsvFormatStrategy.newBuilder()
                .logStrategy(DiskDailyLogStrategy
                        .newBuilder()
                        .context(context.getApplicationContext())
                        .build())
                .build();
    }

    public DiskLogPrinter(DiskLogStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public String getLogPath() {
        return this.formatStrategy.getLogPath();
    }

    @Override
    public void writeCommonInfo() {
        formatStrategy.writeCommonInfo();
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return priority > Logger.DEBUG;
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
