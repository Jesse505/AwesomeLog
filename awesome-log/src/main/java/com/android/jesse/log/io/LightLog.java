package com.android.jesse.log.io;

import android.text.TextUtils;

import com.android.jesse.log.util.DateUtil;
import com.android.jesse.log.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.BufferOverflowException;
import java.nio.MappedByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.FileChannel;

public class LightLog {
    private static final long KB = 1024;
    private static final long MB = 1024 * KB;
    private static final long DEFAULT_CACHE_SIZE = 1 * KB;
    private static final int MINUTE = 60 * 1000;
    private static final long DAY = 24 * 60 * 60 * 1000;
    private static final double DEFAULT_MAX_LOG_SIZE = 50;
    private static final int DEFAULT_KEEP_DAILY = 7;

    private static volatile LightLog sLightLog;
    private MappedByteBuffer mappedByteBuffer;


    private String mCachePath; //log缓存目录
    private String mPath;      //log目录
    private double mMaxLogSizeMb = DEFAULT_MAX_LOG_SIZE;
    private int mMaxKeepDaily = DEFAULT_KEEP_DAILY;

    boolean isCanWriteToSDCard = false;
    private long mCurrentDay;
    private long mLastTime;

    private LightLog() {

    }

    public static LightLog newInstance() {
        if (sLightLog == null) {
            synchronized (LightLog.class) {
                sLightLog = new LightLog();
            }
        }
        return sLightLog;
    }

    public void init(String cachePath, String path, double maxLogSizeMb, int maxKeepDaily) {
        mCachePath = cachePath;
        mPath = path;
        mMaxLogSizeMb = maxLogSizeMb;
        mMaxKeepDaily = maxKeepDaily;
        if (TextUtils.isEmpty(mCachePath) || TextUtils.isEmpty(mPath)) {
            throw new RuntimeException("init method is not invoked");
        }
    }

    public void flush() {
        String currentDate = DateUtil.getDateStr(System.currentTimeMillis());
        flush(currentDate);
    }

    public void flush(String date) {
        if (TextUtils.isEmpty(date)) {
            return;
        }
        String cachePath = getCachePath();
        String logPath = mPath + File.separator + date + ".log";
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()) {
            return;
        }

        RandomAccessFile rafi = null;
        RandomAccessFile rafo = null;
        FileChannel fci = null;
        FileChannel fco = null;

        try {
            File logFile = new File(logPath);
            if (!logFile.exists()) {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            }

            rafi = new RandomAccessFile(cacheFile, "rw");
            rafo = new RandomAccessFile(logFile, "rw");

            fci = rafi.getChannel();
            fco = rafo.getChannel();

            long cacheSize = fci.size();
            long logSize = fco.size();

            MappedByteBuffer mbbi = fci.map(FileChannel.MapMode.READ_WRITE, 0, cacheSize);
            MappedByteBuffer mbbo = fco.map(FileChannel.MapMode.READ_WRITE, logSize, cacheSize);
            for (int i = 0; i < cacheSize; i++) {
                mbbo.put(mbbi.get(i));
            }
            //解除内存映射
            unmap(mbbi);
            unmap(mbbo);
            //清空缓存文件
            FileWriter fileWriter = new FileWriter(cacheFile);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            unmap(mappedByteBuffer);
            mappedByteBuffer = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fci) {
                    fci.close();
                    fci = null;
                }

                if (null != fco) {
                    fco.close();
                    fco = null;
                }

                if (null != rafi) {
                    rafi.close();
                    rafi = null;
                }

                if (null != rafo) {
                    rafo.close();
                    rafo = null;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }


    public void write(byte[] log) {

        if (!isDay()) {
            long tempCurrentDay = DateUtil.getCurrentTime();
            //save时间
            long deleteTime = tempCurrentDay - mMaxKeepDaily * DAY;
            deleteExpiredFile(deleteTime);
            mCurrentDay = tempCurrentDay;
        }

        long currentTime = System.currentTimeMillis(); //每隔1分钟判断一次
        if (currentTime - mLastTime > MINUTE) {
            isCanWriteToSDCard = isCanWriteSDCard();
        }
        mLastTime = System.currentTimeMillis();

        if (!isCanWriteToSDCard) {
            return;
        }

        if (null == log) {
            return;
        }

        try {
            MappedByteBuffer mbbi = getMappedByteBuffer();
            if (mbbi != null) {
                mbbi.put(log);
            }
        } catch (BufferOverflowException e) {
            //缓存区满了则flush到日志文件
            String currentDate = DateUtil.getDateStr(System.currentTimeMillis());
            flush(currentDate);

            MappedByteBuffer mbbi = getMappedByteBuffer();
            if (mbbi != null) {
                mbbi.put(log);
            }
            e.printStackTrace();
        } catch (ReadOnlyBufferException e) {
            e.printStackTrace();
        }
    }

    private boolean isDay() {
        long currentTime = System.currentTimeMillis();
        return mCurrentDay < currentTime && mCurrentDay + DAY > currentTime;
    }


    private boolean isCanWriteSDCard() {
        boolean item = false;
        try {
            long total = FileUtils.getFileSizes(new File(mPath));
            if (total < mMaxLogSizeMb * MB) {
                item = true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 删除过期的文件
     *
     * @param deleteTime 此时间之前的
     */
    private void deleteExpiredFile(long deleteTime) {
        File dir = new File(mPath);
        if (dir.isDirectory()) {
            String[] files = dir.list();
            if (files != null) {
                for (String item : files) {
                    try {
                        if (TextUtils.isEmpty(item)) {
                            continue;
                        }
                        String[] longStrArray = item.split("\\.");
                        if (longStrArray.length > 0) {  //小于时间就删除
                            long longItem = DateUtil.getDateTime(longStrArray[0]);
                            if (longItem <= deleteTime) {
                                new File(mPath, item).delete(); //删除文件
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getCachePath() {
        if (TextUtils.isEmpty(mCachePath)) {
            throw new RuntimeException("init method is not invoked");
        }
        return mCachePath + File.separator + "cache.log";
    }

    private MappedByteBuffer getMappedByteBuffer() {
        if (null != mappedByteBuffer) {
            return mappedByteBuffer;
        }
        RandomAccessFile rafi;
        FileChannel fci;
        try {
            File cacheFile = new File(getCachePath());
            if (!cacheFile.exists()) {
                cacheFile.getParentFile().mkdirs();
                cacheFile.createNewFile();
            }

            rafi = new RandomAccessFile(cacheFile, "rw");
            fci = rafi.getChannel();

            //如果缓存大小大于零，先flush下,因为可能缓存文件里有上次启动app写入的数据，所以先要flush一下
            if (fci.size() > 0) {
                String currentDate = DateUtil.getDateStr(System.currentTimeMillis());
                flush(currentDate);
            }

            MappedByteBuffer mbbi = fci.map(FileChannel.MapMode.READ_WRITE, 0, DEFAULT_CACHE_SIZE);
            if (null != mbbi) {
                mappedByteBuffer = mbbi;
                return mbbi;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解除内存与文件的映射
     */
    private void unmap(MappedByteBuffer mbbi) {
        if (mbbi == null) {
            return;
        }
        try {
            Class<?> clazz = Class.forName("sun.nio.ch.FileChannelImpl");
            Method m = clazz.getDeclaredMethod("unmap", MappedByteBuffer.class);
            m.setAccessible(true);
            m.invoke(null, mbbi);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
