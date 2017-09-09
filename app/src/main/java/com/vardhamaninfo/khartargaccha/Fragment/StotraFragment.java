package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.adapters.JSONArrayAdapter;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;
import com.vardhamaninfo.khartargaccha.Activity.StavanAudioActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class StotraFragment extends Fragment {
    ListView stavansang;
    String s_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stotra, container, false);
        stavansang = (ListView) view.findViewById(R.id.stotra);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_menu_stotra);
        getstavansangrah();
        return view;
    }


    void getstavansangrah() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getStotraDetails();

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();

                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());

//        new VUBaseAsyncTask(getActivity(), null, true, Constant.HOST + Constant.S_sangrah + "StotraFragment", VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//
//                            try {


                                stavansang.setAdapter(new JSONArrayAdapter(getActivity(), result.getJSONArray("stavan_array"),
                                        R.layout.custom_list, new String[]{"title"}, new int[]{R.id.name}) {

                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);

                                        final JSONObject item = (JSONObject) getItem(position);
                                        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
                                        final ImageView dadaimage = (ImageView) view.findViewById(R.id.dadaimage);

                                        try {
                                            String image = item.getString("image");
                                            if(!image.isEmpty()) {

                                                Log.e(" image url ",image);

                                              //  image = "http:/mahasammelan.in/uploads/"+image;
                                                image = image.replace(" ","%20");

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

                                                try {
                                                    s_id = item.getString("id");
                                                    Intent intent = new Intent(getActivity(), StavanAudioActivity.class);
                                                    intent.putExtra("satvansangrah", s_id);
                                                    startActivity(intent);


                                                    Log.d("id", s_id);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });


                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                try {
                                                    s_id = item.getString("id");
                                                    Intent intent = new Intent(getActivity(), StavanAudioActivity.class);
                                                    intent.putExtra("satvansangrah", s_id);
                                                    startActivity(intent);


                                                    Log.d("id", s_id);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                //bundle.putString("sid", s_id);
                                               /* Intent intent=new Intent(getActivity(),Stavansangrah_activity.class);
                                                intent.putExtra("",bundle);
                                                startActivity(intent);*/
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
                               // jsonArray = result.getJSONArray("user_array");
                                jsonArray = result.getJSONArray("stavan_array");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject = jsonArray.getJSONObject(i);


                                    String sid = jsonObject.getString("id");
                                    Log.d("gname", sid);
                                    String s_title = jsonObject.getString("title");
                                    Log.d("address", s_title);
                                   /* String sg_author=jsonObject.getString("author");
                                    Log.d("type",sg_author);*/
                                    String s_audiofile = jsonObject.getString("audio_file");
                                    Log.d("state", s_audiofile);

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {/*lv.setVisibility(root.GONE);
                            Toast.makeText(getActivity(), "No group available", Toast.LENGTH_SHORT).show();*/
                        }
                    }


                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);


    }


}
