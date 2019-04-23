package com.challenge.code.tmgtwittertweets.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class SearchResponse {
    @SerializedName("statuses")
    @Expose
    private List<Tweet> tweets = null;

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setStatuses(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
