package com.carryapp.Classes;

/**
 * Created by siddhi jambhale on 5/22/2017.
 */

public class Trips {

    String mFrom,mTo,mDate,mImage;

    public Trips(String from, String to,String date,String image)
    {
        this.mFrom = from;
        this.mTo = to;
        this.mImage = image;
        this.mDate = date;

    }

    public String getmDate() {
        return mDate;
    }

    public String getmFrom() {
        return mFrom;
    }

    public String getmImage() {
        return mImage;
    }

    public String getmTo() {
        return mTo;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public void setmTo(String mTo) {
        this.mTo = mTo;
    }
}
