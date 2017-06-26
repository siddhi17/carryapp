package com.carryapp.AsyncTasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.LoginActivity;
import com.carryapp.Activities.RegisterActivity;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by siddhi jambhale on 5/31/2017.
 */

public class RegisterAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    RegisterCallBack registerCallBack;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private LinearLayout parentLayout;
    private JSONArray jsonArray;

    public RegisterAsyncTask(Context context, LinearLayout linearLayout) {

        this.mContext = context;
        this.registerCallBack = registerCallBack;
        this.parentLayout = linearLayout;

    }

    public interface RegisterCallBack {
        void doPostExecute(ArrayList<String> register);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!isOnline()) {

            snackbar = Snackbar.make(parentLayout,R.string.check_network, Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            loadingDialog = ProgressDialog.show(mContext, null, mContext.getString(R.string.wait));
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            api = mContext.getResources().getString(R.string.url) + "signup";

            jsonParams = new JSONObject();
            jsonParams.put("username", params[0]);
            jsonParams.put("email", params[1]);
            jsonParams.put("mobile", params[2]);
            jsonParams.put("password", params[3]);
            jsonParams.put("latitude", params[4]);
            jsonParams.put("longitude", params[5]);
            jsonParams.put("deviceid", params[6]);
            jsonParams.put("photo", params[7]);


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

        try {

            if (response.has("message")) {
                String message = response.getString("message");

                        if (message.equals("Sorry, this email already existed"))
                        {
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();

                            snackbar = Snackbar.make(parentLayout, R.string.mailExists, Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                        else if(message.equals("This Email ID is not valid !"))
                        {
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();
                            snackbar = Snackbar.make(parentLayout,R.string.emailAlert, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } else if (message.equals("Required field Password is missing or empty !")) {

                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();
                            snackbar = Snackbar.make(parentLayout,message, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                    else if(message.equals("Success")){


                            JSONObject jsonObject = response.getJSONObject("user");
                            //on successful registration go to sign in

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
                            session.add("ur_rating", jsonObject.getString("ur_rating"));
                            session.add("notificationCount", 0);


                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();

                       /*     snackbar = Snackbar.make(parentLayout,R.string.registered, Snackbar.LENGTH_LONG);

                            snackbar.show();*/

                            ((RegisterActivity)mContext).finish();
                            Intent intent = new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);

                        }

                    }

        } catch (JSONException je) {
            je.printStackTrace();
            //  Toast.makeText(getApplicationContext(), je.getMessage(), Toast.LENGTH_LONG).show();
        }
    } //end of onPostExecute

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}