package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.Model.TodayTithi;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Util.Constant;
import com.vardhamaninfo.khartargaccha.Util.StringUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

public class TodayTithiFragment extends Fragment implements View.OnClickListener {

    private TextView tvTodayDate, tvDay, tvLunar, tvShubhDin, tvDescription;
    private LinearLayout llEvent, llDadawadi, llTirth, llMain;

    private TextView tvEventName, tvEventDate, tvDadawadiName, tvTirthName;
    private ImageView ivEvent, ivDadawadi, ivTirth, ivManglik;

    ImageView search,ivAppIcon;

    private String currentDate;

    private ArrayList<TodayTithi> events;
    private ArrayList<TodayTithi> tirths;
    private ArrayList<TodayTithi> dadawadi;

    private String IMAGE_HOST = "http://mahasammelan.in/uploads/";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tithi_description, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_menu_home);
       // ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.mipmap.logo);


        search = (ImageView) getActivity().findViewById(R.id.searching);
        ivAppIcon = (ImageView) getActivity().findViewById(R.id.ivAppIcon);

        events = new ArrayList<>();
        tirths = new ArrayList<>();
        dadawadi = new ArrayList<>();

        tvTodayDate = (TextView) view.findViewById(R.id.today);
        tvDay = (TextView) view.findViewById(R.id.day);
        tvLunar = (TextView) view.findViewById(R.id.lunar);
        tvShubhDin = (TextView) view.findViewById(R.id.shubh_din);
        tvDescription = (TextView) view.findViewById(R.id.description);

        tvEventName = (TextView) view.findViewById(R.id.event_name);
        tvTirthName = (TextView) view.findViewById(R.id.tirth_name);
        tvDadawadiName = (TextView) view.findViewById(R.id.dadawadi_name);
        tvEventDate = (TextView) view.findViewById(R.id.event_date);

        llEvent = (LinearLayout) view.findViewById(R.id.event_layout);
        llTirth = (LinearLayout) view.findViewById(R.id.tirth_layout);
        llDadawadi = (LinearLayout) view.findViewById(R.id.dadawadi_layout);
        llMain = (LinearLayout) view.findViewById(R.id.main_ll);

        ivEvent = (ImageView) view.findViewById(R.id.event_image);
        ivTirth = (ImageView) view.findViewById(R.id.tirth_image);
        ivDadawadi = (ImageView) view.findViewById(R.id.dadawadi_image);
        ivManglik = (ImageView) view.findViewById(R.id.manglik_image);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = simpleDateFormat.format(c.getTime());

        tvTodayDate.setText(currentDate);

        Log.e("currentDate1", "Today: " + currentDate);

        Locale locale = new Locale("hi");

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        currentDate = simpleDateFormat.format(c.getTime());

        Log.e("currentDate2", "Today: " + currentDate);


        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        tvDay.setText(dayOfTheWeek);

        llEvent.setOnClickListener(this);
        llDadawadi.setOnClickListener(this);
        llTirth.setOnClickListener(this);


        panchang();
