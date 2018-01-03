package com.tjun.asw.androidpro.http;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jun on 17/8/5.
 */

public class QueryHttp {
    public static void main(String args[]){
        OkHttpClient client = new OkHttpClient();
        HttpUrl httpUrl = HttpUrl.parse("https://api.heweather.com/x3/weather")
                .newBuilder()
                .addQueryParameter("city","beijing")
                .addQueryParameter("key","d17ce22ec5404ed883e1cfcaca0ecaa7")
                .build();
        System.out.println(httpUrl.toString());
        Request request = new Request.Builder().url(httpUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                    System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
