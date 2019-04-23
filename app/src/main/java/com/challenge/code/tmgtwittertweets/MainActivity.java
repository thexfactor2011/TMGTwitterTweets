package com.challenge.code.tmgtwittertweets;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.challenge.code.tmgtwittertweets.adapter.TweetAdapter;
import com.challenge.code.tmgtwittertweets.connectivity.ConnectionModel;
import com.challenge.code.tmgtwittertweets.connectivity.ConnectivityLiveData;
import com.challenge.code.tmgtwittertweets.network.response.Tweet;
import com.challenge.code.tmgtwittertweets.viewmodel.TwitterViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TwitterViewModel mTwitterViewModel;
    TweetAdapter mAdapter;
    private Snackbar mNetworkSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindToNetwork();
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

    private void bindToNetwork() {
        final ConnectivityLiveData connectionLiveData = new ConnectivityLiveData(getApplicationContext());
        connectionLiveData.observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(@Nullable ConnectionModel connection) {
                if (connection != null) {
                    if (connection.getIsConnected()) {
                        if (mNetworkSnackbar != null && mNetworkSnackbar.isShown()) {
                            mNetworkSnackbar.dismiss();
                            findViewById(R.id.btnSearch).setEnabled(true);
                        }
                    } else {
                        if (mNetworkSnackbar == null) {
                            mNetworkSnackbar = Snackbar.make(findViewById(R.id.layoutRoot), getString(R.string.no_network_available), Snackbar.LENGTH_INDEFINITE);
                        }
                        findViewById(R.id.btnSearch).setEnabled(false);
                        mNetworkSnackbar.show();
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
