package com.vardhamaninfo.khartargaccha.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Gallery {

    private String id;
    private String galleryId;
    private String title;
    private String status;
    private String imageURL;
    private int totalPhotos;
    private JSONArray jsonArrayAlbumPhotos;
    private boolean hide = false;

    public Gallery(String id, String galleryId, String title, String status, String imageURL, int totalPhotos) {
        this.id = id;
        this.galleryId = galleryId;
        this.title = title;
        this.status = status;
        this.imageURL = imageURL;
        this.totalPhotos = totalPhotos;
        this.jsonArrayAlbumPhotos = null;
    }

    public Gallery(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.galleryId = jsonObject.getString("gallery_id");
        this.title = jsonObject.getString("title");
        this.status = jsonObject.getString("status");

        if (jsonObject.getJSONArray("images_array").length() >= 1) {

            Log.d("hide", "false");

            this.jsonArrayAlbumPhotos = jsonObject.getJSONArray("images_array");
            this.totalPhotos = jsonObject.getJSONArray("images_array").length();
            this.imageURL = jsonObject.getJSONArray("images_array").getJSONObject(0).getString("gallery_image");
        } else {

            Log.d("hide", "true");

            hide = true;
            this.totalPhotos = 0;
            this.imageURL = null;
            this.jsonArrayAlbumPhotos = null;
        }
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public JSONArray getJsonArrayAlbumPhotos() {
        return jsonArrayAlbumPhotos;
    }

    public void setJsonArrayAlbumPhotos(JSONArray jsonArrayAlbumPhotos) {
        this.jsonArrayAlbumPhotos = jsonArrayAlbumPhotos;
    }
}