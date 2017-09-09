package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.adapters.JSONArrayAdapter;
import com.vardhamaninfo.khartargaccha.Activity.DetailScrollingActivity;
import com.vardhamaninfo.khartargaccha.Activity.Navigation_Drawer_Activity;
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

import static com.vardhamaninfo.khartargaccha.Application.Application.getAppInstance;

public class SadhuFragment extends Fragment {

    ListView sadhulist;
    SharedPreferences sharedPreferences;
    LinearLayout dada_lay;
    Double lati = 0.0, loni = 0.0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((Navigation_Drawer_Activity) getActivity()).type = 1;

        View view = inflater.inflate(R.layout.fragment_sadhu_sadhvi, container, false);
        sadhulist = (ListView) view.findViewById(R.id.sadhulist);
        dada_lay = (LinearLayout) view.findViewById(R.id.sadhu_lay);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_menu_sadhu);

        getsadhusadhvi();

//      latestNewsDetails();

        return view;
    }


    private void latestNewsDetails() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getAllSadhuDetails();

        new HttpServerBackend(getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();

                Log.e("  Response latestNewsDetails ", data.body().toString() + " response");


            }
        });
    }

    void getsadhusadhvi() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getAllSadhuDetails();

        new HttpServerBackend(getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();

                if (success) {

                    try {
                        Log.e("  Response ", data.body().toString() + " response");
                        JSONObject result = new JSONObject(data.body().toString());

//        new VUBaseAsyncTask(getActivity(), null, true, Constant.HOST + Constant.Get_All_Users + "sadhu",
//                VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//
//                            try {
                        sadhulist.setAdapter(new JSONArrayAdapter(getActivity(), result.getJSONArray("user_array"),
                                R.layout.custom_list, new String[]{"username_hindi"}, new int[]{R.id.name}) {

                            @Override
                            public View getView(final int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);

                                final JSONObject item = (JSONObject) getItem(position);


                                final ImageView dadaimage = (ImageView) view.findViewById(R.id.dadaimage);

                                try {
                                    JSONObject item1  = (JSONObject) getItem(position);
                                    String image = item1.getString("image");
                                    if(!image.isEmpty()) {


                                        // image = "http://mahasammelan.in/uploads/"+image;


                                        Log.e(" image url ",image);
                                        try {
                                            Glide.with(getActivity()).load(image.replace(" ", "%20"))
                                                    //   .override(70,70)
                                                    // .thumbnail(0.5f)
                                                    // .crossFade()
                                                    .fitCenter()
                                                    .placeholder(R.mipmap.logo)
                                                    .error(R.mipmap.logo)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .into(dadaimage);

                                        } catch (Exception e) {
                                        }

                                    }
                                }catch (Exception r) {}

                                dadaimage.setVisibility(View.VISIBLE);

                                LinearLayout list_click = (LinearLayout) view.findViewById(R.id.list_click);
                                list_click.setTag(item);
                                list_click.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (v.getTag() != null) {

                                            try {
                                                JSONObject item = (JSONObject) getItem(position);
                                                String d_id = item.getString("user_id");
                                                //String latitude1=item.getString("latitude");
                                                //Double latitude= Double.valueOf(latitude1);
                                                //String longitu
                                                Double latitude = item.getDouble("latitude");
                                                Double longitude = item.getDouble("longitude");
                                                String address = item.getString("address");
                                                String username_hindi = item.getString("username_hindi");
//                                                        Intent intent = new Intent(getActivity(), DadaWadiDetailActivity.class);
                                                Log.e("  address/// ", address + " response //////");

                                                LatLng latLng = null;
                                                if (!address.equals("null")) {
                                                    if (getLocationFromAddress(getActivity(), address)) {
                                                        latLng = new LatLng(lati, loni);
                                                        setIntent(d_id, address, username_hindi, latLng);
                                                    } else {
                                                        latLng = new LatLng(0.0, 0.0);
                                                        setIntent(d_id, address, username_hindi, latLng);
                                                    }
                                                } else {
                                                    latLng = new LatLng(0.0, 0.0);
                                                    setIntent(d_id, address, username_hindi, latLng);
                                                }


                                               /* Intent intent = new Intent(getActivity(), DetailScrollingActivity.class);
                                                intent.putExtra("id", d_id);
                                                intent.putExtra("longi", longitude);
                                                intent.putExtra("lati", latitude);
                                                intent.putExtra("address", address);
                                                intent.putExtra("username_hindi", username_hindi);
                                                intent.putExtra("Page_type", "sadhu");
                                                intent.putExtra("lati", lati);
                                                intent.putExtra("longi", loni);
                                                startActivity(intent);*/

                                                sharedPreferences.edit().putString(Constant.sadhu_user_id, d_id).apply();
                                                Log.d("id", d_id);


                                               /* try {
                                                    Glide.with(getActivity()).load(IMAGE_HOST + events.get(position).getImage().replace(" ", "%20"))
                                                            .thumbnail(0.5f)
                                                            .crossFade()
                                                            .placeholder(R.mipmap.logo)
                                                            .error(R.mipmap.logo)
                                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                            .into(ivEvent);

                                                }catch (Exception e){}*/

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

                    JSONArray jsonArray = null;
                    try {
                        JSONObject result = new JSONObject(data.body().toString());
                        jsonArray = result.getJSONArray("user_array");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = new JSONObject();
                            jsonObject = jsonArray.getJSONObject(i);

                            String sg_name = jsonObject.getString("group_name");
                            Log.d("gname", sg_name);
                            String sg_jdate = jsonObject.getString("join_date");
                            Log.d("address", sg_jdate);
                            String sg_mob = jsonObject.getString("mobile_number");
                            Log.d("type", sg_mob);
                            String sg_gid = jsonObject.getString("group_id");
                            Log.d("state", sg_gid);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    makeSnake("No Data Available");
                }
            }


        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void setIntent(String uid, String address, String name_hindi, LatLng latLng) {

        Intent intent = new Intent(getActivity(), DetailScrollingActivity.class);
        intent.putExtra("id", uid);
        intent.putExtra("address", address);
        intent.putExtra("Page_type", "sadhu");
        intent.putExtra("username_hindi", name_hindi);
        intent.putExtra("lati", latLng.latitude);
        intent.putExtra("longi", latLng.longitude);
        startActivity(intent);

    }

    public void makeSnake(String msg) {
        Snackbar snackbar1 = Snackbar.make(dada_lay, msg, Snackbar.LENGTH_LONG);
        snackbar1.getView().setBackgroundColor(Color.parseColor("#774A2B"));
        snackbar1.show();
    }

    private boolean getLocationFromAddress(Context context, String strAddress) {
        Log.d("geo", "geocoder");
        Log.d("myaddr", strAddress);

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        boolean isLatLng = false;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            Log.d("getAddrlat", "" + address);

            if (address == null) {
                Log.d("geo", "geocodernull");
                //return null;
                isLatLng = false;
            }
            Address location = address.get(0);
            lati = location.getLatitude();
            loni = location.getLongitude();
            Log.d("geo", "geocoderhello");

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d("p1", "" + p1);
            Log.d("lat", "" + location.getLatitude());
            Log.d("long", "" + location.getLongitude());

            isLatLng = true;

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return isLatLng;
    }

}