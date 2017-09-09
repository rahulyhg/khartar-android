package com.vardhamaninfo.khartargaccha.Model;

public class TodayTithi {

    private String name;
    private String image;
    private String date;

    public TodayTithi() {
    }

    public TodayTithi(String name, String image, String date) {
        this.name = name;
        this.image = image;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}