package com.challenge.code.tmgtwittertweets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.challenge.code.tmgtwittertweets.adapter.TweetAdapter;
import com.challenge.code.tmgtwittertweets.network.response.Tweet;
import com.challenge.code.tmgtwittertweets.viewmodel.TwitterViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TwitterViewModel mTwitterViewModel;
    TweetAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindToTwitterViewModel();
        setupRecyclerView();
        ImageButton button = findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTwitterViewModel != null){
                    String keyword = ((EditText)findViewById(R.id.editTextKeyword)).getText().toString();
                    mTwitterViewModel.SearchTwitter(keyword);
                }
            }
        });
    }

    private void bindToTwitterViewModel() {
        mTwitterViewModel = ViewModelProviders.of(this).get(TwitterViewModel.class);
        mTwitterViewModel.getObservableProcessingStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean)
                        Toast.makeText(MainActivity.this, "Fetching Tweet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTwitterViewModel.getObservableTweets().observe(this, new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                if(tweets != null){
                    if(tweets.size() > 0){
                        mAdapter.setTweets(tweets);
                        Toast.makeText(MainActivity.this, tweets.size() + " Received", Toast.LENGTH_LONG).show();
                    }else{

                    }
                }
            }
        });
    }

    private void setupRecyclerView(){
        mAdapter = new TweetAdapter(MainActivity.this);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(null);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this,
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
