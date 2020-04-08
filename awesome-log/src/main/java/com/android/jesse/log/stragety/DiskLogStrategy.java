package com.android.jesse.log.stragety;


public interface DiskLogStrategy extends LogStrategy {
    String getLogPath();
    void writeCommonInfo();
}
