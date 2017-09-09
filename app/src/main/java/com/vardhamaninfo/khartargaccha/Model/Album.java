package com.vardhamaninfo.khartargaccha.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Album {

    private String id;
    private String albumImageURL;
    private String albumImageName;

    public Album(String id, String albumImageURL, String albumImageName) {
        this.id = id;
        this.albumImageURL = albumImageURL;
        this.albumImageName = albumImageName;
    }

    public Album(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getString("id");
        this.albumImageURL = jsonObject.getString("gallery_image");
        this.albumImageName = jsonObject.getString("gallery_image_name");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumImageURL() {
        return albumImageURL;
    }

    public void setAlbumImageURL(String albumImageURL) {
        this.albumImageURL = albumImageURL;
    }

    public String getAlbumImageName() {
        return albumImageName;
    }

    public void setAlbumImageName(String albumImageName) {
        this.albumImageName = albumImageName;
    }
}
