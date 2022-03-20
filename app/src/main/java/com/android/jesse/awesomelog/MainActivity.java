package com.android.jesse.awesomelog;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.jesse.log.Logan;
import com.example.jesse.awesomelog.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void write2file(View view) {
        Logan.w("666", 2);
    }

    public void flush(View view) {
        Logan.f();
    }
}
