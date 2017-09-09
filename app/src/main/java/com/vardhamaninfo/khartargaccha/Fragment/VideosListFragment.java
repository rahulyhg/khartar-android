package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Adapter.VideoListAdapter;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.Model.Video;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class VideosListFragment extends Fragment {

    private RecyclerView recyclerView;
    private VideoListAdapter videoListAdapter;
    private ProgressDialog pDialog;

    private ArrayList<Video> videos;

    public VideosListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("यूट्यूब प्लेलिस्ट");

        View view = inflater.inflate(R.layout.fragment_videos_list, container, false);

        videos = new ArrayList<Video>();

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_videos_list_recyclerview);

        videoListAdapter = new VideoListAdapter(getActivity(), videos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(videoListAdapter);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

       // setStatic();
//        getVideoList();

        getYouTubeVideos();

        return view;
    }

    public JSONObject response() {

        String res = "";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = new JSONObject(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void getVideoList(JSONObject result) {

       // JSONObject response = response();

        try {

            JSONArray jsonArray = result.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {

                String videoID = jsonArray.getJSONObject(i).getJSONObject("id").getString("videoId");
                String channelID = jsonArray.getJSONObject(i).getJSONObject("snippet").getString("channelId");
                String title = jsonArray.getJSONObject(i).getJSONObject("snippet").getString("title");
                String description = jsonArray.getJSONObject(i).getJSONObject("snippet").getString("description");
                String imageURL = jsonArray.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                videos.add(new Video(videoID, channelID, title, description, imageURL));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        videoListAdapter = new VideoListAdapter(getActivity(), videos);

        videoListAdapter.notifyDataSetChanged();

    }

    public void setStatic() {

        Log.d("setStatic", "called");

        videos.add(new Video("VQVAlohT2UE", "UC3sZ1hO59ZaLPxBhvJBoiYA", "networkish", "description", "https://i.ytimg.com/vi/VQVAlohT2UE/default.jpg"));
        videos.add(new Video("VQVAlohT2UE", "UC3sZ1hO59ZaLPxBhvJBoiYA", "networkish asdf sd fs adf saf ddsaf asdf sda fsda sda fs ad fsda fsad fs d", "descriptionsad fsd fsda fsadf sda fassda fsda fsda fsda fasd fsdf sdaf sda fsd fd fsdaf sdfsda sadf sdaf sdaf sdf sd fsdfsdaf sdaf sdaf sda fsad  dsa fd fsdf saf sa fd ", "https://i.ytimg.com/vi/VQVAlohT2UE/default.jpg"));

        Log.d("videos", "size " + videos.size());

//        videoListAdapter = new VideoListAdapter(getActivity(), videos);
//        recyclerView.setAdapter(videoListAdapter);
        videoListAdapter.notifyDataSetChanged();
    }


    void getYouTubeVideos() {

        Call<JsonObject> call = Application.getAppInstance().getServerBackendYouTube(RestAdapter.AuthType.UNAUTHARIZED)
//                .getAllTirthDetails();
                .getYouTubeVideosDetails();

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                if (success) {
                    pDialog.dismiss();
                    try {

                        JSONObject result = new JSONObject(data.body().toString());

                        Log.e("  Response ", data.body().toString() + " response");
                        getVideoList(result);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    if (data == null){

                        getYouTubeVideos();


                    }else {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.youtube_video_list), Toast.LENGTH_SHORT).show();

                    }
                }
            }


        });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

}
//https://www.googleapis.com/youtube/v3/search?order=date&part=id,snippet&maxResults=50&channelId=UC3sZ1hO59ZaLPxBhvJBoiYA&key=AIzaSyCApY8NbKa8asDTitNWYu2XfISwm8P9WWw