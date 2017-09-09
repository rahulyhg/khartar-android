package com.vardhamaninfo.khartargaccha.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.AsyncPlayer;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import nl.changer.audiowife.AudioWife;
import retrofit2.Call;
import retrofit2.Response;

public class StavanAudioActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    MediaPlayer mPlayer;
    // ImageView music ;
    ImageView m_stop, mstop1;
    //    TextView stittle;
    TextView sauthor, run_time, total_time;
    String Tittle, author, song;
    // Bundle bundle;
    SharedPreferences s;
    // SurfaceHolder surface;
    //  Handler handler;
    String S_id, id;
    AsyncPlayer ap;
    private SeekBar mMediaSeekBar;
    private ImageView ivStavanAudioPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stavan_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        m_stop = (ImageView) findViewById(R.id.stop);
        mstop1 = (ImageView) findViewById(R.id.stop1);
//        stittle = (TextView) findViewById(R.id.tittle);
        run_time = (TextView) findViewById(R.id.run_time);
        total_time = (TextView) findViewById(R.id.total_time);
        sauthor = (TextView) findViewById(R.id.lyrics);
        mMediaSeekBar = (SeekBar) findViewById(R.id.media_seekbar);
        ivStavanAudioPhoto = (ImageView) findViewById(R.id.ivStavanAudioPhoto);
        //WaitTask();
        s = PreferenceManager.getDefaultSharedPreferences(StavanAudioActivity.this);
        //
        //S_id=getIntent().getStringExtra("satvansangrah");

        id = getIntent().getStringExtra("satvansangrah");
        Log.d("bundle", id);

        //  Log.d("check", S_id);

        getsong();

        //  playmusic();

        /*mPlayer = new MediaPlayer();
        // mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        try {
            mPlayer.setDataSource(getApplicationContext(), myUri1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(StavanAudioActivity.this, "not play song", Toast.LENGTH_SHORT).show();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            mPlayer.prepareAsync();
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }


        m_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
*//*
                if (mPlayer.isPlaying()){

                    mPlayer.pause();

                }
                else if (mPlayer!=null){


                    mPlayer.start();

                }*//*
                if(mPlayer.isPlaying()){
                    if(mPlayer!=null){
                        mPlayer.pause();
                        // Changing button image to play button
                        m_stop.setImageResource(R.mipmap.playb);
                    }
                }else{
                    // Resume song
                    if(mPlayer!=null){
                        mPlayer.start();
                        // Changing button image to pause button
                        m_stop.setImageResource(R.mipmap.pause);
                    }
                }

            }
        });*/

        m_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S_id = s.getString(Constant.S_song, "");
                Log.d("songs", S_id);
                Uri myUri1 = Uri.parse(S_id);
                Log.d("Uri", "" + myUri1);
                //WaitTask();
                AudioWife.getInstance()
                        .init(StavanAudioActivity.this, myUri1)
                        .setPlayView(m_stop)
                        .setPauseView(mstop1)
                        .setSeekBar(mMediaSeekBar)
                        .setRuntimeView(run_time)
                        .setTotalTimeView(total_time);


                AudioWife.getInstance().addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //Toast.makeText(getBaseContext(), "Completed", Toast.LENGTH_SHORT).show();
                        // do you stuff.
                    }
                });

                AudioWife.getInstance().addOnPlayClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getBaseContext(), "Play", Toast.LENGTH_SHORT).show();
                        // get-set-go. Lets dance.
                    }
                });

                AudioWife.getInstance().addOnPauseClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getBaseContext(), "Pause", Toast.LENGTH_SHORT).show();
                        // Your on audio pause stuff.
                    }
                });
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Toast.makeText(StavanAudioActivity.this, "Song is playing", Toast.LENGTH_SHORT).show();
    }

    void getsong() {

        Log.e(" song id ", id + " response");

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getStavanAudioDetail(id);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
                    @Override
                    public void onReturn(boolean success, Response<JsonObject> data) {

                        pDialog.dismiss();

                        if (success) {

                            Log.e("  Response getsong ", data.body().toString() + " response");

                            JSONArray j = null;
                            JSONObject jobject = null;
                            try {

                                JSONObject result = new JSONObject(data.body().toString());


//        new VUBaseAsyncTask(StavanAudioActivity.this, null, true, Constant.HOST + Constant.S_sangrah_single + id,
//                VUBaseAsyncTask.METHOD_GET,
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, JSONObject result) {
//                        if (success == 1) {
//                            JSONObject jobject = null;
//                            try {

                                jobject = result.getJSONObject("stavan_array");
                                Tittle = jobject.getString("title");

                                getSupportActionBar().setTitle(Tittle);

//                                stittle.setText(Tittle);
                                author = jobject.getString("lyrics");
                                sauthor.setText(author);
                                song = jobject.getString("audio_file_url");
                                Log.d("songs", song);
                                s.edit().putString(Constant.S_song, song).apply();

                                String image = jobject.getString("image_file_url");

                                if(image.isEmpty()){

                                    image = "no url";
                                }

                                //image_file_url

                                Glide.with(StavanAudioActivity.this).load(image)
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .placeholder(R.mipmap.logo)
                                        .error(R.mipmap.logo)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(ivStavanAudioPhoto);



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(StavanAudioActivity.this, "Music not play", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        AudioWife.getInstance().pause();

        //mPlayer.stop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                AudioWife.getInstance().pause();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean WaitTask() {
        final ProgressDialog dialog = new ProgressDialog(StavanAudioActivity.this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait..");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = 15000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);
        return true;
    }
}
