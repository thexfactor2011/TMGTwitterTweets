package com.challenge.code.tmgtwittertweets.network;

import com.challenge.code.tmgtwittertweets.network.response.SearchResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public interface TwitterApiService {

    //AuthHeader should be Base64(APIKEY:Secret.
    //This will return a access token that can be used to search
    //grantType is always "client_credentials" according to twitter api.
    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<TwitterToken> GetToken(@Header("Authorization") String authHeader, @Field("grant_type") String grantType);

    @GET("/1.1/search/tweets.json")
    Call<SearchResponse> search(@Header("Authorization") String authHeader, @Query("q") String keyword);

}
