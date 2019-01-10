package com.example.shakil.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPrev;
    private Button btnFastBack;
    private Button btnPlay;
    private Button btnFastForward;
    private Button btnNext;

    private SeekBar seekBar;

    Thread updateSeekBar;

    int position;
    Uri uri;
    static MediaPlayer mediaPlayer;
    ArrayList<File> mySongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnPrev = findViewById(R.id.btn_prev);
        btnFastBack = findViewById(R.id.btn_fast_back);
        btnPlay = findViewById(R.id.btn_play);
        btnFastForward = findViewById(R.id.btn_fast_forward);
        btnNext = findViewById(R.id.btn_next);

        seekBar = findViewById(R.id.seekBar_position_bar);

        btnPlay.setOnClickListener(this);
        btnFastForward.setOnClickListener(this);
        btnFastBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);

        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration){
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();



        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");


        position = bundle.getInt("pos",0);

        uri = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);

        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btn_play:
                if(mediaPlayer.isPlaying()){
                    btnPlay.setText(">");
                    mediaPlayer.pause();
                }
                else {
                    btnPlay.setText("||");
                    mediaPlayer.start();
                }
                break;

            case R.id.btn_fast_back:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;

            case R.id.btn_fast_forward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                break;

            case R.id.btn_next:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position+1)%mySongs.size();
                uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();

                break;

            case R.id.btn_prev:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position-1<0)? mySongs.size()-1:position-1;
                uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();

                break;
        }
    }
}
