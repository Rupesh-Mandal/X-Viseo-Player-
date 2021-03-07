package com.kali.xvideoplayer.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kali.xvideoplayer.R;
import com.kali.xvideoplayer.model.VideoFiles;


import java.util.ArrayList;

import static com.kali.xvideoplayer.adapter.VideoAdapter.videoFilesArrayList;
import static com.kali.xvideoplayer.adapter.VideoFolderAdapter.videoFoldeoFiles;

public class PlayerActivity extends AppCompatActivity{

    PlayerView playerView;
    ExoPlayer simpleExoPlayer;
    int position=-1;
    String path;
    String sender;
    ArrayList<VideoFiles> myVideoLiset=new ArrayList<>();
    ImageView netBtn,prevBtn;
    TextView fileName;
    public SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullSceen();
        setContentView(R.layout.activity_player);
        getSupportActionBar().hide();
        sender=getIntent().getStringExtra("sender");
        if (sender.equals("folder")) {
            myVideoLiset = videoFoldeoFiles;
        } else {
            myVideoLiset = videoFilesArrayList;
        }
        initView();


    }

    private void initView() {

        playerView=findViewById(R.id.exoplayer_movie);
        netBtn=playerView.findViewById(R.id.next_btn);
        prevBtn=playerView.findViewById(R.id.previous_btn);
        fileName=playerView.findViewById(R.id.fileNamePlaying);
        seekBar=playerView.findViewById(R.id.seek_progress);
        position=getIntent().getIntExtra("position",-1);
        seekBar.setActivated(true);
        path=myVideoLiset.get(position).getPath();
        playVideo(path,true);

        setOnClicLisner();

    }

    private void setOnClicLisner() {
        netBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleExoPlayer.isPlaying()){
                    simpleExoPlayer.stop();
                    simpleExoPlayer.release();
                    position=((position + 1) % myVideoLiset.size());
                    path=myVideoLiset.get(position).getPath();
                    playVideo(path,true);

                }else {
                    simpleExoPlayer.stop();
                    simpleExoPlayer.release();
                    position=((position + 1) % myVideoLiset.size());
                    path=myVideoLiset.get(position).getPath();
                    playVideo(path,false);
                }
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleExoPlayer.isPlaying()) {
                    simpleExoPlayer.stop();
                    simpleExoPlayer.release();
                    position = ((position - 1) < 0 ? (myVideoLiset.size() - 1) : (position - 1));
                    path=myVideoLiset.get(position).getPath();
                    playVideo(path,true);
                }else {
                    simpleExoPlayer.stop();
                    simpleExoPlayer.release();
                    position = ((position - 1) < 0 ? (myVideoLiset.size() - 1) : (position - 1));
                    path=myVideoLiset.get(position).getPath();
                    playVideo(path,false);
                }

            }
        });
    }

    private void playVideo(String pathOfVideo,Boolean cundition) {
        if (pathOfVideo!=null){
            fileName.setText(myVideoLiset.get(position).getTitle());
            Uri uri=Uri.parse(pathOfVideo);
            simpleExoPlayer=new SimpleExoPlayer.Builder(this).build();
            DataSource.Factory factory=new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this,"X_Video_Player"));
            ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
            MediaSource mediaSource=new ProgressiveMediaSource.Factory(factory,extractorsFactory).createMediaSource(uri);
            playerView.setPlayer(simpleExoPlayer);
            playerView.setKeepScreenOn(true);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(cundition);
            seekBar.setMax((int) simpleExoPlayer.getDuration());
            seekBar.setMin(0);
            simpleExoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, int reason) {
                    Log.e("xxxx","onTimelineChanged");
                    seekBar.setProgress((int) simpleExoPlayer.getCurrentPosition());

                }

                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                    Log.e("xxxx","");
                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                    Log.e("xxxx","onTracksChanged");
                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    Log.e("xxxx","onLoadingChanged");
                    seekBar.setProgress((int) simpleExoPlayer.getCurrentPosition());
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Log.e("xxxx","onPlayerStateChanged");
                }

                @Override
                public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
                    Log.e("xxxx","onPlaybackSuppressionReasonChanged");
                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    Log.e("xxxx","onIsPlayingChanged");
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                    Log.e("xxxx","onRepeatModeChanged");
                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                    Log.e("xxxx","onShuffleModeEnabledChanged");
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Log.e("xxxx","onPlayerError");
                }

                @Override
                public void onPositionDiscontinuity(int reason) {
                    Log.e("xxxx","onPositionDiscontinuity");
                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                    Log.e("xxxx","onPlaybackParametersChanged");
                }

                @Override
                public void onSeekProcessed() {
                    Log.e("xxxx","onSeekProcessed");
                }
            });


        }
    }

    private void setFullSceen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
    }




}