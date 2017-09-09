package com.vardhamaninfo.khartargaccha.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.Fragment.DadavadiFragment;
import com.vardhamaninfo.khartargaccha.Fragment.EventsFragment;
import com.vardhamaninfo.khartargaccha.Fragment.GalleryFragment;
import com.vardhamaninfo.khartargaccha.Fragment.PachkhanFragment;
import com.vardhamaninfo.khartargaccha.Fragment.PanchangFragment;
import com.vardhamaninfo.khartargaccha.Fragment.SadhuFragment;
import com.vardhamaninfo.khartargaccha.Fragment.SadhviFragment;
import com.vardhamaninfo.khartargaccha.Fragment.SamadhanFragment;
import com.vardhamaninfo.khartargaccha.Fragment.StavansangrahFragment;
import com.vardhamaninfo.khartargaccha.Fragment.StotraFragment;
import com.vardhamaninfo.khartargaccha.Fragment.TirthFragment;
import com.vardhamaninfo.khartargaccha.Fragment.TodayTithiFragment;
import com.vardhamaninfo.khartargaccha.Fragment.VideosListFragment;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Util.Constant;
import com.vardhamaninfo.khartargaccha.Util.StringUtility;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

import static com.vardhamaninfo.khartargaccha.Util.StringUtility.exitApp;

public class Navigation_Drawer_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    // Fragment fragment;
    private SharedPreferences prefs;
    String lati, longi, key_id, email_iid, name;
    Timer mTimer;
    ImageView search;
    public int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        prefs = PreferenceManager.getDefaultSharedPreferences(Navigation_Drawer_Activity.this);
        key_id = prefs.getString(Constant.NEW_ID, "");
        email_iid = prefs.getString(Constant.NEW_EMAIL, "");
        name = prefs.getString(Constant.NEW_NAME, "");
        Log.d("navusername", name);
        Log.d("navuseremail_address", email_iid);
        Log.d("navuser_id", key_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search = (ImageView) toolbar.findViewById(R.id.searching);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Navigation_Drawer_Activity.this, SearchingActivity.class);
                i.putExtra("Type", type);
                startActivity(i);
            }
        });

        //checkLocation();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
       // drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fragmentManager = getSupportFragmentManager();
//        fragment = null;
//        fragment = new TodayTithiFragment();
        search.setVisibility(View.GONE);