//        getevents();

        return view;
    }

    void panchang() {

        Log.e("url home", Constant.HOST + Constant.Panchang + "/" + currentDate);

       // currentDate = "2016-09-24";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getPanchangDateDetail(currentDate);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
                    @Override
                    public void onReturn(boolean success, Response<JsonObject> data) {

                        pDialog.dismiss();

//                        Log.e(" Response panchang ", data.body().toString() + " response");

                        if (success) {

                            Log.e("  Response ", data.body().toString() + " response");

                           // JSONObject json = null;

                            llMain.setVisibility(View.VISIBLE);

                            try {
                                JSONObject result = new JSONObject(data.body().toString());


                                if(result.has("panchang_array")) {
                                    JSONObject json = result.getJSONObject("panchang_array");
                                    Log.d("array", String.valueOf(result.getJSONObject("panchang_array")));

                                    tvDay.setText(json.getString("weekday"));

                                    if (json.getString("shubh_din").toLowerCase().equals("yes")) {
                                        tvShubhDin.setVisibility(View.VISIBLE);
                                    } else {
                                        tvShubhDin.setVisibility(View.GONE);
                                    }

                                    String hindiDate = json.getString("lunar_month") + " ";
                                    hindiDate += json.getString("lunar_cycle") + " ";
                                    hindiDate += json.getString("lunar_tithi");

                                    tvLunar.setText(hindiDate);
                                    tvDescription.setText(json.getString("description"));

                                }else {

                                    tvLunar.setVisibility(View.GONE);
                                    tvDescription.setVisibility(View.GONE);
                                    tvShubhDin.setVisibility(View.GONE);
                                }

                                    Random randomGenerator = new Random();
//                                int randomInt = randomGenerator.nextInt(100);
//
//                                Log.d("randomInt", "This " + randomInt);

                                if (result.has("0")) {

                                    Log.d("has", "Called");

                                    JSONArray jsonArray = result.getJSONArray("0");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        if (jsonObject.has("type") && jsonObject.getString("type").equals("tirth")) {

                                            tirths.add(new TodayTithi(jsonObject.getString("religious_name"), jsonObject.getString("religious_image"), ""));

                                        } else if (jsonObject.has("type") && jsonObject.getString("type").equals("dadawadi")) {

                                            dadawadi.add(new TodayTithi(jsonObject.getString("religious_name"), jsonObject.getString("religious_image"), ""));

                                        } else if (!jsonObject.has("type")) {

                                            events.add(new TodayTithi(jsonObject.getString("event_name"), jsonObject.getString("image"), jsonObject.getString("event_date")));
                                        }
                                    }
                                }

                                Log.d("events", "Called " + events.size());

                                if (events.size() > 0) {

                                    llEvent.setVisibility(View.VISIBLE);

                                    int position = randomGenerator.nextInt(events.size() - 1);

                                    tvEventName.setText(events.get(position).getName());
                                    tvEventDate.setText(events.get(position).getDate());

                                    Log.d("events", "position" + position);
                                    Log.d("tvEventName", "This " + events.get(position).getName());
                                    Log.d("tvEventDate", "This " + events.get(position).getDate());

                                    Glide.with(getActivity()).load(IMAGE_HOST + events.get(position).getImage().replace(" ", "%20"))
                                            .thumbnail(0.5f)
                                            .crossFade()
                                            .placeholder(R.mipmap.logo)
                                            .error(R.mipmap.logo)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(ivEvent);

                                    Log.d("events", "size");

                                } else {
                                    llEvent.setVisibility(View.GONE);

                                    Log.d("events", "size0");
                                }

                                Log.d("dadawadi", "Called " + dadawadi.size());

                                if (dadawadi.size() > 0) {

                                    llDadawadi.setVisibility(View.VISIBLE);

                                    int position = randomGenerator.nextInt(dadawadi.size() - 1);

                                    tvDadawadiName.setText(dadawadi.get(position).getName());

                                    Log.d("dadawadi", "position" + position);
                                    Log.d("tvDadawadiName", "This " + dadawadi.get(position).getName());

                                    Glide.with(getActivity()).load(IMAGE_HOST + dadawadi.get(position).getImage().replace(" ", "%20"))
                                            .thumbnail(0.5f)
                                            .crossFade()
                                            .placeholder(R.mipmap.logo)
                                            .error(R.mipmap.logo)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(ivDadawadi);

                                    Log.d("dadawadi", "size");
                                } else {
                                    llDadawadi.setVisibility(View.GONE);

                                    Log.d("dadawadi", "size0");
                                }

                                Log.d("tirths", "Called " + tirths.size());

                                if (tirths.size() > 0) {

                                    llTirth.setVisibility(View.VISIBLE);

                                    int position = randomGenerator.nextInt(tirths.size() - 1);

                                    tvTirthName.setText(tirths.get(position).getName());

                                    Log.d("tirths", "position" + position);
                                    Log.d("tvTirthName", "This " + tirths.get(position).getName());

                                    Glide.with(getActivity()).load(IMAGE_HOST + tirths.get(position).getImage().replace(" ", "%20"))
                                            .thumbnail(0.5f)
                                            .crossFade()
                                            .placeholder(R.mipmap.logo)
                                            .error(R.mipmap.logo)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(ivTirth);

                                    Log.d("tirths", "size");
                                } else {
                                    llTirth.setVisibility(View.GONE);

                                    Log.d("tirths", "size0");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                            llMain.setVisibility(View.GONE);
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {

        StringUtility stringUtility = new StringUtility();
        FragmentManager fragmentManager = getFragmentManager();

        if (view.getId() == R.id.event_layout) {

            search.setVisibility(View.GONE);
            stringUtility.replaceFragment(new EventsFragment(), fragmentManager);

        }

        if (view.getId() == R.id.dadawadi_layout) {

            search.setVisibility(View.VISIBLE);
            stringUtility.replaceFragment(new DadavadiFragment(), fragmentManager);

        }

        if (view.getId() == R.id.tirth_layout) {

            search.setVisibility(View.VISIBLE);
            stringUtility.replaceFragment(new TirthFragment(), fragmentManager);

        }


    }

    @Override
    public void onResume() {
        search.setVisibility(View.GONE);
        ivAppIcon.setVisibility(View.VISIBLE);

        super.onResume();
    }
}