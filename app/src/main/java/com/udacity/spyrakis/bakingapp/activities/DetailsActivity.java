package com.udacity.spyrakis.bakingapp.activities;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.models.StepsItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends BaseActivity {


    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    public static final String ARG_STEPS = "ARG_STEPS";
    public static final String ARG_POSITION = "ARG_POSITION";
    public static final String ARG_PLAY_WHEN_READY = "ARG_PLAY_WHEN_READY";
    public static final String SELECTED_POSITION = "SELECTED_POSITION";


    @BindView(R.id.exo_cntlr)
    ImageView ivHideControllerButton;

    @BindView(R.id.player_view)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.recipe_instructions)
    TextView recipeInstructions;

    @BindView(R.id.next_button)
    Button nextButton;

    @BindView(R.id.previous_button)
    Button previousButton;

    @BindView(R.id.nav_container)
    FrameLayout navContainer;

    @BindView(R.id.root)
    LinearLayout root;

    @BindView(R.id.thumbnail_image)
    ImageView thumbnail;

    private SimpleExoPlayer player;
    private ArrayList<StepsItem> steps;
    private StepsItem step;
    private String url;
    private int position;
    private long videoPosition = C.TIME_UNSET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            position = getIntent().getIntExtra(ARG_POSITION, 0);
            steps = getIntent().getParcelableArrayListExtra(ARG_STEPS);
        } else {
            position = savedInstanceState.getInt(ARG_POSITION);
            steps = savedInstanceState.getParcelableArrayList(ARG_STEPS);
        }

        setContent();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = ++position;
                releasePlayer();
                setContent();
                checkNextButton();
                checkPreviousButton();
                initializePlayer();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = --position;
                releasePlayer();
                setContent();
                checkNextButton();
                checkPreviousButton();
                initializePlayer();
            }
        });

        checkNextButton();
        checkPreviousButton();

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            navContainer.setVisibility(View.GONE);
            recipeInstructions.setVisibility(View.GONE);
            actionBar.hide();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
            FrameLayout.LayoutParams rootParams = (FrameLayout.LayoutParams) root.getLayoutParams();
            params.height = rootParams.height;
            simpleExoPlayerView.setLayoutParams(params);

        }

    }

    private void setContent() {
        step = steps.get(position);
        recipeInstructions.setText(step.getDescription());
        url = step.getVideoURL();
        String thumbnailUrl = step.getThumbnailURL();
        if (thumbnailUrl != null && !thumbnailUrl.equals("")) {
            Picasso.get().load(step.getThumbnailURL()).into(thumbnail);
        } else {
            thumbnail.setVisibility(View.GONE);
        }
    }

    private void checkNextButton() {
        if (steps.size() - 1 == position) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }
    }

    private void checkPreviousButton() {
        if (0 == position) {
            previousButton.setEnabled(false);
        } else {
            previousButton.setEnabled(true);
        }
    }

    private void initializePlayer() {

        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);

        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayerView.hideController();
            }
        });

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    thumbnail.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (videoPosition != C.TIME_UNSET) {
                player.seekTo(videoPosition);
                player.setPlayWhenReady(shouldAutoPlay);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
            if (videoPosition != C.TIME_UNSET) {
                player.seekTo(videoPosition);
                player.setPlayWhenReady(shouldAutoPlay);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(SELECTED_POSITION, player.getCurrentPosition());
        outState.putInt(ARG_POSITION, position);
        outState.putParcelableArrayList(ARG_STEPS, steps);
        outState.putBoolean(ARG_PLAY_WHEN_READY, player.getPlayWhenReady());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        videoPosition = savedInstanceState.getLong(SELECTED_POSITION);
        shouldAutoPlay = savedInstanceState.getBoolean(ARG_PLAY_WHEN_READY);
        if (videoPosition != C.TIME_UNSET) {
            player.seekTo(videoPosition);
            player.setPlayWhenReady(shouldAutoPlay);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

