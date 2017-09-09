package com.vardhamaninfo.khartargaccha.Retrofit;

import com.vardhamaninfo.khartargaccha.Util.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {

    //TODO For Url
    //main
     String API_BASE_URL = Constant.HOST;
     String API_BASE_URL_YouTube = Constant.HOST_YOUTUBE;

    private Retrofit restAdapter;
    private Retrofit restAdapterYouTue;

    public enum AuthType {
        AUTHARIZED, UNAUTHARIZED
    }

    private RestInterface restInterface;

    public RestAdapter() {
    }

    public RestInterface getRestInterface() {
        restAdapter = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restInterface = restAdapter.create(RestInterface.class);
        return restInterface;
    }

    //Have to decide about the sessionId
    public RestInterface getAuthRestInterface() {

        return restInterface;
    }


    public RestInterface getRestInterfaceYouTube() {
        restAdapterYouTue = new Retrofit.Builder()
                .baseUrl(API_BASE_URL_YouTube)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restInterface = restAdapterYouTue.create(RestInterface.class);
        return restInterface;
    }

}