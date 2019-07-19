package com.example.android.play_api;

import java.io.Serializable;
import java.util.Date;

public class item implements Serializable {

    private String month;
    private int day;
    private String title;
    private Date date;
    private String bible;
    private String devotion;
    private String prayer;

    item(String month,int day,String title,Date date,String bible,String devotion,String prayer){
        this.month = month;
        this.day = day;
        this.title = title;
        this.date = date;
        this.bible = bible;
        this.devotion = devotion;
        this.prayer = prayer;
    }

    public int getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getTitle() {
        return title;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBible() {
        return bible;
    }

    public void setBible(String bible) {
        this.bible = bible;
    }

    public String getDevotion() {
        return devotion;
    }

    public String getPrayer() {
        return prayer;
    }


    public void setDevotion(String devotion) {
        this.devotion = devotion;
    }

    public void setPrayer(String prayer) {
        this.prayer = prayer;
    }
}
