package com.carryapp.Classes;

/**
 * Created by siddhi jambhale on 5/27/2017.
 */

public class TravelHistory {

    String mFrom,mTo,mDate,mImage;

    public TravelHistory(){}

    public TravelHistory(String from, String to,String date,String image)
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
