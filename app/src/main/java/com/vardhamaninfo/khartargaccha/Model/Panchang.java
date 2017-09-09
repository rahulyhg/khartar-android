package com.vardhamaninfo.khartargaccha.Model;

public class Panchang {

    private String shubhDin;
    private String date;
    private String day;
    private String lunarYear;
    private String lunarDay;
    private String lunarCycle;
    private String lunarMonth;
    private String description;

    public Panchang() {
    }

    public Panchang(String shubhDin, String date, String day, String lunarYear, String lunarDay,
                    String lunarCycle, String lunarMonth, String description) {
        this.shubhDin = shubhDin;
        this.date = date;
        this.day = day;
        this.lunarYear = lunarYear;
        this.lunarDay = lunarDay;
        this.lunarCycle = lunarCycle;
        this.lunarMonth = lunarMonth;
        this.description = description;
    }

    public String getShubhDin() {
        return shubhDin;
    }

    public void setShubhDin(String shubhDin) {
        this.shubhDin = shubhDin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(String lunarYear) {
        this.lunarYear = lunarYear;
    }

    public String getLunarDay() {
        return lunarDay;
    }

    public void setLunarDay(String lunarDay) {
        this.lunarDay = lunarDay;
    }

    public String getLunarCycle() {
        return lunarCycle;
    }

    public void setLunarCycle(String lunarCycle) {
        this.lunarCycle = lunarCycle;
    }

    public String getLunarMonth() {
        return lunarMonth;
    }

    public void setLunarMonth(String lunarMonth) {
        this.lunarMonth = lunarMonth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}