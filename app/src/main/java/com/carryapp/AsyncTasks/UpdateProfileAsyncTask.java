package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddhi jambhale on 6/2/2017.
 */

public class UpdateProfileAsyncTask extends AsyncTask<String, Void, JSONObject> {

        String api;
        JSONObject jsonParams;
        Context mContext;
        private ProgressDialog loadingDialog;
        private Snackbar snackbar;
        private FrameLayout parentLayout;

        private String mName,mEmail,mAge,mCarType,mCarModel,mNumber,mUserPhoto,mCarPhoto,mDNIPhoto;

        public UpdateProfileAsyncTask(Context context, FrameLayout layout) {

            this.mContext = context;
            this.parentLayout = layout;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            if (!isOnline()) {

                snackbar = Snackbar.make(parentLayout, R.string.check_network, Snackbar.LENGTH_LONG);
                snackbar.show();

            } else {

                loadingDialog = ProgressDialog.show(mContext, null, mContext.getString(R.string.wait));
                loadingDialog.setCancelable(false);
            }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                api = mContext.getResources().getString(R.string.url) + "edituser";

                jsonParams = new JSONObject();

                mName = params[0];
                mAge = params[1];
                mNumber = params[2];
                mEmail = params[3];
                mDNIPhoto = params[4];
                mCarPhoto = params[5];
                mUserPhoto = params[6];
                mCarModel = params[7];
                mCarType = params[8];

                jsonParams.put("ur_name", params[0]);
                jsonParams.put("ur_age", params[1]);
                jsonParams.put("ur_mob_no", params[2]);
                jsonParams.put("ur_email", params[3]);
                jsonParams.put("ur_dni_photo", params[4]);
                jsonParams.put("ur_car_photo", params[5]);
                jsonParams.put("ur_photo", params[6]);
                jsonParams.put("ur_car_model", params[7]);
                jsonParams.put("ur_car_type", params[8]);

                ServerRequest request = new ServerRequest(api, jsonParams);
                return request.sendPostRequest(params[9]);


            } catch (JSONException je) {
                return Excpetion2JSON.getJSON(je);
            } catch (Exception ue) {
                return Excpetion2JSON.getJSON(ue);
            }
        }  //end of doInBackground

        @Override
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            try {
                if (response.has("message")) {
                    String message = response.getString("message");
                    if (message.equals("Information updated successfully !")) {

                        if (loadingDialog.isShowing())
                            loadingDialog.dismiss();

                        snackbar = Snackbar.make(parentLayout,message, Snackbar.LENGTH_LONG);
                        snackbar.show();

                                SessionData session = new SessionData(mContext);
                                session.add("ur_name", mName);
                                session.add("ur_email", mEmail);
                                session.add("ur_mob_no", mNumber);
                                session.add("ur_photo", mUserPhoto);
                                session.add("ur_car_model", mCarModel);
                                session.add("ur_car_type", mCarType);
                                session.add("ur_car_photo", mCarPhoto);
                                session.add("ur_dni_photo", mDNIPhoto);
                                session.add("ur_birth_date", mAge);

                            }

                        } else {

                        }

            } catch (JSONException je) {

                je.printStackTrace();


            }
        } //end of onPostExecute
        //check network
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }


}
