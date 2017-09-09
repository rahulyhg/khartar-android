package com.vardhamaninfo.khartargaccha.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class EventDetailsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView ename, edatetime, edescription;
    String event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ename = (TextView) findViewById(R.id.event_name);
        edatetime = (TextView) findViewById(R.id.event_date_time);
        edescription = (TextView) findViewById(R.id.event_desc);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EventDetailsActivity.this);

        event_id = getIntent().getStringExtra("event_id");

        Getsingevent(event_id);
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

    public void Getsingevent(String evuid) {//getSingleEvent


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSingleEvent(evuid);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());


//        new VUBaseAsyncTask(EventDetailsActivity.this, null, true, Constant.HOST + Constant.GETSINGLEEVENT + evuid,
//                VUBaseAsyncTask.METHOD_GET, new VUIBaseAsyncTask() {
//            @Override
//            public void onTaskCompleted(int success, JSONObject result) {
//
//                if (success == 1) {
//                    JSONObject jsonObject = null;
//                    try {
//

                        JSONObject jsonObject1 = result.getJSONObject("event_array");
                        String sid = jsonObject1.getString("id");
                        String eventname = jsonObject1.getString("event_name");
                        Log.v("ppp", eventname);
                        ename.setText(eventname);

                        String eventdescription = jsonObject1.getString("event_description");
                        edescription.setText(eventdescription);
                        Log.v("ppp", eventdescription);
                        String eventtimedate = jsonObject1.getString("event_date_time");
                        edatetime.setText(eventtimedate);
                        Log.v("ppp", eventtimedate);
                        // sharedPreferences.edit().putString(Costant.EVENTNAME,eventname);
                        //sharedPreferences.edit().putString(Costant.EVENTDESCRIPTION,eventdescription);
                        // sharedPreferences.edit().putString(Costant.EVENTDATETIME,eventtimedate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(EventDetailsActivity.this, getResources().getString(R.string.try_Again), Toast.LENGTH_SHORT).show();
                }
            }
        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
}
