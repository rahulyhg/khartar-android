package com.vardhamaninfo.khartargaccha.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.util.VUStringUtility;
import com.vardhaman.vardhamanutilitylibrary.util.VUSystemUtility;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class DadaWadiDetailActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    TextView d_name, d_address, group_name, diksha_date, mob_no, title_name, last_seen, open_map;
    SharedPreferences sharedPreferences;
    String id, page_type, u_id, map_address, address1, sadhu_addr, dest_name, latin, longin;
    View gr_view, diksha_view;
    Double lat, lon;
    CameraPosition cameraPosition;
    LinearLayout lay_last_seen, g_name_lay, diksh_dte_lay, name_lay, address_lay, mobile_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dada_wadi_detail);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DadaWadiDetailActivity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*ActionBar actionBar = getActionBar();
        actionBar.show();*/


        id = sharedPreferences.getString(Constant.Dadawadi_id, "");
        Log.d("getid", id);
        d_name = (TextView) findViewById(R.id.name);
        d_address = (TextView) findViewById(R.id.address);
        group_name = (TextView) findViewById(R.id.gname);
        diksha_date = (TextView) findViewById(R.id.diksha);
        mob_no = (TextView) findViewById(R.id.mob_no);
        last_seen = (TextView) findViewById(R.id.last_seen);
        title_name = (TextView) findViewById(R.id.title_name);
        gr_view = (View) findViewById(R.id.group_view);
        diksha_view = (View) findViewById(R.id.diksha_view);
        lay_last_seen = (LinearLayout) findViewById(R.id.lay_last_seen);
        g_name_lay = (LinearLayout) findViewById(R.id.g_name_lay);
        diksh_dte_lay = (LinearLayout) findViewById(R.id.diksh_dte_lay);
        name_lay = (LinearLayout) findViewById(R.id.name_lay);
        address_lay = (LinearLayout) findViewById(R.id.address_lay);
        mobile_lay = (LinearLayout) findViewById(R.id.mobile_lay);
        open_map = (TextView) findViewById(R.id.open_map);
        open_map.setOnClickListener(this);

        u_id = getIntent().getStringExtra("id");
        address1 = getIntent().getStringExtra("address");
        page_type = getIntent().getStringExtra("Page_type");
        Log.d("page_type", page_type);
        Log.d("id", u_id);
        Log.d("address", address1);
        lat = getIntent().getDoubleExtra("lati", 0.0);
        lon = getIntent().getDoubleExtra("longi", 0.0);
        Log.d("indetail", "" + lat);
        Log.d("indetail", "" + lon);

        latin = String.valueOf(lat);
        longin = String.valueOf(lon);
        if (VUStringUtility.stringNotEmpty(latin)) {
            Log.d("inif", "inif");
            sadhu_addr = getCompleteAddressString(lat, lon);

            address_lay.setVisibility(View.VISIBLE);


        } else {
            Log.d("inelse", "inelse");
            d_address.setText(address1);

            address_lay.setVisibility(View.VISIBLE);
        }

        if (page_type.equals("Tirth")) {
            Singletirthdetail(u_id);

        } else if (page_type.equals("Dadawadi")) {

            getsingledadawadi(u_id);
        } else {
            getsinglesadhu(u_id);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        cameraPosition = CameraPosition.builder()
                .target(sydney)
                .zoom(13)
                .bearing(90)
                .build();

        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);

    }

    void getsingledadawadi(String user_id) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSignalDadawadiDetails(user_id);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());

//        new VUBaseAsyncTask(this, null, true, Constant.HOST + Constant.single_Get_Dadawadi + user_id, VUBaseAsyncTask.METHOD_GET,
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//
//                        if (success == 1) {
//
//                            JSONArray jsonArray = null;
//                            try {
//
//                                JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject = result.getJSONObject("place_array");
                        String s_id = jsonObject.getString("id");
                        Log.d("sid", s_id);
                        dest_name = jsonObject.getString("name");
                        String s_name = jsonObject.getString("name");
                        String m_no = jsonObject.getString("mobile_number");

                        if (!VUStringUtility.stringNotEmpty(m_no)) {
                            mobile_lay.setVisibility(View.GONE);
                        } else {
                            mobile_lay.setVisibility(View.VISIBLE);
                            mob_no.setText(m_no);
                        }

                        Log.d("name", s_name);
                        d_name.setText(s_name);
                        title_name.setText(s_name);
                        map_address = jsonObject.getString("address");

                        //onMapReady(mMap);
                        //d_address.setText(map_address);
                        Log.d("address", map_address);

                        String state_id = jsonObject.getString("state_id");
                        Log.d("state", state_id);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(DadaWadiDetailActivity.this, getResources().getString(R.string.try_Again), Toast.LENGTH_SHORT).show();
                }
            }


        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }


    public void Singletirthdetail(String uid) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSingleTirth(uid);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());


