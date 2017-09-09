package com.vardhamaninfo.khartargaccha.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.vardhaman.vardhamanutilitylibrary.util.VUSystemUtility;
import com.vardhamaninfo.khartargaccha.Fragment.DadawadiDetailFragment;
import com.vardhamaninfo.khartargaccha.Fragment.SadhuDetailFragment;
import com.vardhamaninfo.khartargaccha.Fragment.SadhviDetailFragment;
import com.vardhamaninfo.khartargaccha.Fragment.TirthDetailFragment;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Util.Constant;

public class DetailScrollingActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    public ImageView ivPhoto;
    private SharedPreferences sharedPreferences;
    String dadawadiID;
    public String userID;
    public String slatitude;
    public String slongitude;
    String page_type;
    String map_address;
    String address1;
    String sadhu_addr;
    public String dest_name;
    public Double latitude, longitude;
    private Fragment fragment;

    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Screen Name");
        toolbar.setTitle("Screen Name");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailScrollingActivity.this);
        dadawadiID = sharedPreferences.getString(Constant.Dadawadi_id, "");

        Log.d("getid", dadawadiID);

        ivPhoto = (ImageView) findViewById(R.id.activity_detail_iv_photo);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        userID = getIntent().getStringExtra("id");
        address1 = getIntent().getStringExtra("address");
        page_type = getIntent().getStringExtra("Page_type");
        latitude = getIntent().getDoubleExtra("lati", 0.0);
        longitude = getIntent().getDoubleExtra("longi", 0.0);

        if (getIntent().hasExtra("username_hindi")) {

            Log.e("username_hindi", "has");

            if (!getIntent().getStringExtra("username_hindi").isEmpty()) {

                dest_name = getIntent().getStringExtra("username_hindi");

                getSupportActionBar().setTitle(dest_name);

                Log.e("username_hindi", dest_name);
            }
        }

        slatitude = latitude.toString();
        slongitude = longitude.toString();

        Log.d("page_type", page_type);
        Log.d("id", userID);
        Log.d("address", address1 + " address1");
        Log.e("address", address1 + " address1");
        Log.d("indetail", "" + latitude);
        Log.d("indetail", "" + longitude);

        changeFragmentByType();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("FloatingActionButton on click", slatitude + "    " + slongitude + "  " + dest_name);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //  startActivity(VUSystemUtility.getInstance(DetailScrollingActivity.this).generateNavigationIntent("0", "0", "26.8992", "75.8338", "Raja park Jaipur"));
                if (slatitude.equals("0.0") && slongitude.equals("0.0")) {

                    Toast.makeText(DetailScrollingActivity.this, getResources().getString(R.string.detail_no_proper_location),
                            Toast.LENGTH_SHORT).show();

                } else {

                    startActivity(VUSystemUtility.getInstance(DetailScrollingActivity.this).generateNavigationIntent("0", "0", slatitude, slongitude, dest_name));
                }
            }
        });
    }

    public void changeFragmentByType() {

        switch (page_type) {

            case "sadhu":

                if (dest_name.isEmpty()) {
                    getSupportActionBar().setTitle("साधु");
                } else {
                    getSupportActionBar().setTitle(dest_name);
                }
                fragment = new SadhuDetailFragment();
                break;

            case "sadhvi":

                if (dest_name.isEmpty()) {
                    getSupportActionBar().setTitle("साध्वी");
                } else {
                    getSupportActionBar().setTitle(dest_name);
                }

                fragment = new SadhviDetailFragment();
                break;

            case "Dadawadi":

                if (dest_name.isEmpty()) {
                    getSupportActionBar().setTitle("दादावाड़ी");
                } else {
                    getSupportActionBar().setTitle(dest_name);
                }

                fragment = new DadawadiDetailFragment();
                break;

            case "Tirth":

                if (dest_name.isEmpty()) {
                    getSupportActionBar().setTitle("तीर्थ");
                } else {
                    getSupportActionBar().setTitle(dest_name);
                }

                fragment = new TirthDetailFragment();
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.activity_detail_fragment, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View v) {
//        startActivity(VUSystemUtility.getInstance(DetailScrollingActivity.this).generateNavigationIntent("0", "0", slatitude, slongitude, dest_name));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();
                return true;

            default:
                return false;
        }
    }
}
