package com.challenge.code.tmgtwittertweets.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.challenge.code.tmgtwittertweets.network.response.Tweet;
import com.challenge.code.tmgtwittertweets.repository.TwitterRepository;

import java.util.List;

/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class TwitterViewModel extends AndroidViewModel implements TwitterRepository.RepositoryCallback {
    private TwitterRepository mRepository;
    private MutableLiveData<Boolean> mProcessing;
    private MutableLiveData<List<Tweet>> mObservableSearchResults;

    /**
     * This viewmodel is the single source of how the views interact with the api.
     */
    public TwitterViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TwitterRepository(application.getApplicationContext(), this);
        mProcessing = new MutableLiveData<>();
        mProcessing.setValue(false);
        mObservableSearchResults = new MutableLiveData<>();
    }

    //TODO: This would be a good case for a Transformation.
    public void SearchTwitter(String keyword){
        mProcessing.setValue(true);
        mRepository.searchTwitterByKeyword(keyword);
    }

    public LiveData<Boolean> getObservableProcessingStatus(){
        return mProcessing;
    }

    public LiveData<List<Tweet>> getObservableTweets(){
        return mObservableSearchResults;
    }

    //We need a way to receive the results to update the livedata.
    //Better solution would be to implement the database to trigger updates when data is saved.
    @Override
    public void onSearchResultReceived(List<Tweet> response) {
        mProcessing.setValue(false);
        mObservableSearchResults.setValue(response);
    }

    //TODO: Return this to the Observer. Update to use NetowkBoundResources.
    @Override
    public void onSearchResultError(String error) {
        mProcessing.setValue(false);
    }
}
