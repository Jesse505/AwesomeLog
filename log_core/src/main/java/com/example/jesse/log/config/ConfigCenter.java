package com.example.jesse.log.config;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;

/**
 * 日志配置文件
 *
 * @author dongyk on 2019/2/20
 */
public class ConfigCenter {
    /**
     * 默认保存7天日志
     */
    private static final int DEFAULT_DAILY_SIZE = 7;

    /**
     * 默认保存70m
     */
    private static final double DEFAULT_LOG_FILE_SIZE_MB = 70;


    /**
     * 日志最大保存天数
     */
    private int mMaxKeepDaily = DEFAULT_DAILY_SIZE;

    private double mMaxLogSizeMb = DEFAULT_LOG_FILE_SIZE_MB;

    /**
     * 日志保存路径
     */
    private String mLogPath;

    /**
     * 日志缓存路径
     */
    private String mCachePath;

    /**
     * 商家ID
     */
    private String mKdtId;

    private Context mContext;

    /**
     * 设备id
     */
    private String deviceId;


    /**
     * 是否为平板
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


    private static class ZanLoggerConfigHolder {
        private static final ConfigCenter instance = new ConfigCenter();
    }

    public static ConfigCenter getInstance() {
        return ZanLoggerConfigHolder.instance;
    }

    private ConfigCenter() {

    }

    public boolean isHd() {
        return hd;
    }

    public void setHd(boolean hd) {
        this.hd = hd;
    }

    public int getMaxKeepDaily() {
        return mMaxKeepDaily;
    }

    public void setMaxKeepDaily(int maxKeepDaily) {
        this.mMaxKeepDaily = maxKeepDaily;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getMaxLogSizeMb() {
        return mMaxLogSizeMb;
    }

    public void setMaxLogSizeMb(double maxLogSizeMb) {
        mMaxLogSizeMb = maxLogSizeMb;
    }

    public String getLogPath() {
        if (TextUtils.isEmpty(mLogPath)) {
            mLogPath = getDefaultLogPath();
        }
        return mLogPath;
    }

    public String getmCachePath() {
        if (TextUtils.isEmpty(mCachePath)) {
            mCachePath = getDefaultCachePath();
        }
        return mCachePath;
    }

    public Context getContext() {
        if (mContext == null) throw new RuntimeException("ConfigCenter context can not be null");
        return mContext;
    }

    public String getPackageName() {
        return getContext().getPackageName();
    }

    public void setContext(Context context) {
        mContext = context;
    }


    public String getKdtId() {
        return mKdtId;
    }

    public void setKdtId(String kdtId) {
        mKdtId = kdtId;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * /sdcard/Android/data/xx.xx.xx/files/zanLogger/log
     *
     * @return 返回默认目录
     */
    private String getDefaultLogPath() {
        String mPath = new File(getContext().getExternalFilesDir(null), "log").getAbsolutePath();
        File logFile = new File(mPath);
        if (!logFile.exists()){
            logFile.mkdirs();
        }
        return mPath;
    }

    private String getDefaultCachePath() {
        String mPath = new File(getContext().getExternalFilesDir(null), "cache").getAbsolutePath();
        File logFile = new File(mPath);
        if (!logFile.exists()){
            logFile.mkdirs();
        }
        return mPath;
    }

}
