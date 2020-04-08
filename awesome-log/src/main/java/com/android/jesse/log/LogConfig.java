package com.android.jesse.log;

import android.content.Context;

import com.android.jesse.log.encrypt.LogEncrypt;


public class LogConfig {

    /**
     * 上下文(*必须)
     */
    private Context mContext;

    /**
     * 日志最大容量，默认70m
     */
    private double maxLogSizeMb = 70;

    /**
     * 日志保存日期。默认近7天
     */
    private int maxKeepDaily = 7;

    /**
     * 日志加密方式
     */
    private LogEncrypt logEncrypt;

    /**
     * 日志保存路径
     */
    private String mLogPath;

    /**
     * 日志缓存路径
     */
    private String mCachePath;



    public LogConfig(Context context) {
        mContext = context;
    }

    public LogConfig setLogEncrypt(LogEncrypt logEncrypt) {
        this.logEncrypt = logEncrypt;
        return this;
    }

    public LogConfig maxLogSizeMb(double maxLogSizeMb) {
        this.maxLogSizeMb = maxLogSizeMb;
        return this;
    }

    public LogConfig maxKeepDaily(int maxKeepDaily) {
        this.maxKeepDaily = maxKeepDaily;
        return this;
    }

    public String getmLogPath() {
        return mLogPath;
    }

    public LogConfig setmLogPath(String mLogPath) {
        this.mLogPath = mLogPath;
        return this;
    }

    public String getmCachePath() {
        return mCachePath;
    }

    public LogConfig setmCachePath(String mCachePath) {
        this.mCachePath = mCachePath;
        return this;
    }

    public LogEncrypt getLogEncrypt() {
        return logEncrypt;
    }

    public Context getContext() {
        return mContext;
    }

    public double getMaxLogSizeMb() {
        return maxLogSizeMb;
    }

    public int getMaxKeepDaily() {
        return maxKeepDaily;
    }
}
