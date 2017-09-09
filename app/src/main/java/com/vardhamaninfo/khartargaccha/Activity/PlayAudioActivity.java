package com.vardhamaninfo.khartargaccha.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;

import com.vardhamaninfo.khartargaccha.R;

public class PlayAudioActivity extends AppCompatActivity {
    private SeekBar mMediaSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMediaSeekBar = (SeekBar) findViewById(R.id.media_seekbar);


    }

}
