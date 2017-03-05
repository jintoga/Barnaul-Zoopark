package com.dat.barnaulzoopark.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dat.barnaulzoopark.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by DAT on 3/6/2017.
 */

public class YoutubeVideoFragment extends YouTubePlayerSupportFragment
    implements YouTubePlayer.OnInitializedListener {

    private static final String KEY_URL = "URL";

    public static YoutubeVideoFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        YoutubeVideoFragment fragment = new YoutubeVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
        boolean wasRestored) {
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        if (!wasRestored) {
            player.cueVideo(getArguments().getString(KEY_URL));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
        YouTubeInitializationResult youTubeInitializationResult) {
        String errorMessage = youTubeInitializationResult.toString();
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        Log.d("errorMessage:", errorMessage);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(getString(R.string.api_key), this);
    }
}
