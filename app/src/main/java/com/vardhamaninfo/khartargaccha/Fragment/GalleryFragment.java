package com.vardhamaninfo.khartargaccha.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.gson.JsonObject;
import com.vardhamaninfo.khartargaccha.Adapter.GalleryAdapter;
import com.vardhamaninfo.khartargaccha.Activity.AlbumActivity;
import com.vardhamaninfo.khartargaccha.Application.Application;
import com.vardhamaninfo.khartargaccha.Interface.ClickListener;
import com.vardhamaninfo.khartargaccha.Listenter.RecyclerTouchListener;
import com.vardhamaninfo.khartargaccha.Model.Gallery;
import com.vardhamaninfo.khartargaccha.R;
import com.vardhamaninfo.khartargaccha.Retrofit.HttpServerBackend;
import com.vardhamaninfo.khartargaccha.Retrofit.RestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GalleryFragment extends Fragment {

    //    GridView gallery;
    View view;
    RecyclerView recyclerViewGallery;
    private List<Gallery> galleryList = new ArrayList<>();
    private GalleryAdapter galleryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.gallery_navigation, container, false);
//        gallery = (GridView) view.findViewById(R.id.gallery_grid);

        recyclerViewGallery = (RecyclerView) view.findViewById(R.id.gallery_navigation_recyclerview);

        galleryAdapter = new GalleryAdapter(getActivity(), galleryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewGallery.setLayoutManager(mLayoutManager);
        recyclerViewGallery.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGallery.setAdapter(galleryAdapter);

        recyclerViewGallery.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewGallery, new ClickListener() {
            @Override
            public void onClick(View view, int position) {



                Log.e("Album", galleryList.get(position).getJsonArrayAlbumPhotos().toString());
                Log.e("AlbumName", galleryList.get(position).getTitle());

                Intent albumActivity = new Intent(getActivity(), AlbumActivity.class);
                albumActivity.putExtra("Album", galleryList.get(position).getJsonArrayAlbumPhotos().toString());
                albumActivity.putExtra("AlbumName", galleryList.get(position).getTitle());
                startActivity(albumActivity);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Log.d("some", "done");

        galleryImage();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_menu_gallery);

        /*gallery.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.gallry_item, R.id.txt,
                new String[]{"", "", "", "", "", ""}));*/
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.dadawadi_listview, container, false);

        return view;
    }

    void galleryImage() {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        Call<JsonObject> call = Application.getAppInstance().getServerBackend(RestAdapter.AuthType.UNAUTHARIZED)
                .getGalleryDetails();

        new HttpServerBackend(Application.getAppInstance()).getData(call, new HttpServerBackend.ResponseListener() {
            @Override
            public void onReturn(boolean success, Response<JsonObject> data) {

                pDialog.dismiss();
                if (success) {

                    Log.e("  Response ", data.body().toString() + " response");
                    try {

                        JSONObject result = new JSONObject(data.body().toString());

//
//        new VUBaseAsyncTask(getActivity(), null, true, Constant.HOST + Constant.Gallery, VUBaseAsyncTask.METHOD_GET,
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//                            try {



                                JSONArray jsonArray = result.getJSONArray("gallery_array");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    LinearLayout hiddenLayout = (LinearLayout) view.findViewById(R.id.gallery_title_lay);
//                                    LinearLayout myLayout1 = (LinearLayout) view.findViewById(R.id.category2);
//
//                                    View hiddenInfo1 = getActivity().getLayoutInflater().inflate(R.layout.gallery_title, myLayout1, false);
//
//                                    TextView title = (TextView) hiddenInfo1.findViewById(R.id.gallery_title);
//
//                                    title.setText(job.getString("title"));
//                                    Log.d("title", "" + job.getString("title"));
                                    Gallery galleryitem = new Gallery(jsonObject);
                                    galleryList.add(galleryitem);
//                                    JSONArray jarr1 = job.getJSONArray("images_array");
//                                    for (int k = 0; k < jarr1.length(); k++) {
//                                        JSONObject job1 = jarr1.getJSONObject(k);
//
//                                        LinearLayout hiddenLayout1 = (LinearLayout) view.findViewById(R.id.gall_img);
//                                        LinearLayout myLayout2 = (LinearLayout) hiddenInfo1.findViewById(R.id.show_product1);
//                                        View hiddenInfo2 = getActivity().getLayoutInflater().inflate(R.layout.gallery_images, myLayout2, false);
//                                        GridView gv = (GridView) hiddenInfo2.findViewById(R.id.gallery_grid);
//
//
//                                        gv.setAdapter(new JSONArrayAdapter(getActivity(), job.getJSONArray("images_array"), R.layout.gallry_item, new String[]{"gallery_image_name"}, new int[]{R.id.text}) {
//                                                          @Override
//                                                          public View getView(final int position, View convertView, ViewGroup parent) {
//                                                              View view1 = super.getView(position, convertView, parent);
//                                                              final JSONObject item = (JSONObject) getItem(position);
//                                                              ImageView img = (ImageView) view1.findViewById(R.id.gallery_img);
//
//                                                              try {
//                                                                  Log.d("my_img", "" + item.getString("gallery_image"));
//                                                                  Glide.with(getActivity()).load(item.getString("thumbnail_gallery_image")).into(img);
//                                                              } catch (Exception e) {
//                                                                  e.printStackTrace();
//                                                              }
//
//                                                              //img.setTag(item);
//
//                                                              img.setOnClickListener(new View.OnClickListener() {
//                                                                  @Override
//                                                                  public void onClick(View v) {
//
//                                                                      try {
//                                                                          String img_url = item.getString("medium_gallery_image");
//                                                                          Intent k = new Intent(getActivity(), ZoomImageActivity.class);
//                                                                          k.putExtra("img_url", img_url);
//                                                                          startActivity(k);
//                                                                      } catch (Exception e) {
//                                                                          e.printStackTrace();
//                                                                      }
//
//
//                                                                  }
//                                                              });
//                                                              return view1;
//                                                          }
//                                                      }
//                                        );
//                                        myLayout2.addView(hiddenInfo2);
//                                    }
//                                    myLayout1.addView(hiddenInfo1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            galleryAdapter.notifyDataSetChanged();
                        } else {
                        }
                    }
                });//.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
//
//    void getgallery() {
//
//        AsyncTask<Void, String, JSONObject> voidStringJSONObjectAsyncTask = new VUBaseAsyncTask(getActivity(), null, false, Constant.HOST + Constant.Gallery, VUBaseAsyncTask.METHOD_GET,
//
//                new VUIBaseAsyncTask() {
//                    @Override
//                    public void onTaskCompleted(int success, final JSONObject result) {
//                        if (success == 1) {
//
//                            JSONArray jsonArray = null;
//
//                            try {
//                                jsonArray = result.getJSONArray("gallery_array");
//                                Log.d("1111", String.valueOf(jsonArray));
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    Log.d("length", String.valueOf(jsonArray.length()));
//                                    JSONObject job = jsonArray.getJSONObject(i);
//
//                                    JSONArray jar = job.getJSONArray("images_array");
//                                    Log.d("array", String.valueOf(jar));
//                                    gallery.setAdapter(new JSONArrayAdapter(getActivity(), job.getJSONArray("images_array"), R.layout.gallry_item, new String[]{"gallery_image_name"}, new int[]{R.id.text}) {
//
//                                        @Override
//                                        public View getView(int position, View convertView, ViewGroup parent) {
//                                            View view = super.getView(position, convertView, parent);
//                                            JSONObject item = (JSONObject) getItem(position);
//                                            //   JSONArray items=(JSONArray)getItem(position);
//                                            ImageView galleryimageiew = (ImageView) view.findViewById(R.id.gallery_img);
//                                            try {
//                                                Glide.with(getActivity()).load(item.getString("gallery_image")).into(galleryimageiew);
//                                                Log.d("myimg", "" + item.getString("gallery_image"));
//
//
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            return view;
//                                        }
//                                    });
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            // gallery.setAdapter(new JSONArrayAdapter(getActivity(), result.getJSONArray("images_array"), R.layout.gallry_item, new String[]{"gallery_image"}, new int[]{R.id.gallery_img})
//
//                            //
//                        } else {
//                            Toast.makeText(getActivity(), "Not found Gallery", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//
//                }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
//    }
}



