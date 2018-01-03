package com.tjun.asw.androidpro.http;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Jun on 17/8/6.
 */

public class MultipartHttp {

    public static void main(String args[]){
        OkHttpClient client = new OkHttpClient();

        RequestBody imageBody = RequestBody.create(MediaType.parse("image/png"),new File("/Users/Jun/Desktop/帅哥.png"));

        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name","tanjun")
                .addFormDataPart("filename","帅哥.png",imageBody)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.5:8080/web/UploadServlet")
                .post(body)
                .build();
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
