package com.vardhamaninfo.khartargaccha.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.util.VUStringUtility;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class SadhuSadhviDetailActivity extends AppCompatActivity {

    TextView p_name, g_name, j_date, mob;
    SharedPreferences sharedPreferences;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sadhu_sadhvi_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mob = (TextView) findViewById(R.id.no);
        p_name = (TextView) findViewById(R.id.name);
        g_name = (TextView) findViewById(R.id.gname);
        j_date = (TextView) findViewById(R.id.diksha);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = sharedPreferences.getString(Constant.sadhu_user_id, "");
        getsinglesadhu();
    }

    void getsinglesadhu() {

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


//        new VUBaseAsyncTask(this, null, false,
//                Constant.HOST + Constant.Single_uid + userid, VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//                            JSONArray jsonArray = null;
//                            try {
//

                        JSONObject jsonObject = new JSONObject();
                        jsonObject = result.getJSONObject("user_array");
                        String s_id = jsonObject.getString("user_id");
                        Log.d("uid", s_id);
                        String g_id = jsonObject.getString("group_id");
                        Log.d("ugid", g_id);
                        String s_name = jsonObject.getString("username");
                        Log.d("uname", s_name);
                        p_name.setText(s_name);
                        String u_mob = jsonObject.getString("mobile_number");
                        Log.d("mob", u_mob);
                        if (!VUStringUtility.stringNotEmpty(u_mob)) {
                            mob.setVisibility(View.GONE);
                        } else {
                            mob.setVisibility(View.VISIBLE);
                            mob.setText(u_mob);
                        }

                        String diksha = jsonObject.getString("diksha_date");
                        Log.d("diksha", diksha);
                        j_date.setText(diksha);
                        String gr_name = jsonObject.getString("group_name");
                        Log.d("group_name", gr_name);
                        if (!VUStringUtility.stringNotEmpty(gr_name)) {
                            g_name.setVisibility(View.GONE);
                        } else {
                            g_name.setVisibility(View.VISIBLE);
                            g_name.setText(gr_name);
                        }
                               /* if (!(gr_name =="")){
                                    g_name.setText(gr_name);

                                }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SadhuSadhviDetailActivity.this, getResources().getString(R.string.try_Again), Toast.LENGTH_SHORT).show();
                }
            }
        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
}