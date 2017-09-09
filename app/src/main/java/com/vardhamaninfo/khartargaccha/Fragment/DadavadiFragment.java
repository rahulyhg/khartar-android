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
import com.vardhamaninfo.khartargaccha.Application.Application;
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

public class DadavadiFragment extends Fragment {
    ListView selectedList;
    SharedPreferences sharedPreferences;
    LinearLayout dada_lay;
    Double lati = 0.0, loni = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((Navigation_Drawer_Activity) getActivity()).type = 3;

        View view = inflater.inflate(R.layout.dadawadi_listview, container, false);

        selectedList = (ListView) view.findViewById(R.id.selectedList);
        dada_lay = (LinearLayout) view.findViewById(R.id.dada_lay);

        Constant.Dadawadi_id = "";

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_menu_dadawadi);

        getdadawadi();
        return view;
    }

    void getdadawadi() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getDadawadiDetails();

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                       final JSONObject result = new JSONObject(data.body().toString());

//        new VUBaseAsyncTask(getActivity(), null, true, Constant.HOST + Constant.Get_Dadawadi,
//                VUBaseAsyncTask.METHOD_GET,
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//
//                            try {
//

                                selectedList.setAdapter(new JSONArrayAdapter(getActivity(), result.getJSONArray("place_array"),
                                        R.layout.custom_list, new String[]{"name_hindi"}, new int[]{R.id.name}) {

                                    @Override
                                    public View getView(final int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);

                                        final ImageView dadaimage = (ImageView) view.findViewById(R.id.dadaimage);

                                         JSONObject item = (JSONObject) getItem(position);
                                        try {
                                            JSONObject item1  = (JSONObject) getItem(position);
                                            String image = item1.getString("image");
                                            if(!image.isEmpty()) {

                                                image = "http://mahasammelan.in/uploads/"+image;


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

                                        LinearLayout list_click = (LinearLayout) view.findViewById(R.id.list_click);


                                        list_click.setTag(item);
                                        list_click.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (v.getTag() != null) {

                                                    try {
                                                        JSONObject item = (JSONObject) getItem(position);
                                                        String d_id = item.getString("id");
                                                        String address = item.getString("address");
                                                        String name_hindi = item.getString("name_hindi");

                                                        // http://mahasammelan.in/uploads/
                                                        LatLng latLng = null;

                                                        if (!address.equals("null")) {

                                                            if (getLocationFromAddress(getActivity(), address)) {

                                                                latLng = new LatLng(lati, loni);
                                                                setIntent(d_id, address, name_hindi, latLng);

                                                            } else {
                                                                latLng = new LatLng(0.0, 0.0);
                                                                setIntent(d_id, address, name_hindi, latLng);
                                                            }

                                                        } else {

                                                            latLng = new LatLng(0.0, 0.0);
                                                            setIntent(d_id, address, name_hindi, latLng);

                                                        }


                                                       /* getLocationFromAddress(getActivity(), address);

                                                        Log.e("lati", "" + lati);
                                                        Log.e("longi", "" + loni);

                                                        Intent intent = new Intent(getActivity(), DetailScrollingActivity.class);
                                                        intent.putExtra("id", d_id);
                                                        intent.putExtra("address", address);
                                                        intent.putExtra("username_hindi", name_hindi);
                                                        intent.putExtra("lati", lati);
                                                        intent.putExtra("longi", loni);
                                                        intent.putExtra("Page_type", "Dadawadi");
                                                        startActivity(intent);//name_hindi*/

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
//
//                                        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
//
//                                        imageView.setTag(item);
//                                        imageView.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                if (v.getTag() != null) {
//
//                                                    try {
//                                                        String d_id = item.getString("id");
//                                                        String address = item.getString("address");
//                                                        getLocationFromAddress(getActivity(), address);
//                                                        Log.d("lati", "" + lati);
//                                                        Log.d("longi", "" + loni);
//                                                        Intent intent = new Intent(getActivity(), DadaWadiDetailActivity.class);
//                                                        intent.putExtra("id", d_id);
//                                                        intent.putExtra("address", address);
//                                                        intent.putExtra("lati", lati);
//                                                        intent.putExtra("longi", loni);
//                                                        intent.putExtra("Page_type", "Dadawadi");
//                                                        startActivity(intent);
//
//                                                        sharedPreferences.edit().putString(Constant.Dadawadi_id, d_id).apply();
//                                                        Log.d("id", d_id);
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            }
//                                        });
                                        return view;
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JSONArray jsonArray = null;
                            try {
                                final JSONObject result = new JSONObject(data.body().toString());
                                jsonArray = result.getJSONArray("place_array");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject = jsonArray.getJSONObject(i);

                                    String d_name = jsonObject.getString("name_hindi");
                                    Log.d("name", d_name);
                                    String d_address = jsonObject.getString("address");
                                    Log.d("address", d_address);
                                    String d_type = jsonObject.getString("type");
                                    Log.d("type", d_type);
                                    String state_name = jsonObject.getString("state_name");
                                    Log.d("state", state_name);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            makeSnake("No Data Available");

                        /*lv.setVisibility(root.GONE);
                            Toast.makeText(getActivity(), "No group available", Toast.LENGTH_SHORT).show();*/
                        }
                    }


                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void setIntent(String uid, String address, String name_hindi, LatLng latLng){

        Intent intent = new Intent(getActivity(), DetailScrollingActivity.class);
        intent.putExtra("id", uid);
        intent.putExtra("address", address);
        intent.putExtra("Page_type", "Dadawadi");
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

            isLatLng =  true;

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return isLatLng;
    }
}
