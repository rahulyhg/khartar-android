package com.vardhamaninfo.khartargaccha.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.vardhamaninfo.khartargaccha.Adapter.ViewPagerAdapter;
import com.vardhamaninfo.khartargaccha.Custom.MyPhotoView;
import com.vardhamaninfo.khartargaccha.R;

public class ZoomImageActivity extends AppCompatActivity {
    String url;
    ViewPager vg;
    ViewPagerAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        vg = (ViewPager) findViewById(R.id.view_pager1);
        url = getIntent().getStringExtra("img_url");
        Log.d("img_url", url);
        MyPhotoView mp = new MyPhotoView(ZoomImageActivity.this);
        vpAdapter = new ViewPagerAdapter(ZoomImageActivity.this, url);
        vg.setAdapter(vpAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}