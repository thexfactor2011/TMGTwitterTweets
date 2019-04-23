package com.challenge.code.tmgtwittertweets.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.challenge.code.tmgtwittertweets.R;
import com.challenge.code.tmgtwittertweets.network.response.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry Anuszewski on 4/23/2019.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {
    private List<Tweet> mTweets;
    Context mContext;

    public TweetAdapter(Context context) {
        mTweets = new ArrayList<>();
        mContext = context;
    }

    public void setTweets(List<Tweet> newTweets){
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new TweetDiffCallback(mTweets,newTweets));
        mTweets.clear();
        mTweets.addAll(newTweets);
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public TweetAdapter.TweetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for this fragment
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tweet_row, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetViewHolder holder, int position) {
        holder.setTweet(mTweets.get(position));
    }

    @Override
    public int getItemCount() {
        return mTweets == null ? 0 : mTweets.size();
    }

    class TweetDiffCallback extends DiffUtil.Callback {
        private List<Tweet> mOldTweets;
        private List<Tweet> mNewTweets;

        public TweetDiffCallback(List<Tweet> oldTweets, List<Tweet> newTweets) {
            mOldTweets = oldTweets;
            mNewTweets = newTweets;
        }

        @Override
        public int getOldListSize() {
            return mOldTweets == null ? 0 : mOldTweets.size();
        }

        @Override
        public int getNewListSize() {
            return mNewTweets == null ? 0 : mNewTweets.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            if (mOldTweets.get(oldItemPosition) == null || mNewTweets.get(newItemPosition) == null) {
                return false;
            }
            return mOldTweets.get(oldItemPosition).getId().equals(mNewTweets.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            if (mOldTweets.get(oldItemPosition) == null || mNewTweets.get(newItemPosition) == null) {
                return false;
            }
            return mOldTweets.get(oldItemPosition).getText().equals(mNewTweets.get(newItemPosition).getText());
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // Implement method if you're going to use ItemAnimator
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

    class TweetViewHolder extends RecyclerView.ViewHolder {
        ImageView userLogo;
        TextView userName;
        TextView tweet;

        Tweet mTweet;

        public TweetViewHolder(@NonNull View itemView) {
            super(itemView);
            userLogo = itemView.findViewById(R.id.image_user_logo);
            userName = itemView.findViewById(R.id.text_user);
            tweet = itemView.findViewById(R.id.text_tweet);
        }

        public void setTweet(Tweet tweet) {
            mTweet = tweet;
            setProfileImage();
            userName.setText(tweet.getUser().getName());
            this.tweet.setText(tweet.getText());
        }

        private void setProfileImage() {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mTweet.getUser().getProfileImageUrl())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            userLogo.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }
}
