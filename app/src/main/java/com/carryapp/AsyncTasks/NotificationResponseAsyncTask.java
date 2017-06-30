package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.carryapp.Classes.Notifications;
import com.carryapp.Database.NotiTableHelper;
import com.carryapp.Fragments.NoticesFragment;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/12/2017.
 */

public class NotificationResponseAsyncTask  extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private CoordinatorLayout parentLayout;
    private NoticesFragment noticesFragment;
    private NotiTableHelper db;
    private NotiResponseCalBack notiResponseCalBack;
    private String ntId;


    public NotificationResponseAsyncTask(Context context, NoticesFragment noticesFragment,NotiResponseCalBack notiResponseCalBack1) {

        this.mContext = context;
        this.noticesFragment = noticesFragment;
        this.notiResponseCalBack = notiResponseCalBack1;
    }



    public interface NotiResponseCalBack {
        void doPostExecute(JSONObject jsonObject);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!isOnline()) {

            snackbar = Snackbar.make(noticesFragment.getView(), R.string.check_network, Snackbar.LENGTH_LONG);
            snackbar.show();

        } else {

            loadingDialog = ProgressDialog.show(mContext, null, mContext.getString(R.string.wait));
            loadingDialog.setCancelable(false);
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            api = mContext.getResources().getString(R.string.url) + "accept";

            jsonParams = new JSONObject();

            jsonParams.put("pt_id", params[0]);
            jsonParams.put("nt_receiver_id", params[1]);
            jsonParams.put("accept_status", params[2]);
            jsonParams.put("nt_id", params[4]);


            ntId = params[4];

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendPostRequest(params[3]);

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

                    db = new NotiTableHelper(mContext);

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    noticesFragment.setNotifications();

                    Notifications notifications = new Notifications();
                    notifications.setNt_status(response.getString("acceptance"));
                    notifications.setNt_id(ntId);

                    db.updateNoti(notifications);

                    noticesFragment.getLocalData();
                    noticesFragment.setData();
                    // notiResponseCalBack.doPostExecute(true);

                }
                else if (message.equals("Access Denied. Invalid Api key")) {

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    snackbar = Snackbar.make(noticesFragment.getView(),R.string.warning, Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
                else if (message.equals("Sorry, No device id available.")) {

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                }
                else if (message.equals("Already accepted.")) {

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                }
                else if (message.equals("Already rejected.")) {

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                }
            }

        } catch (JSONException je) {

            je.printStackTrace();

        }

        notiResponseCalBack.doPostExecute(response);

    } //end of onPostExecute
    //check network
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
