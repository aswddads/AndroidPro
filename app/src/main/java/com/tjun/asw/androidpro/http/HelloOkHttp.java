package com.tjun.asw.androidpro.http;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jun on 17/8/4.
 */

public class HelloOkHttp {
    public static void main(String args[]){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.imooc.com")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
