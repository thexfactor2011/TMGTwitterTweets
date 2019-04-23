package com.challenge.code.tmgtwittertweets.repository;

import android.content.Context;
import android.util.Base64;

import com.challenge.code.tmgtwittertweets.R;
import com.challenge.code.tmgtwittertweets.network.TweetClient;
import com.challenge.code.tmgtwittertweets.network.TwitterApiService;
import com.challenge.code.tmgtwittertweets.network.TwitterToken;
import com.challenge.code.tmgtwittertweets.network.response.SearchResponse;
import com.challenge.code.tmgtwittertweets.network.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class TwitterRepository {
    private final TwitterApiService mApiService;
    private String mAuthToken;
    private Context mContext;
    private String mKeyword = "";
    private RepositoryCallback mCallback;

    public TwitterRepository(Context context, RepositoryCallback callback){
        mApiService = TweetClient.getApiService();
        mAuthToken = "";
        mContext = context;
        mCallback = callback;
    }

    //Testing constructor
    public TwitterRepository(TwitterApiService service, String authToken, RepositoryCallback callback){
        mApiService = service;
        mAuthToken = authToken;
        mCallback = callback;
    }

    public void searchTwitterByKeyword(String keyword){
        mKeyword = keyword;
        if(mAuthToken != null && !"".equals(mAuthToken)) {
            TweetClient.getApiService().search(mAuthToken, keyword).enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    if(response.code() == 200){
                        if(response.body() != null){
                            if(mCallback != null){
                                mCallback.onSearchResultReceived(response.body().getTweets());
                            }
                        }else{
                            //Body is null
                        }
                    }else{
                        //Invalid response code.
                        //If 401 get new token.
                    }
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    if(mCallback != null){
                        mCallback.onSearchResultError(t.getMessage());
                    }
                }
            });
        }else{
            getApiToken();
        }
    }

    private void getApiToken(){
        String key = mContext.getString(R.string.com_twitter_sdk_android_CONSUMER_KEY);
        String secret = mContext.getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET);
        //TODO: Check if these are valued, if not we need to display an error.
        byte[] bytes = (key +":"+secret).getBytes();
        String header = "Basic " + Base64.encodeToString(bytes, Base64.NO_WRAP);

        TweetClient.getApiService().GetToken(header, "client_credentials").enqueue(new Callback<TwitterToken>() {
            @Override
            public void onResponse(Call<TwitterToken> call, Response<TwitterToken> response) {
                String type = response.body().tokenType;
                String token = response.body().accessToken;
                mAuthToken = type + " " + token;
                searchTwitterByKeyword(mKeyword);
            }

            @Override
            public void onFailure(Call<TwitterToken> call, Throwable t) {
                if(mCallback != null){
                    mCallback.onSearchResultError(t.getMessage());
                }
            }
        });
    }

    public interface RepositoryCallback{
        void onSearchResultReceived(List<Tweet> tweets);

        void onSearchResultError(String error);
    }
}
