package com.example.rashid.lostandfound.ListView;

import android.graphics.Bitmap;

/**
 * Created by rashid on 8/20/2017.
 */

public class MyListDataClass
{
    String text1,text2, timedate;
    Bitmap img;
    public MyListDataClass(String text1, String text2, Bitmap img, String td) {
        this.text1 = text1;
        this.text2 = text2;
        this.img = img;
        this.timedate = td;
    }

    public String getTimedate() {
        return timedate;
    }

    public void setTimedate(String timedate) {
        this.timedate = timedate;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
