package com.tjun.asw.androidpro.http;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jun on 17/8/5.
 */

public class HeadHttp {

    public static void main(String args[]){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.imooc.com")
                .addHeader("User-Agent","from Jun http")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + " : " + headers.value(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
