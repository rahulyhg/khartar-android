package com.vardhamaninfo.khartargaccha.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Adapter.PanchangAdapter;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.Model.Panchang;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class PanchangDetailActivity extends AppCompatActivity {

    String date;
    // TextView dating, weekday, lunar_month, lunar_year, lunar_tithi, shubh_din, lunar_cycle, desc;
    //LinearLayout lay, pan_lay;

    private RecyclerView recyclerView;
    private PanchangAdapter panchangAdapter;
    private ArrayList<Panchang> panchangs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panchang_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.content_panchang_detail_recyclerview);

        panchangs = new ArrayList<Panchang>();

        panchangAdapter = new PanchangAdapter(this, panchangs);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(panchangAdapter);

        date = getIntent().getStringExtra("date");

        Log.d("date", date);

        // setData();

        panchangAdapter.notifyDataSetChanged();
//        dating = (TextView) findViewById(R.id.dating);
//        weekday = (TextView) findViewById(R.id.weekday);
//        lunar_month = (TextView) findViewById(R.id.lunar_month);
//        lunar_year = (TextView) findViewById(R.id.lunar_year);
//        lunar_tithi = (TextView) findViewById(R.id.lunar_tithi);
//        shubh_din = (TextView) findViewById(R.id.shubh_din);
//        lunar_cycle = (TextView) findViewById(R.id.lunar_cycle);
//        lay = (LinearLayout) findViewById(R.id.panchang_lay);
//        desc = (TextView) findViewById(R.id.desc);
//        pan_lay = (LinearLayout) findViewById(R.id.panclayout);

          panchang(date);
//        panchang("2016-09-25");
    }

    public void setData() {

        //  panchangs.add(new Panchang("No", "24-September-2017", "Sunday", "2073", "आश्विन", "कार्तिक", "चैत्र", ""));

        for (int i = 0; i < 10; i++) {

            panchangs.add(new Panchang("Yes", "24-September-2017", "Sunday", "2073", "आश्विन", "कार्तिक", "चैत्र", "This is Description"));
        }
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


    void panchang(String date) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getCalenderEvents(date);
        //.getPanchangDateDetail(date);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());

                        JSONArray jsonArray = result.getJSONArray("user_array");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String id = jsonObject.optString("id");
                            String date = jsonObject.optString("date");
                            String weekday = jsonObject.optString("weekday");
                            String lunar_year = jsonObject.optString("lunar_year");
                            String lunar_month = jsonObject.optString("lunar_month");
                            String lunar_tithi = jsonObject.optString("lunar_tithi");
                            String shubh_din = jsonObject.optString("shubh_din");
                            String lunar_cycle = jsonObject.optString("lunar_cycle");
                            String description = jsonObject.optString("description");


                                  /*  "id": "3",
                                    "date": "2016-09-23",
                                    "weekday": "Friday",
                                    "lunar_year": "2073",
                                    "lunar_month": "अश्विन",
                                    "lunar_tithi": "अस्टमी",
                                    "shubh_din": "No",
                                    "lunar_cycle": "Krushna",
                                    "description": ""*/


                            panchangs.add(new Panchang(shubh_din, date, weekday, lunar_year, lunar_tithi,
                                    lunar_cycle, lunar_month, description));


                        }

                        panchangAdapter.notifyDataSetChanged();

                        //panchangs.add(new Panchang("No", "24-September-2017", "Sunday", "2073", "आश्विन", "कार्तिक", "चैत्र", ""));

//
//    new VUBaseAsyncTask(this, null, false, Constant.HOST + Constant.Panchang+date , VUBaseAsyncTask.METHOD_GET,
//
//            new VUIBaseAsyncTask() {
//                @Override
//                public void onTaskCompleted(int success, final JSONObject result) {
//                    if (success == 1) {
//                        try {
//

                        //pan_lay.setVisibility(View.VISIBLE);
                       /* String wday, lumon, lunyear, lucycle, luntit, shudin, descr;
                        JSONObject job = result.getJSONObject("panchang_array");
                        String date = job.getString("date");
                        dating.setText(date);
                        wday = job.getString("weekday");
                        weekday.setText(wday);
                        lumon = job.getString("lunar_month");
                        lunar_month.setText(lumon);
                        lunyear = job.getString("lunar_year");
                        lunar_year.setText(lunyear);
                        lucycle = job.getString("lunar_cycle");
                        lunar_cycle.setText(lucycle);
                        luntit = job.getString("lunar_tithi");
                        lunar_tithi.setText(luntit);
                        shudin = job.getString("shubh_din");
                        shubh_din.setText(shudin);
                        descr = job.getString("description");
                        desc.setText(descr);*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    //makeSnake("Panchang Details not Available");

                    if (data != null) {

                        //   makeSnake("Panchang Details not Available");
                        Toast.makeText(PanchangDetailActivity.this, "Panchang Details not Available", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(PanchangDetailActivity.this, getResources().getString(R.string.try_Again), Toast.LENGTH_SHORT).show();
                    }

                }


            }


        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

    }


   /* public void makeSnake(String msg) {

        Snackbar snackbar1 = Snackbar.make(lay, msg, Snackbar.LENGTH_LONG);
        snackbar1.getView().setBackgroundColor(Color.parseColor("#774A2B"));
        snackbar1.show();

    }*/
}
