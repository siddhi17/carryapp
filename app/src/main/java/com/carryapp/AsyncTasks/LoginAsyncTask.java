package com.carryapp.AsyncTasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddhi jambhale on 6/1/2017.
 */

public class LoginAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private CoordinatorLayout parentLayout;

    public LoginAsyncTask(Context context, CoordinatorLayout linearLayout) {

        this.mContext = context;
        this.parentLayout = linearLayout;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        loadingDialog = new ProgressDialog(mContext);

        if (!isOnline()) {

            snackbar = Snackbar.make(parentLayout, R.string.check_network, Snackbar.LENGTH_LONG);
            snackbar.show();

        } else {

            loadingDialog.show(mContext, null, mContext.getString(R.string.wait));
            loadingDialog.setCancelable(false);
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            api = mContext.getResources().getString(R.string.url) + "signin";

            jsonParams = new JSONObject();

            jsonParams.put("email", params[0]);
            jsonParams.put("password", params[1]);
            jsonParams.put("deviceid", params[2]);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendRequest();

        } catch (JSONException je) {
            return Excpetion2JSON.getJSON(je);
        } catch (Exception ue) {
            return Excpetion2JSON.getJSON(ue);
        }
    }  //end of doInBackground

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();

        try {
            JSONArray jsonArray = response.getJSONArray("array");
            Log.d("ServerResponsejsonArray", "" + jsonArray);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("message")) {
                        String message = jsonObject.getString("message");
                        if (message.equals("Login failed. Incorrect credentials")) {

                            snackbar = Snackbar.make(parentLayout,R.string.alertEmail, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                        else if(message.equals("This Email ID is not valid !"))
                        {

                            snackbar = Snackbar.make(parentLayout,R.string.emailAlert, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } else if (message.equals("Required field Password is missing or empty !")) {

                            snackbar = Snackbar.make(parentLayout,message, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                        else if(message.equals("Login failed. Blank password"))
                        {

                            snackbar = Snackbar.make(parentLayout,message, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }

                    } else {


                        SessionData session = new SessionData(mContext);
                        session.add("ur_id", jsonObject.getString("ur_id"));
                        session.add("ur_name", jsonObject.getString("ur_name"));
                        session.add("ur_email", jsonObject.getString("ur_email"));
                        session.add("ur_mob_no", jsonObject.getString("ur_mob_no"));
                        session.add("ur_device_id", jsonObject.getString("ur_device_id"));
                        session.add("ur_photo", jsonObject.getString("ur_photo"));
                        session.add("api_key", jsonObject.getString("api_key"));
                        session.add("ur_car_model", jsonObject.getString("ur_car_model"));
                        session.add("ur_car_type", jsonObject.getString("ur_car_type"));
                        session.add("ur_car_photo", jsonObject.getString("ur_car_photo"));
                        session.add("ur_dni_photo", jsonObject.getString("ur_dni_photo"));
                        session.add("ur_birth_date", jsonObject.getString("ur_birth_date"));


                        Intent intent = new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);

                    }
                }

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

