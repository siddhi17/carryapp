package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.MainActivity;
import com.carryapp.Classes.Notifications;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/11/2017.
 */

public class GetNotificationsAsyncTask  extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private LinearLayout parentLayout;
    private JSONArray list;
    private ArrayList<Notifications> notificationsArrayList;
    private GetNotificationsCallBack getNotificationsCallBack;

    public GetNotificationsAsyncTask(Context context, GetNotificationsCallBack getNotificationsCallBack,LinearLayout parentLayout) {

        this.mContext = context;
        this.parentLayout = parentLayout;
        this.getNotificationsCallBack = getNotificationsCallBack;

    }

    public interface GetNotificationsCallBack {
        void doPostExecute(ArrayList<Notifications> notifications);
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
            api = mContext.getResources().getString(R.string.url) + "getnotification";

            jsonParams = new JSONObject();


            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendPostRequest(params[0]);

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
                    list = response.getJSONArray("notificationlist");

                    notificationsArrayList = new ArrayList<>();

                    for (int j = 0; j < list.length(); j++) {

                        JSONObject jsonObject = list.getJSONObject(j);

                        Notifications notifications = new Notifications();

                        notifications.setNt_id(jsonObject.getString("nt_id"));
                        notifications.setNt_message(jsonObject.getString("nt_message"));
                        notifications.setNt_status(jsonObject.getString("nt_status"));

                        notificationsArrayList.add(notifications);

                    }
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    getNotificationsCallBack.doPostExecute(notificationsArrayList);
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
