package com.challenge.code.tmgtwittertweets.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class TweetClient {
    private static Retrofit retrofit = null;
    private static TwitterApiService apiService = null;
    private static String mBaseURL = "https://api.twitter.com";

    private static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient client = getHttpClient();
            Gson gson = new GsonBuilder()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(mBaseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // FOR DEBUGGING ONLY!
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient.addInterceptor(interceptor).build();

        return httpClient.build();
    }

    public static TwitterApiService getApiService() {
        if (apiService == null) {
            apiService = getClient().create(TwitterApiService.class);
        }
        return apiService;
    }
}
