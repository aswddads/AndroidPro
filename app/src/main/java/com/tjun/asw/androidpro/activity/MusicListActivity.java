package com.tjun.asw.androidpro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tjun.asw.androidpro.R;
import com.tjun.asw.androidpro.adapter.MyAdapter;
import com.tjun.asw.androidpro.model.Music;
import com.tjun.asw.androidpro.model.MusicFirst;
import com.tjun.asw.androidpro.recyc.MyDividerItemDecoration;
import com.tjun.asw.androidpro.uri.MyUri;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by asw on 2017/12/27.
 */

public class MusicListActivity extends Activity {
    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mSwipRefreshLayout;
    private ProgressBar mProgressBar;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Music> musicSecondList;
    private MusicFirst musicFirst;

    private long mExitTime;

//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 1){
//                musicFirst = new MusicFirst();
//                musicFirst = new Gson().fromJson(msg.obj.toString(),MusicFirst.class);
//                musicSecondList = musicFirst.getPlaylist().getTracks();
//            }
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mRecycleView = findViewById(R.id.recyc_music_list);
        mSwipRefreshLayout = findViewById(R.id.swipeRefresh_list);
        mProgressBar = findViewById(R.id.pb_progress);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        // 设置布局管理器
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        sendAsyncRequest(MyUri.BASE_URI+MyUri.MUSIC_LIST_PATH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        MenuItem searchItem = menu.findItem(R.id.item_search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MusicListActivity.this,MusicSearchActivity.class));
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search:
                startActivity(new Intent(MusicListActivity.this,MusicSearchActivity.class));
                break;
//            case R.id.item_more:
               // startActivity(new Intent(MusicListActivity.this,AboutMoreActivity.class));
//                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);

    }


    private void sendAsyncRequest(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Toast.makeText(MusicListActivity.this,"NetWork error!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mRecycleView.setVisibility(View.VISIBLE);
                            getJson(result);
                        }
                    });
//                    Message message = Message.obtain();
//                    message.obj = result;
//                    message.what = 1;
//                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void getJson(String res){
        musicFirst = new MusicFirst();
        musicFirst = new Gson().fromJson(res,MusicFirst.class);
        musicSecondList = musicFirst.getPlaylist().getTracks();
        final MyAdapter mAdapter = new MyAdapter(musicSecondList);
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long id = musicSecondList.get(position).getId();
                Intent intent = new Intent(MusicListActivity.this,MusicPlayActivity.class);
                intent.putExtra("musicId",id);
                intent.putExtra("musicName",musicSecondList.get(position).getName());
                intent.putExtra("musicPic",musicSecondList.get(position).getAl().getPicUrl());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                long id = musicSecondList.get(position).getId();
                Intent intent = new Intent(MusicListActivity.this,MusicPlayActivity.class);
                intent.putExtra("musicName",musicSecondList.get(position).getName());
                intent.putExtra("musicId",id);
                intent.putExtra("musicPic",musicSecondList.get(position).getAl().getPicUrl());
                startActivity(intent);
            }
        });

        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.updateData(musicSecondList);
                mSwipRefreshLayout.setRefreshing(false);
            }
        });
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MusicListActivity.this, "再按一次退出大头音乐", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
