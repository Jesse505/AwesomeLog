package com.android.jesse.awesomelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.os.Bundle;
import android.view.View;


import com.android.jesse.log.ALog;
import com.android.jesse.log.LogConfig;
import com.example.jesse.awesomelog.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private static final int RC_STORAGE_PERM = 123;
    private static final String[] WRITE_AND_READ_STORAGE =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化日志系统
        ALog.init(new LogConfig(this));
    }


    public void write2file(View view) {
        write2file();
    }

    @AfterPermissionGranted(RC_STORAGE_PERM)
    public void write2file() {
        if (hasWriteAndReadPermissions()) {
            ALog.i("zyf", "hello AwesomeLog");
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    MainActivity.this,
                    "申请存储卡选项用于存储",
                    RC_STORAGE_PERM,
                    WRITE_AND_READ_STORAGE);
        }
    }

    private boolean hasWriteAndReadPermissions() {
        return EasyPermissions.hasPermissions(this, WRITE_AND_READ_STORAGE);
    }

    public void flush(View view) {
        ALog.flush();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

}
