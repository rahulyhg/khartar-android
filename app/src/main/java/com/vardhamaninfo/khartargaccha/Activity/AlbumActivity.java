package com.vardhamaninfo.khartargaccha.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.stfalcon.frescoimageviewer.ImageViewer;
import com.vardhamaninfo.khartargaccha.Adapter.AlbumAdapter;
import com.vardhamaninfo.khartargaccha.Interface.ClickListener;
import com.vardhamaninfo.khartargaccha.Listenter.RecyclerTouchListener;
import com.vardhamaninfo.khartargaccha.Model.Album;
import com.vardhamaninfo.khartargaccha.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    RecyclerView recyclerViewAlbum;
    private List<Album> albumList = new ArrayList<>();
    private AlbumAdapter albumAdapter;
    private String imageList[];
    private String activityTitle;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String albumResponse = getIntent().getStringExtra("Album");
        activityTitle = getIntent().getStringExtra("AlbumName");

        getSupportActionBar().setTitle(activityTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            jsonArray = new JSONArray(albumResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerViewAlbum = (RecyclerView) findViewById(R.id.activity_album_recyclerview);

        albumAdapter = new AlbumAdapter(this, albumList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewAlbum.setLayoutManager(layoutManager);

        recyclerViewAlbum.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAlbum.setAdapter(albumAdapter);

        recyclerViewAlbum.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerViewAlbum, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                new ImageViewer.Builder(AlbumActivity.this, imageList)
                        .setStartPosition(position)
                        .show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        updateAlbumList();
    }

    void updateAlbumList() {

        imageList = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                imageList[i] = jsonObject.getString("gallery_image");

                albumList.add(new Album(jsonObject));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        albumAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return false;
    }
}