//        if (fragment != null) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .addToBackStack("myorder")
//                    .commit();
//        }
        StringUtility stringUtility = new StringUtility();
        stringUtility.replaceFragment(new TodayTithiFragment(), fragmentManager);

        TimerTask scanTask;
        final Handler handler = new Handler();
        mTimer = new Timer();
        scanTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.d("hello", "hello");
                        checkLocation();
                    }
                });
            }
        };

        mTimer.schedule(scanTask, 30000, 30000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("count", "" + count);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (count == 1) {
                //super.onBackPressed();
                // finish();
                exitApp(Navigation_Drawer_Activity.this);

            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    public void checkLocation() {
        Log.d("checkloc", "insideloc");

        //gps testing start

        GpsTracker gps = new GpsTracker(Navigation_Drawer_Activity.this);
        double latitude1 = gps.getLatitude();
        double longitude1 = gps.getLongitude();
        Log.d("updatecheckloc", "" + latitude1 + "" + longitude1);
        Log.d("checkloc", "" + gps.canGetLocation);

        if (gps.canGetLocation()) {
            Log.d("checkloc", "insidegetloc");

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            lati = String.valueOf(latitude);
            longi = String.valueOf(longitude);
            Log.d("updatelati", lati);
            Log.d("updatelongi", longi);
            updateLocation();
            String dl = String.valueOf(latitude);
            String dll = String.valueOf(longitude);
            Log.d("checkloc", dl + "" + dll);
            if (!dl.equals("0.0") && !dll.equals("0.0")) {
                Log.d("gps latitude work fine", String.valueOf(latitude));

            } else {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                //changeGPS();
                Log.d("gps latitude error", String.valueOf(latitude));
                Log.d("gps longitude error", String.valueOf(longitude));
            }
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
            //changeGPS();
        }
        //gps testing end
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        StringUtility stringUtility = new StringUtility();
        fragmentManager = getSupportFragmentManager();

        if (id == R.id.sadu_sadhvi) {
            //Toast.makeText(Navigation_Drawer_Activity.this,"hello",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
            //       fragment = new SadhuFragment();
            search.setVisibility(View.VISIBLE);

            //     if (fragment != null) {
            //          fragmentManager.beginTransaction()
            //                .replace(R.id.container, fragment)
//                        .addToBackStack("myorder")
            //                   .commit();
            //    }

            stringUtility.replaceFragment(new SadhuFragment(), fragmentManager);

        } else if (id == R.id.sadhvi) {
            //Toast.makeText(Navigation_Drawer_Activity.this,"hello",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new SadhviFragment();
            search.setVisibility(View.VISIBLE);
            stringUtility.replaceFragment(new SadhviFragment(), fragmentManager);

//            if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }
        } else if (id == R.id.panchag) {
            //Toast.makeText(Navigation_Drawer_Activity.this,"panchang",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new PanchangFragment();
            search.setVisibility(View.GONE);
            stringUtility.replaceFragment(new PanchangFragment(), fragmentManager);

//            if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }
        } else if (id == R.id.address) {

            //Toast.makeText(Navigation_Drawer_Activity.this,"Dadavadi new",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new DadavadiFragment();

            search.setVisibility(View.VISIBLE);

            stringUtility.replaceFragment(new DadavadiFragment(), fragmentManager);
//            search.setVisibility(View.GONE);

            //   if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }
        } else if (id == R.id.tirth) {

            //Toast.makeText(Navigation_Drawer_Activity.this,"Tirth new",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new TirthFragment();

            search.setVisibility(View.VISIBLE);

            stringUtility.replaceFragment(new TirthFragment(), fragmentManager);
//            search.setVisibility(View.GONE);

//            if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }
        } else if (id == R.id.stavansangrah) {

            //Toast.makeText(Navigation_Drawer_Activity.this,"Stavan",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new StavansangrahFragment();
            search.setVisibility(View.GONE);

            stringUtility.replaceFragment(new StavansangrahFragment(), fragmentManager);

//            if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }

        } else if (id == R.id.pachakhan) {

            //Toast.makeText(Navigation_Drawer_Activity.this,"Stavan",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new PachkhanFragment();
            search.setVisibility(View.GONE);

            stringUtility.replaceFragment(new PachkhanFragment(), fragmentManager);

//            if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }
        } else if (id == R.id.stotra) {

            //Toast.makeText(Navigation_Drawer_Activity.this,"Stavan",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new StotraFragment();
            search.setVisibility(View.GONE);

            stringUtility.replaceFragment(new StotraFragment(), fragmentManager);

//            if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }
        } else if (id == R.id.chating) {

            //Toast.makeText(Navigation_Drawer_Activity.this,"events",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new SamadhanFragment();
            search.setVisibility(View.GONE);

            stringUtility.replaceFragment(new SamadhanFragment(), fragmentManager);

//            if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }
        } else if (id == R.id.events) {

            //Toast.makeText(Navigation_Drawer_Activity.this,"events",Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new EventsFragment();
            search.setVisibility(View.GONE);

            stringUtility.replaceFragment(new EventsFragment(), fragmentManager);

//            if (fragment != null) {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment)
////                        .addToBackStack("myorder")
//                        .commit();
//            }
        } else if (id == R.id.photos) {

            //Toast.makeText(Navigation_Drawer_Activity.this, "Photos", Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new GalleryFragment();
            search.setVisibility(View.GONE);

            stringUtility.replaceFragment(new GalleryFragment(), fragmentManager);

            // if (fragment != null) {
            //            fragmentManager.beginTransaction()
            //                     .replace(R.id.container, fragment)
//                        .addToBackStack("myorder")
            //                     .commit();
            // }
        } else if (id == R.id.today) {

            //Toast.makeText(Navigation_Drawer_Activity.this, "Photos", Toast.LENGTH_LONG).show();
//            fragmentManager = getSupportFragmentManager();
//            fragment = null;
//            fragment = new TodayTithiFragment();
            search.setVisibility(View.GONE);

            stringUtility.replaceFragment(new TodayTithiFragment(), fragmentManager);

            //   if (fragment != null) {
            //    fragmentManager.beginTransaction()
            //           .replace(R.id.container, fragment)
//                        .addToBackStack("myorder")
            //          .commit();
            //  }

        } else if (id == R.id.sadh_logout) {

            prefs.edit().putString(Constant.IS_LOGIN, "0").apply();
            prefs.edit().putString(Constant.NEW_ID, "").apply();
            prefs.edit().putString(Constant.NEW_NAME, "").apply();
            prefs.edit().putString(Constant.NEW_EMAIL, "").apply();
            mTimer.cancel();
            Intent i = new Intent(Navigation_Drawer_Activity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (id == R.id.youtube) {

            search.setVisibility(View.GONE);

            stringUtility.replaceFragment(new VideosListFragment(), fragmentManager);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void updateLocation() {
        Log.d("updateinsideparser", "inside");


        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .userUpdateLocation(lati, longi,key_id);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                if (success) {

                    Log.d("updateparser", "updated Successfully");

                }
            }

        });



       /* new VUBaseAsyncTask(Navigation_Drawer_Activity.this, null, false,
                Constant.HOST + Constant.Update_loc + key_id, VUBaseAsyncTask.METHOD_POST,
                new String[]{"latitude", "longitude"},
                new String[]{lati, longi}, new VUIBaseAsyncTask() {
            @Override
            public void onTaskCompleted(int success, JSONObject result) {
                if (success == 1) {
                    Log.d("updateparser", "updated Successfully");
                }
            }
        }
        ).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);*/
    }


}
