package com.example.android.bakingapp.ui.detail;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.RecipeStep;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class DetailStepFragment extends Fragment {

    public static final String ITEM_ID = "ITEM ID";
    private static final String PLAY_POSITION = "PLAY POSITION";
    private static final String READY_TO_PLAY = "READY TO PLAY";
    private RecipeStep mStep;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    public DetailStepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ITEM_ID)) {
            mStep = getArguments().getParcelable(ITEM_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        TextView descriptionTextView = rootView.findViewById(R.id.recipe_step_detail);

        if (mStep != null) {
            descriptionTextView.setText(mStep.getDescription());
        }

        mPlayerView = rootView.findViewById(R.id.recipe_video);
        long videoPosition = 0L;
        boolean playWhenReady = true;
        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getLong(PLAY_POSITION, videoPosition);
            playWhenReady = savedInstanceState.getBoolean(READY_TO_PLAY, true);
        }
        if (mStep != null) {
            String videoUrl = mStep.getVideoUrl();
            if (!TextUtils.isEmpty(videoUrl)) {
                initializePlayer(Uri.parse(videoUrl), getActivity(), videoPosition, playWhenReady);
            } else {
                String thumbnailUrl = mStep.getThumbnailUrl();
                if (!TextUtils.isEmpty(thumbnailUrl) && thumbnailUrl.endsWith(".mp4")) {
                    initializePlayer(Uri.parse(thumbnailUrl), getActivity(), videoPosition, playWhenReady);
                } else {
                    mPlayerView.setVisibility(View.GONE);
                    descriptionTextView.setVisibility(View.VISIBLE);
                }
            }
        }
        return rootView;
    }

    private void initializePlayer(Uri uri, Context context, long videoPosition,
                                  boolean playWhenReady) {
        if (mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(context, "BakingApp");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            mExoPlayer.prepare(videoSource);
            mExoPlayer.seekTo(videoPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            outState.putLong(PLAY_POSITION, mExoPlayer.getCurrentPosition());
            outState.putBoolean(READY_TO_PLAY, mExoPlayer.getPlayWhenReady());
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null) releasePlayer();
    }
}
