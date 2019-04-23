package com.challenge.code.tmgtwittertweets.network.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class Description {
    @SerializedName("urls")
    @Expose
    private List<Object> urls = null;

    public List<Object> getUrls() {
        return urls;
    }

    public void setUrls(List<Object> urls) {
        this.urls = urls;
    }
}
