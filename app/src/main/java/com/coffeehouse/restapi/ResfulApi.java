package com.coffeehouse.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResfulApi {
    private static ResfulApi ourInstance;

    public static ResfulApi getInstance() {
        if (ourInstance == null) {
            ourInstance = new ResfulApi();
        }
        return ourInstance;
    }


    private Retrofit retrofit;

    private ResfulApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(new HttpUrl.Builder()
                        .scheme("http")
//                        .host("192.168.1.246")
                        .host("192.168.1.13")
                        .port(8080)
                        .build())
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
    }

    public <T> T getService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }


    public static RequestBody createJsonRequestBody(Map<String, Object> params) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new JSONObject(params).toString());
    }

    public static RequestBody createJsonRequestBody(Object object) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(object));
    }
}
