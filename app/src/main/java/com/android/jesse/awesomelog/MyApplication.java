package com.android.jesse.awesomelog;

import android.app.Application;
import android.util.Log;


import com.android.jesse.log.Logan;
import com.android.jesse.log.LoganConfig;
import com.android.jesse.log.OnLoganProtocolStatus;

import java.io.File;

public class MyApplication extends Application {

    private static final String TAG = "AwesomeLog";
    private static final String FILE_NAME = "logan_v1";

    @Override
    public void onCreate() {
        super.onCreate();
        initLogan();
    }

    private void initLogan() {
        LoganConfig config = new LoganConfig.Builder()
                .setCachePath(getApplicationContext().getFilesDir().getAbsolutePath())
                .setPath(getApplicationContext().getExternalFilesDir(null).getAbsolutePath()
                        + File.separator + FILE_NAME)
                .build();
        Log.i(TAG, "cachePath > " + getApplicationContext().getFilesDir().getAbsolutePath());
        Log.i(TAG, "path > " + getApplicationContext().getExternalFilesDir(null).getAbsolutePath()
                + File.separator + FILE_NAME);
        Logan.init(config);
        Logan.setDebug(true);
        Logan.setOnLoganProtocolStatus(new OnLoganProtocolStatus() {
            @Override
            public void loganProtocolStatus(String cmd, int code) {
                Log.d(TAG, "clogan > cmd : " + cmd + " | " + "code : " + code);
            }
        });

    }
}
