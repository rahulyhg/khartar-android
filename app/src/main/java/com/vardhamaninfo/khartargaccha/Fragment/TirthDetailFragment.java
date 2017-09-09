package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.util.VUStringUtility;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.Activity.DetailScrollingActivity;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class TirthDetailFragment extends Fragment {

    private TextView tvAddress,tv_mobile,tv_description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tirth_detail, container, false);

        tvAddress = (TextView) view.findViewById(R.id.tirth_tv_address);
        tv_mobile = (TextView) view.findViewById(R.id.tirth_tv_mobile);
        tv_description = (TextView) view.findViewById(R.id.tirth_tv_description);
        Log.d("view", "refer");

        getTirthRequest(((DetailScrollingActivity) getActivity()).userID);

        return view;
    }

    public void getTirthRequest(String userid) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getSignalTirthDetails(userid);

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());

//        new VUBaseAsyncTask(getActivity(), null, true, Constant.HOST + Constant.SINGLETIRTH + userid,
//                VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//
////                            JSONArray jsonArray = null;
//
//                            try {

                                Log.d("Tirth JSON", result + "");

                                JSONObject jsonObject = result.getJSONObject("place_array");

                                String s_id = jsonObject.getString("id");

                                String dest_name = jsonObject.getString("name_hindi");
                                String s_name = jsonObject.getString("name_hindi");

//                                String m_no = jsonObject.getString("mobile_number");

                                String map_address = jsonObject.getString("address_hindi");
                                String description_hindi = jsonObject.getString("description_hindi");

                                if (!jsonObject.getString("images").equals(null)) {

                                    String image = jsonObject.getString("images");
                                  //  String image = jsonObject.getString("image");
                                    image = image.trim();
                                    image = image.replaceAll(" ", "%20");
                                    Log.e(" image url ",image);;
//                                    String image = "http://mahasammelan.in/assets/globals/plugins/jquery-file-upload/server/php/files/thumbnail/1457088037-WP1000646.jpg";

                                    Glide.with(getActivity()).load(image)
                                            .thumbnail(0.5f)
                                            .crossFade()
                                           // .placeholder(R.mipmap.jain_tirth)
                                           // .error(R.mipmap.jain_tirth)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(((DetailScrollingActivity) getActivity()).ivPhoto);
                                }

                                ((DetailScrollingActivity) getActivity()).getSupportActionBar().setTitle(s_name);

                                Log.d("sid", s_id);
                                Log.d("name", s_name);
                                Log.d("address", map_address);

//                                String state_id = jsonObject.getString("state_id");
//                                Log.d("state", state_id);

                                if (!VUStringUtility.stringNotEmpty(map_address)) {
                                 //   tvAddress.setText("---");
                                } else {
                                    tvAddress.setText(map_address);
                                }

                                if (!VUStringUtility.stringNotEmpty(description_hindi)) {
                                  //  tv_description.setText("---");
                                } else {
                                    tv_description.setText(description_hindi);
                                }


                              //  tv_mobile.setText("---");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                        }
                    }
                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
}
