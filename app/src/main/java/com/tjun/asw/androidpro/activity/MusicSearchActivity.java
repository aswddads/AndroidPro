package com.tjun.asw.androidpro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tjun.asw.androidpro.R;
import com.tjun.asw.androidpro.adapter.MyAdapter;
import com.tjun.asw.androidpro.model.Music;
import com.tjun.asw.androidpro.model.SearchMusicList;
import com.tjun.asw.androidpro.model.SearchResultMusicList;
import com.tjun.asw.androidpro.recyc.MyDividerItemDecoration;
import com.tjun.asw.androidpro.uri.MyUri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by asw on 2017/12/27.
 */

public class MusicSearchActivity extends AppCompatActivity{
    private String mSearchWords;
    private String mUrl;

    private EditText mEdit;
    private TextView mSearch;
    private RecyclerView mRecyclerView;
    private ProgressBar mSearchPro;

    private RecyclerView.LayoutManager mLayoutManager;


    //    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1){
//                MyAdapter myAdapter = new MyAdapter((List<Music>) msg.obj);
//                mRecyclerView.setAdapter(myAdapter);
//                myAdapter.notify();
//            }
//        }
//    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mEdit = findViewById(R.id.et_search);
        mSearch = findViewById(R.id.tv_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_search_list);
        mSearchPro = findViewById(R.id.pb_search_progress);

        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchPro.setVisibility(View.VISIBLE);
                mSearchWords = mEdit.getText().toString();
                mUrl = MyUri.BASE_URI+MyUri.MUSIC_SEARCH_PATH+mSearchWords;
                sendAsyncRequest(mUrl);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0) ;
            }
        });

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);//指定Toolbar上的视图文件
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id){
//            case android.R.id.home:
//                this.finish();//真正实现回退功能的代码
//            default:break;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void sendAsyncRequest(String url){

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();

//                    Message message = Message.obtain();
//                    message.what = 1;
//                    message.obj = musicList;
//                    mHandler.sendMessage(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSearchPro.setVisibility(View.INVISIBLE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            getJson(result);
                        }
                    });
                }
            }
        });
    }
    private void getJson(String result){
        SearchMusicList searchMusicList = new SearchMusicList();
        searchMusicList = new Gson().fromJson(result,SearchMusicList.class);
        SearchResultMusicList searchResultMusicList = new SearchResultMusicList();
        searchResultMusicList = searchMusicList.getResult();
        List<Music> musicList = new ArrayList<>();
        musicList = searchResultMusicList.getSongs();

        MyAdapter myAdapter = new MyAdapter(musicList);
        mRecyclerView.setAdapter(myAdapter);

        final List<Music> finalMusicList = musicList;
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long id = finalMusicList.get(position).getId();
                Intent intent = new Intent(MusicSearchActivity.this,MusicPlayActivity.class);
                intent.putExtra("musicId",id);
                intent.putExtra("musicName",finalMusicList.get(position).getName());
                intent.putExtra("musicPic",finalMusicList.get(position).getArtists().get(0).getImg1v1Url());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                long id = finalMusicList.get(position).getId();
                Intent intent = new Intent(MusicSearchActivity.this,MusicPlayActivity.class);
                intent.putExtra("musicId",id);
                intent.putExtra("musicName",finalMusicList.get(position).getName());
                intent.putExtra("musicPic",finalMusicList.get(position).getArtists().get(position).getImg1v1Url());
                startActivity(intent);
            }
        });
    }
}
