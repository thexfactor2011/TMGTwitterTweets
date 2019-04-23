package com.challenge.code.tmgtwittertweets.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.challenge.code.tmgtwittertweets.network.response.SearchResponse;
import com.challenge.code.tmgtwittertweets.network.response.Tweets;
import com.challenge.code.tmgtwittertweets.repository.TwitterRepository;

import java.util.List;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class TwitterViewModel extends AndroidViewModel implements TwitterRepository.RepositoryCallback {
    private TwitterRepository mRepository;
    private MutableLiveData<Boolean> mProcessing;
    private MutableLiveData<List<Tweets>> mObservableSearchResults;

    public TwitterViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TwitterRepository(application.getApplicationContext(), this);
        mProcessing = new MutableLiveData<>();
        mProcessing.setValue(false);
        mObservableSearchResults = new MutableLiveData<>();
    }

    public void SearchTwitter(String keyword){
        mProcessing.setValue(true);
        mRepository.searchTwitterByKeyword(keyword);
    }

    public LiveData<Boolean> getObservableProcssingStatus(){
        return mProcessing;
    }

    @Override
    public void onSearchResultReceived(List<Tweets> response) {
        mProcessing.setValue(false);
        mObservableSearchResults.setValue(response);
    }

    @Override
    public void onSearchResultError(String error) {
        mProcessing.setValue(false);
    }
}
