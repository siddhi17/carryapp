package com.carryapp.Classes;

/**
 * Created by siddhi jambhale on 5/22/2017.
 */

public class Transport {


    public String mDateTime,mProductImg,mProductName,mUserName,mRatings;

    public Transport(String dateTime,String productImg,String productName,String userName,String ratings)
    {
        this.mDateTime = dateTime;
        this.mProductImg = productImg;
        this.mProductName = productName;
        this.mUserName = userName;
        this.mRatings = ratings;
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public String getmProductImg() {
        return mProductImg;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmRatings() {
        return mRatings;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public void setmProductImg(String mProductImg) {
        this.mProductImg = mProductImg;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public void setmRatings(String mRatings) {
        this.mRatings = mRatings;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }
}
