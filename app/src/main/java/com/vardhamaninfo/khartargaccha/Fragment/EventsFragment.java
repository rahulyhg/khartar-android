package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
import com.vardhaman.vardhamanutilitylibrary.adapters.JSONArrayAdapter;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.Activity.EventDetailsActivity;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;


public class EventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    ListView eventlist;
    ImageView earrow;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events, container, false);
        eventlist = (ListView) view.findViewById(R.id.eventlist);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_menu_events);
        Getevent();

        return view;
    }

    public void Getevent() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getEventDetails();

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());


//        new VUBaseAsyncTask(getActivity(), null, true,
//                Constant.HOST + Constant.GETEVENT, VUBaseAsyncTask.METHOD_GET,
//                new VUIBaseAsyncTask() {
//            @Override
//            public void onTaskCompleted(int success, JSONObject result) {
//                if (success == 1) {
//
//                    try {

                        eventlist.setAdapter(new JSONArrayAdapter(getActivity(),
                                result.getJSONArray("event_array"), R.layout.custom_list,
                                new String[]{"event_name"}, new int[]{R.id.name}) {
                                                 @Override
                                                 public View getView(final int position, View convertView, ViewGroup parent) {
                                                     View view = super.getView(position, convertView, parent);
                                                     final JSONObject item = (JSONObject) getItem(position);
                                                     // ch=(CheckBox)view.findViewById(R.id.checka);


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


                                                     LinearLayout list_click = (LinearLayout) view.findViewById(R.id.list_click);
                                                     final TextView event_date = (TextView) view.findViewById(R.id.datetime);
                                                     String eventdate_time = null;
                                                     try {
                                                         eventdate_time = item.getString("event_date_time");
                                                         Log.d("date and time", eventdate_time);
                                                     } catch (Exception e) {
                                                         e.printStackTrace();
                                                     }

                                                     event_date.setText(eventdate_time);
                                                     list_click.setTag(item);
                                                     list_click.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {

                                                             if (v.getTag() != null) {
                                                                 try {

                                                                     String uid = item.getString("id");
                                                                     Intent intent = new Intent(getActivity(), EventDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                     intent.putExtra("event_id", uid);
                                                                     startActivity(intent);

                                                                 } catch (Exception e) {
                                                                     e.printStackTrace();
                                                                 }
                                                             }
                                                         }
                                                     });

                                                     earrow = (ImageView) view.findViewById(R.id.image);
                                                     earrow.setTag(item);
                                                     earrow.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {

                                                             if (v.getTag() != null) {
                                                                 try {
                                                                     String uid = item.getString("id");
                                                                     Intent intent = new Intent(getActivity(), EventDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                     intent.putExtra("event_id", uid);
                                                                     startActivity(intent);

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
                }
            }
        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
}
