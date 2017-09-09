package com.vardhamaninfo.khartargaccha.Model;

public class Video {

    private String videoID;
    private String channelID;
    private String title;
    private String description;
    private String imageURL;

    public Video(String videoID, String channelID, String title, String description, String imageURL) {
        this.videoID = videoID;
        this.channelID = channelID;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
