package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.LoginActivity;
import com.carryapp.Activities.MainActivity;
import com.carryapp.Activities.RegisterActivity;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/1/2017.
 */

public class FacebookLoginAsyncTask  extends AsyncTask<String, Void, JSONObject> {

    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private RelativeLayout parentLayout;

    public FacebookLoginAsyncTask(Context context, RelativeLayout linearLayout) {

        this.mContext = context;
          this.parentLayout = linearLayout;

    }

    public interface RegisterCallBack {
        void doPostExecute(ArrayList<String> register);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!isOnline()) {
            //   showAlert(getString(R.string.check_network));
         /*   CommonUtils.showAlert(RegisterCustomerActivity.this, getResources().getString(R.string.check_network), "Check Network");*/
            snackbar = Snackbar.make(parentLayout, R.string.check_network, Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            loadingDialog = ProgressDialog.show(mContext, null, mContext.getString(R.string.wait));
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            api = mContext.getResources().getString(R.string.url) + "fcsignup";

            jsonParams = new JSONObject();
            jsonParams.put("username", params[0]);
            jsonParams.put("email", params[1]);
            jsonParams.put("gender", params[2]);
            jsonParams.put("bday", params[3]);
            jsonParams.put("latitude", params[4]);
            jsonParams.put("longitude", params[5]);
            jsonParams.put("deviceid", params[6]);
            jsonParams.put("photo", params[7]);


            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendRequest();

        } catch (JSONException je) {
            return Excpetion2JSON.getJSON(je);
        } catch (Exception ue) {
            ue.printStackTrace();
            return Excpetion2JSON.getJSON(ue);
        }
    }  //end of doInBackground

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);

        try {
            if (response.has("message")) {
                String message = response.getString("message");

               if (message.equals("Success")) {

                    //on successful registration go to sign in
                    JSONObject jsonObject = response.getJSONObject("user");

                    SessionData session = new SessionData(mContext);
                    session.add("ur_id", jsonObject.getString("ur_id"));
                    session.add("ur_name", jsonObject.getString("ur_name"));
                    session.add("ur_email", jsonObject.getString("ur_email"));
                    session.add("ur_mob_no", jsonObject.getString("ur_mob_no"));
                    session.add("ur_device_id", jsonObject.getString("ur_device_id"));
                    session.add("ur_photo", jsonObject.getString("ur_photo"));
                    session.add("api_key", jsonObject.getString("ur_apikey"));
                    session.add("ur_car_model", jsonObject.getString("ur_car_model"));
                    session.add("ur_car_type", jsonObject.getString("ur_car_type"));
                    session.add("ur_car_photo", jsonObject.getString("ur_car_photo"));
                    session.add("ur_dni_photo", jsonObject.getString("ur_dni_photo"));
                    session.add("ur_birth_date", jsonObject.getString("ur_birth_date"));
                    session.add("ur_rating", jsonObject.getString("ur_rating"));
                    session.add("latitude", jsonObject.getString("ur_latitude"));
                    session.add("longitude", jsonObject.getString("ur_longitude"));

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    ((MainActivity) mContext).finish();
                    Intent intent = new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);

                }

            }
        }catch (JSONException je) {
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
