package com.vardhamaninfo.khartargaccha.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.adapters.JSONArrayAdapter;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchingActivity extends AppCompatActivity implements View.OnClickListener {
    EditText search_edit;
    ImageView close_search;
    ListView lv;
    LinearLayout lay;
    SharedPreferences sharedPreferences;
    int type;
    Double lati, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        type = getIntent().getIntExtra("Type", 0);

        search_edit = (EditText) findViewById(R.id.search_text);
        close_search = (ImageView) findViewById(R.id.close_search);
        close_search.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.search_list);
        lay = (LinearLayout) findViewById(R.id.ll_searching);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SearchingActivity.this);

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //close_search.setVisibility(View.VISIBLE);

                if (type == 1) {
                    getSearchSadhu(s.toString());
                } else if (type == 2) {
                    getSearchSadhvi(s.toString());
                } else if (type == 3) {
                    getSearchDadawadi(s.toString());
                } else if (type == 4) {
                    getSearchTirth(s.toString());
                }

                if (search_edit.getText().toString().length() > 0) {
                    close_search.setVisibility(View.VISIBLE);
                } else {
                    close_search.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_search:
                if (search_edit.getText().toString().length() > 0) {
                    search_edit.getText().clear();
                    close_search.setVisibility(View.GONE);
                }
                //se_list.setVisibility(View.GONE);
                break;
        }
    }

    void getSearchSadhu(final String name) {

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSearchSadhuDetail(name);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");

                    lv.setVisibility(View.VISIBLE);
                    try {

                       final JSONObject result = new JSONObject(data.body().toString());



//        new VUBaseAsyncTask(SearchingActivity.this, null, false, Constant.HOST + Constant.UserSearch + name + "/" + "Sadhu",
//                VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//
//
//                            lv.setVisibility(View.VISIBLE);
//
//                            try {
                                lv.setAdapter(new JSONArrayAdapter(SearchingActivity.this, result.getJSONArray("user_array"), R.layout.custom_list,
                                        new String[]{"username_hindi"}, new int[]{R.id.name}) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);

                                        TextView serchtxt = (TextView) view.findViewById(R.id.name);
                                        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
                                        LinearLayout list_click = (LinearLayout) view.findViewById(R.id.list_click);

                                        final JSONObject item = (JSONObject) getItem(position);

                                        try {
                                            JSONArray jsonArray = result.getJSONArray("user_array");
                                            JSONObject jsonObject = jsonArray.getJSONObject(position);
                                            serchtxt.setText(jsonObject.getString("username_hindi"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        list_click.setTag(item);
                                        list_click.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (v.getTag() != null) {

                                                    try {
                                                        String d_id = item.getString("user_id");
                                                        Double latitude = item.getDouble("latitude");
                                                        Double longitude = item.getDouble("longitude");
                                                        String address = item.getString("address");

                                                        Intent intent = new Intent(SearchingActivity.this, DetailScrollingActivity.class);
                                                        intent.putExtra("id", d_id);
                                                        intent.putExtra("longi", longitude);
                                                        intent.putExtra("lati", latitude);
                                                        intent.putExtra("address", address);
                                                        intent.putExtra("Page_type", "sadhu");
                                                        startActivity(intent);

                                                        sharedPreferences.edit().putString(Constant.sadhu_user_id, d_id).apply();
                                                        Log.d("id", d_id);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });

                                        return view;
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            lv.setVisibility(View.GONE);
                        }
                    }
                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    void getSearchSadhvi(final String name) {

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSearchSadhviDetail(name);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");

                    lv.setVisibility(View.VISIBLE);
                    try {

                        final JSONObject result = new JSONObject(data.body().toString());


//
//        new VUBaseAsyncTask(SearchingActivity.this, null, false, Constant.HOST + Constant.UserSearch + name + "/" + "Sadhvi", VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//                            lv.setVisibility(View.VISIBLE);
//
//                            try {
                                lv.setAdapter(new JSONArrayAdapter(SearchingActivity.this, result.getJSONArray("user_array"), R.layout.custom_list,
                                                      new String[]{"username_hindi"}, new int[]{R.id.name}) {
                                                  @Override
                                                  public View getView(int position, View convertView, ViewGroup parent) {
                                                      View view = super.getView(position, convertView, parent);

                                                      TextView serchtxt = (TextView) view.findViewById(R.id.name);
                                                      final ImageView imageView = (ImageView) view.findViewById(R.id.image);
                                                      LinearLayout list_click = (LinearLayout) view.findViewById(R.id.list_click);

                                                      final JSONObject item = (JSONObject) getItem(position);

                                                      try {
                                                          JSONArray jsonArray = result.getJSONArray("user_array");
                                                          JSONObject jsonObject = jsonArray.getJSONObject(position);

                                                          serchtxt.setText(jsonObject.getString("username_hindi"));
                                                      } catch (Exception e) {
                                                          e.printStackTrace();
                                                      }

                                                      list_click.setTag(item);
                                                      list_click.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              if (v.getTag() != null) {

                                                                  try {
                                                                      String d_id = item.getString("user_id");
                                                                      Double latitude = item.getDouble("latitude");
                                                                      Double longitude = item.getDouble("longitude");
                                                                      String address = item.getString("address");

                                                                      Intent intent = new Intent(SearchingActivity.this, DetailScrollingActivity.class);
                                                                      intent.putExtra("id", d_id);
                                                                      intent.putExtra("longi", longitude);
                                                                      intent.putExtra("lati", latitude);
                                                                      intent.putExtra("address", address);
                                                                      intent.putExtra("Page_type", "sadhvi");
                                                                      startActivity(intent);

                                                                      sharedPreferences.edit().putString(Constant.sadhu_user_id, d_id).apply();
                                                                      Log.d("id", d_id);

                                                                  } catch (Exception e) {
                                                                      e.printStackTrace();
                                                                  }
                                                              }
                                                          }
                                                      });

                                                      return view;
                                                  }
                                              }
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            lv.setVisibility(View.GONE);
                        }
                    }
                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    void getSearchDadawadi(final String name) {

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSearchDadawadiDetail(name);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");

                    lv.setVisibility(View.VISIBLE);
                    try {

                        final JSONObject result = new JSONObject(data.body().toString());

//        new VUBaseAsyncTask(SearchingActivity.this, null, false, Constant.HOST + Constant.DadawadiSearch + name + "/" + "dadawadi", VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//                            lv.setVisibility(View.VISIBLE);
//
//                            try {
                                lv.setAdapter(new JSONArrayAdapter(SearchingActivity.this, result.getJSONArray("user_array"), R.layout.custom_list,
                                                      new String[]{"username"}, new int[]{R.id.name}) {
                                                  @Override
                                                  public View getView(int position, View convertView, ViewGroup parent) {
                                                      View view = super.getView(position, convertView, parent);

                                                      TextView serchtxt = (TextView) view.findViewById(R.id.name);
                                                      final ImageView imageView = (ImageView) view.findViewById(R.id.image);
                                                      LinearLayout list_click = (LinearLayout) view.findViewById(R.id.list_click);

                                                      final JSONObject item = (JSONObject) getItem(position);

                                                      try {
                                                          JSONArray jsonArray = result.getJSONArray("user_array");
                                                          JSONObject jsonObject = jsonArray.getJSONObject(position);

                                                          serchtxt.setText(jsonObject.getString("name_hindi"));
                                                      } catch (Exception e) {
                                                          e.printStackTrace();
                                                      }

                                                      list_click.setTag(item);
                                                      list_click.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              if (v.getTag() != null) {

                                                                  try {
                                                                      String d_id = item.getString("id");
//                                                                      Double latitude = item.getDouble("latitude");
//                                                                      Double longitude = item.getDouble("longitude");
                                                                      String address = item.getString("address");
                                                                      getLocationFromAddress(SearchingActivity.this, address);

                                                                      Intent intent = new Intent(SearchingActivity.this, DetailScrollingActivity.class);
                                                                      intent.putExtra("id", d_id);
                                                                      intent.putExtra("longi", longi);
                                                                      intent.putExtra("lati", lati);
                                                                      intent.putExtra("address", address);
                                                                      intent.putExtra("Page_type", "Dadawadi");
                                                                      startActivity(intent);

                                                                      sharedPreferences.edit().putString(Constant.sadhu_user_id, d_id).apply();

                                                                      Log.d("id", d_id);

                                                                  } catch (Exception e) {
                                                                      e.printStackTrace();
                                                                  }
                                                              }
                                                          }
                                                      });

                                                      return view;
                                                  }
                                              }
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            lv.setVisibility(View.GONE);
                        }
                    }
                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    void getSearchTirth(final String name) {
        //getSearchTirthDetail


        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSearchTirthDetail(name);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");

                    lv.setVisibility(View.VISIBLE);
                    try {

                        final JSONObject result = new JSONObject(data.body().toString());



//        new VUBaseAsyncTask(SearchingActivity.this, null, false, Constant.HOST + Constant.DadawadiSearch + name + "/" + "tirth", VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//                            lv.setVisibility(View.VISIBLE);
//
//                            try {
                                lv.setAdapter(new JSONArrayAdapter(SearchingActivity.this, result.getJSONArray("user_array"), R.layout.custom_list,
                                                      new String[]{"username"}, new int[]{R.id.name}) {
                                                  @Override
                                                  public View getView(int position, View convertView, ViewGroup parent) {
                                                      View view = super.getView(position, convertView, parent);

                                                      TextView serchtxt = (TextView) view.findViewById(R.id.name);
                                                      final ImageView imageView = (ImageView) view.findViewById(R.id.image);
                                                      LinearLayout list_click = (LinearLayout) view.findViewById(R.id.list_click);

                                                      final JSONObject item = (JSONObject) getItem(position);

                                                      try {
                                                          JSONArray jsonArray = result.getJSONArray("user_array");
                                                          JSONObject jsonObject = jsonArray.getJSONObject(position);

                                                          serchtxt.setText(jsonObject.getString("name_hindi"));
                                                      } catch (Exception e) {
                                                          e.printStackTrace();
                                                      }

                                                      list_click.setTag(item);
                                                      list_click.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              if (v.getTag() != null) {

                                                                  try {
                                                                      String d_id = item.getString("id");
//                                                                      Double latitude = item.getDouble("latitude");
//                                                                      Double longitude = item.getDouble("longitude");
                                                                      String address = item.getString("address");
                                                                      getLocationFromAddress(SearchingActivity.this, address);

                                                                      Intent intent = new Intent(SearchingActivity.this, DetailScrollingActivity.class);
                                                                      intent.putExtra("id", d_id);
                                                                      intent.putExtra("longi", longi);
                                                                      intent.putExtra("lati", lati);
                                                                      intent.putExtra("address", address);
                                                                      intent.putExtra("Page_type", "Tirth");
                                                                      startActivity(intent);

                                                                      sharedPreferences.edit().putString(Constant.sadhu_user_id, d_id).apply();
                                                                      Log.d("id", d_id);

                                                                  } catch (Exception e) {
                                                                      e.printStackTrace();
                                                                  }
                                                              }
                                                          }
                                                      });

                                                      return view;
                                                  }
                                              }
                                );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            lv.setVisibility(View.GONE);
                        }
                    }
                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
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

    public void makeSnake(String msg) {
        Snackbar snackbar1 = Snackbar.make(lay, msg, Snackbar.LENGTH_LONG);
        snackbar1.getView().setBackgroundColor(Color.parseColor("#0f7560"));
        snackbar1.show();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Log.d("geo", "geocoder");
        Log.d("myaddr", strAddress);

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            Log.d("getAddrlat", "" + address);


            if (address == null) {
                Log.d("geo", "geocodernull");
                return null;
            }
            Address location = address.get(0);
            lati = location.getLatitude();
            longi = location.getLongitude();
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
}