package com.example.jesse.log;

import android.content.Context;

import com.example.jesse.log.encrypt.LogEncrypt;


public class LogConfig {

    /**********************必须参数***************/
    /**
     * 上下文(*必须)
     */
    private Context mContext;


    /**
     * 设备id (*必须)
     */
    private String deviceId;


    /**
     * 是否为hd
     */
    private boolean hd;


    /**
     * Android 系统版本  Andorid x.x
     */
    private String osVersion;

    /**
     * app版本
     */
    private String appVersion;

    /**
     * 设备名称
     */
    private String deviceName;

    private boolean autoBackup;

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


    public LogConfig(Context context) {
        mContext = context;
    }

    public LogConfig setLogEncrypt(LogEncrypt logEncrypt) {
        this.logEncrypt = logEncrypt;
        return this;
    }

    public LogConfig deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }


    public LogConfig hd(boolean hd) {
        this.hd = hd;
        return this;
    }


    public LogConfig osVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public LogConfig appVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public LogConfig deviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }


    public LogConfig autoBackup(boolean autoBackup) {
        this.autoBackup = autoBackup;
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

    public boolean isAutoBackup() {
        return autoBackup;
    }

    public LogEncrypt getLogEncrypt() {
        return logEncrypt;
    }

    public Context getContext() {
        return mContext;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isHd() {
        return hd;
    }


    public String getOsVersion() {
        return osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public double getMaxLogSizeMb() {
        return maxLogSizeMb;
    }

    public int getMaxKeepDaily() {
        return maxKeepDaily;
    }
}
