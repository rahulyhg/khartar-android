package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.util.VUStringUtility;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.Activity.DetailScrollingActivity;
import com.vardhamaninfo.khartargaccha.Activity.FullDetailActivity;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class SadhviDetailFragment extends Fragment {

    private TextView tvAddress, tvGroupName, tvDikshaDate, tvMobileNumber, tvEmailAddress, tvLastSeen, tvCurrentLocation;
    private Button btShowDetails;
    String user_id = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sadhvi_detail, container, false);

        tvLastSeen = (TextView) view.findViewById(R.id.sadhu_sadhvi_tv_last_seen);
        tvCurrentLocation = (TextView) view.findViewById(R.id.sadhu_sadhvi_tv_current_location);
        tvAddress = (TextView) view.findViewById(R.id.sadhu_sadhvi_tv_address);
        tvGroupName = (TextView) view.findViewById(R.id.sadhu_sadhvi_tv_group_name);
        tvDikshaDate = (TextView) view.findViewById(R.id.sadhu_sadhvi_tv_diksha_date);
        tvMobileNumber = (TextView) view.findViewById(R.id.sadhu_sadhvi_tv_mobile_number);
        tvEmailAddress = (TextView) view.findViewById(R.id.sadhu_sadhvi_tv_email_address);

        btShowDetails = (Button) view.findViewById(R.id.fragment_sadhvi_detail_bt_show_details);

        btShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityFullDetailActivity = new Intent(getActivity(), FullDetailActivity.class);
                activityFullDetailActivity.putExtra("user_id", user_id);
                startActivity(activityFullDetailActivity);
            }
        });

        Log.d("view", "refer");

        getSadhviRequest(((DetailScrollingActivity) getActivity()).userID);

        Log.d("latitude", ((DetailScrollingActivity) getActivity()).latitude + "");
        Log.d("longitude", ((DetailScrollingActivity) getActivity()).longitude + "");

        getCompleteAddressString(((DetailScrollingActivity) getActivity()).latitude, ((DetailScrollingActivity) getActivity()).longitude);

        return view;
    }

    public void getSadhviRequest(String userid) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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


//        new VUBaseAsyncTask(getActivity(), null, true, Constant.HOST + Constant.Single_uid + userid, VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//
//                            Log.d("Sadhvi JSON", result + "");
//
////                            JSONArray jsonArray = null;
//
//                            try {

                                JSONObject jsonObject = result.getJSONObject("user_array");

                                String diksha = jsonObject.getString("diksha_date");

                                String s_id = jsonObject.getString("user_id");
                                String g_id = jsonObject.getString("group_id");

                                user_id = s_id;

                                String s_name = jsonObject.getString("username_hindi");
                                String dest_name = jsonObject.getString("username_hindi");

                                String gr_name = "---";
                                gr_name = jsonObject.getString("group_name_hindi");

                                String lasts = "---";
                                lasts = jsonObject.getString("modified_date");

                                String addr = "---";
                                addr = jsonObject.getString("address");

                                String u_mob = jsonObject.getString("mobile_number");
                                String email = jsonObject.getString("email_address");

                                if (!jsonObject.getString("image_url").equals(null)) {

                                    String image = jsonObject.getString("image_url");
                                    image = image.trim();
                                    image = image.replaceAll(" ", "%20");
                                    Log.e(" image url ",image);;
//                                    String image = "http://mahasammelan.in/assets/globals/plugins/jquery-file-upload/server/php/files/thumbnail/1457088037-WP1000646.jpg";

                                    Glide.with(getActivity()).load(image)
                                            //.placeholder(R.mipmap.jain_sadhvi)
                                            .thumbnail(0.5f)
                                            .crossFade()
                                            //.error(R.mipmap.jain_sadhvi)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(((DetailScrollingActivity) getActivity()).ivPhoto);
                                }

                                Log.d("uid", s_id);
                                Log.d("ugid", g_id);
                                Log.d("sadhuaddr", addr);
                                Log.d("uname", s_name);
                                Log.d("mob", u_mob);
                                Log.d("diksha", diksha);
                                Log.d("group_name", gr_name);

                                ((DetailScrollingActivity) getActivity()).getSupportActionBar().setTitle(s_name);

//                                tvName.setText(s_name);

                                tvLastSeen.setText(lasts);

                                if (!VUStringUtility.stringNotEmpty(email)) {
                                  //  tvEmailAddress.setText("---");
                                } else {
                                    tvEmailAddress.setText(email);
                                }

                                if (!VUStringUtility.stringNotEmpty(u_mob)) {
                                  //  tvMobileNumber.setText("---");
                                } else {
                                    tvMobileNumber.setText(u_mob);
                                }

                                if (!VUStringUtility.stringNotEmpty(gr_name)) {
                                //    tvGroupName.setText("---");
                                } else {
                                    tvGroupName.setText(gr_name);
                                }

                                if (!VUStringUtility.stringNotEmpty(addr)) {
                                 //   tvAddress.setText("---");
                                } else {
                                    tvAddress.setText(addr);
                                }

                                tvDikshaDate.setText(diksha);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        }
                    }
                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
//        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//
//                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
//                }
//                strAdd = strReturnedAddress.toString();
//
//                tvCurrentLocation.setText(strAdd);
//
//                Log.d("LATITUDE", LATITUDE + "");
//                Log.d("LONGITUDE", LONGITUDE + "");
//
//                Log.d("My Current", "" + strReturnedAddress.toString());
//            } else {
//                Log.d("My Current", "No Address returned!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            //Log.w("My Current loction address", "Canont get Address!");
//        }


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            tvCurrentLocation.setText("---");

            if (addresses.size() != 0) {

                Address address = addresses.get(0);

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    strAdd += address.getAddressLine(i) + ", ";
                }

//                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                Log.d("address", strAdd + "-");
                Log.d("city", city + "-");
                Log.d("state", state + "-");
                Log.d("country", country + "-");
                Log.d("postalCode", postalCode + "-");
                Log.d("knownName", knownName + "-");

                ((DetailScrollingActivity) getActivity()).dest_name = strAdd;
                tvCurrentLocation.setText(strAdd);
            }

            Log.d("address", "run");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return strAdd;
    }
}