//        new VUBaseAsyncTask(DadaWadiDetailActivity.this, null, true, Constant.HOST + Constant.SINGLETIRTH + uid, VUBaseAsyncTask.METHOD_GET, new VUIBaseAsyncTask() {
//            @Override
//            public void onTaskCompleted(int success, JSONObject result) {
//                if (success == 1) {
//
//                    JSONObject jsonObject = null;
//                    try {
                        JSONObject jsonObject1 = result.getJSONObject("place_array");
                        dest_name = jsonObject1.getString("name");
                        String stname = jsonObject1.getString("name");
                        String m_no = jsonObject1.getString("mobile_number");
                        Log.v("tirth", stname);
                        d_name.setText(stname);
                        title_name.setText(stname);

                        if (!VUStringUtility.stringNotEmpty(m_no)) {
                            mobile_lay.setVisibility(View.GONE);
                        } else {
                            mobile_lay.setVisibility(View.VISIBLE);
                            mob_no.setText(m_no);
                        }


                        //mob_no.setText(m_no);

                        map_address = jsonObject1.getString("address");

                        Log.v("taddress", map_address);
                        //d_address.setText(map_address);
/*sharedPreferences.edit().putString(Costant.SADHUTIRTHNAME,stname);
         sharedPreferences.edit().putString(Costant.SADHUTIRTHADDRESS,staddress);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(DadaWadiDetailActivity.this, getResources().getString(R.string.try_Again), Toast.LENGTH_SHORT).show();
                }
            }
        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

    }


    void getsinglesadhu(String userid) {


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSingleSadhuDetail(userid);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());


//                        new VUBaseAsyncTask(this, null, true, Constant.HOST + Constant.Single_uid + userid, VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//                            JSONArray jsonArray = null;
//                            try {

                        diksha_view.setVisibility(View.VISIBLE);
                        diksh_dte_lay.setVisibility(View.VISIBLE);
                        diksha_date.setVisibility(View.VISIBLE);
                        last_seen.setVisibility(View.VISIBLE);
                        lay_last_seen.setVisibility(View.VISIBLE);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject = result.getJSONObject("user_array");
                        String s_id = jsonObject.getString("user_id");
                        Log.d("uid", s_id);
                        String g_id = jsonObject.getString("group_id");
                        Log.d("ugid", g_id);
                        String s_name = jsonObject.getString("username");
                        dest_name = jsonObject.getString("username");
                        String lasts = jsonObject.getString("modified_date");
                        String addr = jsonObject.getString("address");
                        Log.d("sadhuaddr", addr);
                        //d_address.setText(addr);
                        last_seen.setText(lasts);
                        // last_seen.setText(lasts);
                        Log.d("uname", s_name);
                        d_name.setText(s_name);
                        title_name.setText(s_name);
                        String u_mob = jsonObject.getString("mobile_number");
                        Log.d("mob", u_mob);
                        if (!VUStringUtility.stringNotEmpty(u_mob)) {
                            mobile_lay.setVisibility(View.GONE);
                        } else {
                            mobile_lay.setVisibility(View.VISIBLE);
                            mob_no.setText(u_mob);
                        }


                        String diksha = jsonObject.getString("diksha_date");
                        Log.d("diksha", diksha);
                        diksha_date.setText(diksha);
                        String gr_name = jsonObject.getString("group_name");
                        Log.d("group_name", gr_name);
                        if (!VUStringUtility.stringNotEmpty(gr_name)) {
                            g_name_lay.setVisibility(View.GONE);
                        } else {
                            g_name_lay.setVisibility(View.VISIBLE);
                            group_name.setText(gr_name);
                            gr_view.setVisibility(View.VISIBLE);
                        }
                               /* if (!(gr_name =="")){
                                    g_name.setText(gr_name);

                                }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(DadaWadiDetailActivity.this, getResources().getString(R.string.try_Again), Toast.LENGTH_SHORT).show();
                }
            }
        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Log.d("geo", "geocoder");
        Log.d("myaddr", strAddress);

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            Log.d("getAddrlat", "" + address);


            if (address == null) {
                Log.d("geo", "geocodernull");
                return null;
            }
            Address location = address.get(0);
            lat = location.getLatitude();
            lon = location.getLongitude();
            Log.d("geo", "geocoderhello");

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d("p1", "" + p1);
            Log.d("lat", "" + location.getLatitude());
            Log.d("long", "" + location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    /* TimerTask scanTask;
     final Handler handler = new Handler();
     Timer mTimer = new Timer();

   public void sendSMS() {

       scanTask = new TimerTask() {
           public void run() {
               handler.post(new Runnable() {
                   public void run() {
                       //your method here which you want to call every 30 sec
                   }
               });
           }
       };

       mTimer.schedule(scanTask, 30000, 30000);
   }*/
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                }
                strAdd = strReturnedAddress.toString();
                d_address.setText(strAdd);
                //Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                // Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onClick(View v) {

        startActivity(VUSystemUtility.getInstance(DadaWadiDetailActivity.this).generateNavigationIntent("0", "0", latin, longin, dest_name));
    }
}
