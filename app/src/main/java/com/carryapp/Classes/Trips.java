package com.carryapp.Classes;

import com.carryapp.helper.SessionData;

/**
 * Created by siddhi jambhale on 5/22/2017.
 */

public class Trips {

    String mFrom,mTo,mDate,mImage,mPostId,mPostDetails,mPostStatus,mPostCharges,mPostName;

    public Trips(){}

    public Trips(String from, String to, String date, String image, String postId,String potName,String postDetails,String postCharges,String poststatus)
    {
        this.mFrom = from;
        this.mTo = to;
        this.mImage = image;
        this.mDate = date;
        this.mPostId = postId;
        this.mPostName = potName;
        this.mPostCharges = postCharges;
        this.mPostStatus = poststatus;
        this.mPostDetails = postDetails;

    }

    public String getmPostCharges() {
        return mPostCharges;
    }

    public String getmPostDetails() {
        return mPostDetails;
    }

    public String getmPostId() {
        return mPostId;
    }

    public String getmPostName() {
        return mPostName;
    }

    public String getmPostStatus() {
        return mPostStatus;
    }

    public void setmPostCharges(String mPostCharges) {
        this.mPostCharges = mPostCharges;
    }

    public void setmPostDetails(String mPostDetails) {
        this.mPostDetails = mPostDetails;
    }

    public void setmPostId(String mPostId) {
        this.mPostId = mPostId;
    }

    public void setmPostName(String mPostName) {
        this.mPostName = mPostName;
    }

    public void setmPostStatus(String mPostStatus) {
        this.mPostStatus = mPostStatus;
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
