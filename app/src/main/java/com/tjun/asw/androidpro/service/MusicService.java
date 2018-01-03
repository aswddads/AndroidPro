package com.tjun.asw.androidpro.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by asw on 2017/12/28.
 */

public class MusicService extends Service {
    public MediaPlayer mediaPlayer;
    public boolean tag = false;

    private String mUrl = null;

    public MusicService() {
        mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(mUrl);
//            mediaPlayer.prepareAsync();
           // mediaPlayer.setLooping(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //  通过 Binder 来保持 Activity 和 Service 的通信
    public MyBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        public MusicService getService(String url) {
            mUrl = url;
            return MusicService.this;
        }
    }

    public void playOrPause() {
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//        } else {
        try {
            mediaPlayer.setDataSource(mUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(mUrl);
                //mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
