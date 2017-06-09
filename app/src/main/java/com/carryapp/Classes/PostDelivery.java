package com.carryapp.Classes;

import com.carryapp.AsyncTasks.SearchPostsAsyncTask;

/**
 * Created by siddhi jambhale on 6/3/2017.
 */

public class PostDelivery {

    public String mPt_id,mPt_name,mPtDetail,mPtStartLoc,mPtEndLoc,mPtDate,mPtPhoto,mPtSize,mPtWeight,mPtCharges,mPt_Track,mUserName,mRating,mUserId;

    public boolean isChecked;
    public int mPosition;

    public void postDelivery()
    {

    }

    public void PostDelivery(String pt_id, String pt_name, String pt_detail, String pt_start_loc, String pt_end_loc, String pt_date, String pt_photo, String pt_size, String pt_weight, String pt_charges, String pt_track, String username,String rating,String userid)
    {

        this.mPt_id = pt_id;
        this.mPt_name = pt_name;
        this.mPtDetail = pt_detail;
        this.mPtStartLoc = pt_start_loc;
        this.mPtEndLoc = pt_end_loc;
        this.mPtDate = pt_date;
        this.mPtPhoto = pt_photo;
        this.mPtSize = pt_size;
        this.mPtWeight = pt_weight;
        this.mPtCharges = pt_charges;
        this.mPt_Track = pt_track;
        this.mUserName = username;
        this.mRating = rating;
        this.mUserId = userid;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public void setmPt_id(String mPt_id) {
        this.mPt_id = mPt_id;
    }

    public void setmPt_name(String mPt_name) {
        this.mPt_name = mPt_name;
    }

    public void setmPt_Track(String mPt_Track) {
        this.mPt_Track = mPt_Track;
    }

    public void setmPtCharges(String mPtCharges) {
        this.mPtCharges = mPtCharges;
    }

    public void setmPtDate(String mPtDate) {
        this.mPtDate = mPtDate;
    }

    public void setmPtDetail(String mPtDetail) {
        this.mPtDetail = mPtDetail;
    }

    public void setmPtEndLoc(String mPtEndLoc) {
        this.mPtEndLoc = mPtEndLoc;
    }

    public void setmPtPhoto(String mPtPhoto) {
        this.mPtPhoto = mPtPhoto;
    }

    public void setmPtSize(String mPtSize) {
        this.mPtSize = mPtSize;
    }

    public void setmPtStartLoc(String mPtStartLoc) {
        this.mPtStartLoc = mPtStartLoc;
    }

    public void setmPtWeight(String mPtWeight) {
        this.mPtWeight = mPtWeight;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPt_id() {
        return mPt_id;
    }

    public String getmPtEndLoc() {
        return mPtEndLoc;
    }

    public String getmPt_name() {
        return mPt_name;
    }

    public String getmPt_Track() {
        return mPt_Track;
    }

    public String getmPtCharges() {
        return mPtCharges;
    }

    public String getmPtDate() {
        return mPtDate;
    }

    public String getmPtDetail() {
        return mPtDetail;
    }

    public String getmPtPhoto() {
        return mPtPhoto;
    }

    public String getmPtSize() {
        return mPtSize;
    }

    public String getmPtStartLoc() {
        return mPtStartLoc;
    }

    public String getmPtWeight() {
        return mPtWeight;
    }

    public String getmUserName() {
        return mUserName;
    }
}
