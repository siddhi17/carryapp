package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by siddhi jambhale on 6/5/2017.
 */

public class GetCostAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private CoordinatorLayout parentLayout;
    private GetCostCallBack getCostCallBack;

    public GetCostAsyncTask(Context context, GetCostCallBack getCostCallBack) {

        this.mContext = context;
        this.getCostCallBack = getCostCallBack;

    }
    public interface GetCostCallBack {
        void doPostExecute(Double cost);
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
            api = mContext.getResources().getString(R.string.url) + "charge";

            jsonParams = new JSONObject();

            jsonParams.put("pt_size", params[0]);
            jsonParams.put("pt_weight", params[1]);
            jsonParams.put("pt_dist", params[2]);

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

           if (message.equals("Success")) {

                    Double cost = response.getDouble("cost");

                    getCostCallBack.doPostExecute(cost);

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

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
