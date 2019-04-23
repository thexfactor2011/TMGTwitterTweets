package com.challenge.code.tmgtwittertweets.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class TwitterToken {
    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("access_token")
    public String accessToken;
}
