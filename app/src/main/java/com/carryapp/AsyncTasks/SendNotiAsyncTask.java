package com.carryapp.AsyncTasks;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.carryapp.Fragments.TransportListFragment;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddhi jambhale on 6/9/2017.
 */

public class SendNotiAsyncTask  extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private LinearLayout parentLayout;
    private GetCostAsyncTask.GetCostCallBack getCostCallBack;
    private SendNotificationCallBack sendNotificationCallBack;

    public SendNotiAsyncTask(Context context,LinearLayout  parentLayout) {

        this.mContext = context;
        this.parentLayout = parentLayout;
        this.getCostCallBack = getCostCallBack;

    }
    public interface SendNotificationCallBack {
        void doPostExecute(Boolean noti);
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
            api = mContext.getResources().getString(R.string.url) + "requestdelivery";

            jsonParams = new JSONObject();

            jsonParams.put("pt_id", params[0]);
            jsonParams.put("nt_receiver_id", params[1]);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendPostRequest(params[2]);

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

                if (message.equals("Success")) {

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    snackbar = Snackbar.make(parentLayout,R.string.deliveryRequest, Snackbar.LENGTH_LONG);
                    snackbar.show();

                 //   sendNotificationCallBack.doPostExecute(true);

                } else if (message.equals("Sorry, No device id available.")) {

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    snackbar = Snackbar.make(parentLayout,message, Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
                else if (message.equals("Access Denied. Invalid Api key")) {
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    snackbar = Snackbar.make(parentLayout,R.string.warning, Snackbar.LENGTH_LONG);
                    snackbar.show();

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
