package com.challenge.code.tmgtwittertweets;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

        //Track The network status. We cannot pull tweets without ti.
        bindToNetwork();

        //Handles the tweets that match the keyword.
        bindToTwitterViewModel();

        setupRecyclerView();

        EditText editText = findViewById(R.id.editTextKeyword);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Identifier of the action. This will be either the identifier you supplied,
                // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    ImageButton button = findViewById(R.id.btnSearch);
                    button.performClick();
                    return true;
                }
                // Return true if you have consumed the action, else false.
                return false;
            }
        });

        //TODO: Use Databinding.
        ImageButton button = findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwitterViewModel != null) {
                    EditText editText = ((EditText) findViewById(R.id.editTextKeyword));
                    String keyword = editText.getText().toString();
                    if ("".equals(keyword)){
                        Toast.makeText(MainActivity.this, getString(R.string.empty_keyword_error), Toast.LENGTH_LONG).show();
                    }else{
                        mTwitterViewModel.SearchTwitter(keyword);
                        hideKeyboard(MainActivity.this, editText);
                    }
                }
            }
        });
    }

    /**
     * I choose to use viewmodels because the data persists even after a screen rotation.
     */
    private void bindToTwitterViewModel() {

        //This is a status that we are making a request.
        //TODO: Use Network Bound resource.
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

        //This updates the UI based on how many tweets we get from the API
        mTwitterViewModel.getObservableTweets().observe(this, new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                if (tweets != null) {
                    if (tweets.size() > 0) {
                        findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
                        findViewById(R.id.noTweetsLayout).setVisibility(View.GONE);
                        mAdapter.setTweets(tweets);
                        //Toast.makeText(MainActivity.this, tweets.size() + " Received", Toast.LENGTH_LONG).show();
                    } else {
                        findViewById(R.id.recyclerView).setVisibility(View.GONE);
                        findViewById(R.id.noTweetsLayout).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * We shouldnt let the user make a request when they have no network.
     * This will lock the button and display a snackbar saying why they cant act.
     * It will automatically update the UI once the network is restored.
     */
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

    private void setupRecyclerView() {
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

    public void hideKeyboard(Activity activity, View view) {
        if (activity != null && view != null) {
            final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
