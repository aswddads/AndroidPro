package com.tjun.asw.androidpro.http;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jun on 17/8/5.
 */

public class PostHttp {
    public static void main(String args[]){
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("username","tj")
                .add("userage","100")
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/web/HelloServlet")
                .post(formBody)
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
