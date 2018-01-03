package com.tjun.asw.androidpro.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tjun.asw.androidpro.R;

public class MainActivity extends AppCompatActivity {

    private static final int TRANS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, MusicListActivity.class);
                startActivity(intent);
                finish();
            }
        }, TRANS);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
