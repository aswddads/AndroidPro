package com.tjun.asw.androidpro.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tjun.asw.androidpro.R;
import com.tjun.asw.androidpro.model.MusicPlay;
import com.tjun.asw.androidpro.model.SelectMusic;
import com.tjun.asw.androidpro.service.MusicService;
import com.tjun.asw.androidpro.uri.MyUri;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by asw on 2017/12/27.
 */

public class MusicPlayActivity extends Activity{
    private TextView mMusicStatus;
    private TextView MusicTime;
    private SeekBar MusicSeekBar;
    private TextView MusicTotal;
    private ImageView BtnPlay;
    private ImageView BtnStop;
    private ImageView mCancel;
    private ProgressBar mPbCancel;
    private TextView mSongName;
    private ImageView mPic;

    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private boolean tag1 = false;
    private boolean tag2 = false;
    private MusicService musicService;

    private String mUrl = MyUri.BASE_URI+MyUri.MUSIC_PLAY_PATH;

    private String mPlayUrl = null;
    private String mPlayName = null;
    private String mPlayPicUrl = null;

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1){
//                mPlayUrl = msg.obj.toString();
//                bindServiceConnection();
//            }
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Bundle bundle = getIntent().getExtras();
        mUrl = mUrl + bundle.getLong("musicId");
        mPlayName = bundle.getString("musicName");
        mPlayPicUrl = bundle.getString("musicPic");
        initView();
        sendRequest(mUrl);
//        myListener();
        MusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    musicService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder) (service)).getService(mPlayUrl);
            Log.i("musicService", musicService + "");
//            MusicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    private void sendRequest(String url){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    SelectMusic selectMusic = new SelectMusic();
                    selectMusic = new Gson().fromJson(response.body().string(),SelectMusic.class);
                    MusicPlay musicPlay = new MusicPlay();
                    musicPlay = selectMusic.getData().get(0);
                    final String res = musicPlay.getUrl();
//                    Message message = Message.obtain();
//                    message.what = 1;
//                    message.obj = res;
//                    mHandler.sendMessage(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPlayUrl = res;
                            //  回调onServiceConnected 函数，通过IBinder 获取 Service对象，实现Activity与 Service的绑定
                            bindServiceConnection(serviceConnection);
                            myListener();
                        }
                    });
                }
            }
        });
    }
    private void initView() {
        mMusicStatus = (TextView) findViewById(R.id.MusicStatus);
        MusicTime = (TextView) findViewById(R.id.MusicTime);
        MusicSeekBar = (SeekBar)findViewById(R.id.MusicSeekBar);
        MusicTotal = findViewById(R.id.MusicTotal);
        BtnPlay = (ImageView) findViewById(R.id.BtnPlay);
        BtnStop = findViewById(R.id.BtnStop);
        mCancel = findViewById(R.id.iv_cancel);
        mPbCancel = findViewById(R.id.pb_cancel_progress);
        mSongName = findViewById(R.id.MusicStatus);
        mSongName.setText(mPlayName);
        mPic = findViewById(R.id.Image);
        Glide.with(this).load(mPlayPicUrl).asBitmap().into(mPic);
    }

    //  在Activity中调用 bindService 保持与 Service 的通信
    private void bindServiceConnection(ServiceConnection serviceConnection) {
        Intent intent = new Intent(MusicPlayActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, this.BIND_AUTO_CREATE);
    }

    //  通过 Handler 更新 UI 上的组件状态
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MusicTime.setText(time.format(musicService.mediaPlayer.getCurrentPosition()));
            MusicSeekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
            MusicSeekBar.setMax(musicService.mediaPlayer.getDuration());
            MusicTotal.setText(time.format(musicService.mediaPlayer.getDuration()));
            handler.postDelayed(runnable, 200);

        }
    };


    private void myListener() {
        ImageView imageView = (ImageView) findViewById(R.id.Image);
        final ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        BtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService.mediaPlayer != null) {
                    MusicSeekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
                    MusicSeekBar.setMax(musicService.mediaPlayer.getDuration());
                    BtnStop.setVisibility(View.VISIBLE);
                    BtnPlay.setVisibility(View.GONE);
                }
                //  由tag的变换来控制事件的调用
//                if (musicService.tag != true) {
                    musicService.playOrPause();
                    //musicService.tag = true;

                    //if (tag1 == false) {
                        //animator.start();
                       // tag1 = true;
                    //} else {
                   //     animator.resume();
                   // }
//                } else {
//                    musicService.playOrPause();
//                    animator.pause();
//                    musicService.tag = false;
//                }
                //if (tag2 == false) {
                    handler.post(runnable);
                  //  tag2 = true;
               // }
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnStop.setVisibility(View.GONE);
                BtnPlay.setVisibility(View.VISIBLE);
                musicService.stop();
               // animator.pause();
               // musicService.tag = false;
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPbCancel.setVisibility(View.VISIBLE);
                finish();
                musicService.stop();
                //animator.pause();
                //BtnStop.setVisibility(View.GONE);
               // BtnPlay.setVisibility(View.VISIBLE);
//                mPbCancel.setVisibility(View.GONE);
            }
        });
    }

    //  获取并设置返回键的点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
