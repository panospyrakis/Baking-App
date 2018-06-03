package com.udacity.spyrakis.bakingapp.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
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
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
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

    private SimpleExoPlayer player;
    private ArrayList<StepsItem> steps;
    private StepsItem step;
    private String url;
    private int position;

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

        position = getIntent().getIntExtra(ARG_POSITION,0);
        steps = getIntent().getParcelableArrayListExtra(ARG_STEPS);
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

    }

    private void setContent(){
        step = steps.get(position);
        recipeInstructions.setText(step.getDescription());
        url = step.getVideoURL();
    }

    private void checkNextButton(){
        if (steps.size() - 1 == position){
            nextButton.setEnabled(false);
        }else{
            nextButton.setEnabled(true);
        }
    }

    private void checkPreviousButton(){
        if (0 == position){
            previousButton.setEnabled(false);
        }else{
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

