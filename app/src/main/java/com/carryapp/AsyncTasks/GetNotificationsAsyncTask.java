package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.MainActivity;
import com.carryapp.Classes.Notifications;
import com.carryapp.Database.NotiTableHelper;
import com.carryapp.Fragments.NoticesFragment;
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
    private NoticesFragment noticesFragment;
    private SessionData sessionData;
    private boolean home;

    public GetNotificationsAsyncTask(Context context, NoticesFragment noticesFragment, GetNotificationsCallBack getNotificationsCallBack, LinearLayout parentLayout) {

        this.mContext = context;
        this.parentLayout = parentLayout;
        this.noticesFragment = noticesFragment;
        this.getNotificationsCallBack = getNotificationsCallBack;
        loadingDialog = new ProgressDialog(mContext);
    }


    public GetNotificationsAsyncTask(Context context,GetNotificationsCallBack getNotificationsCallBack) {

        this.mContext = context;
        this.getNotificationsCallBack = getNotificationsCallBack;
        loadingDialog = new ProgressDialog(mContext);
        home = true;
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
            if(!home)
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

                    sessionData = new SessionData(mContext);
                    NotiTableHelper notiTableHelper = new NotiTableHelper(mContext);

                    notiTableHelper.deleteAllNoti();

                    //on successful registration go to sign in
                    list = response.getJSONArray("notifications");

                    notificationsArrayList = new ArrayList<>();

                    for (int j = 0; j < list.length(); j++) {

                        JSONObject jsonObject = list.getJSONObject(j);

                        Notifications notifications = new Notifications();

                        notifications.setNt_id(jsonObject.getString("nt_id"));
                        notifications.setNt_message(jsonObject.getString("nt_message"));
                        notifications.setNt_status(jsonObject.getString("nt_status"));
                        notifications.setPt_id(jsonObject.getString("pt_id"));
                        notifications.setSender_id(jsonObject.getString("nt_sender_id"));

                        notificationsArrayList.add(notifications);

                        new AddNotiAsyncTask(mContext).execute(notifications.getNt_id(), notifications.getNt_message(), notifications.getPt_id(),
                                notifications.getSender_id(),notifications.getNt_status());
                    }
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    noticesFragment.mRecyclerView.setVisibility(View.VISIBLE);
                    noticesFragment.mTextViewData.setVisibility(View.GONE);

                    getNotificationsCallBack.doPostExecute(notificationsArrayList);
                }
                else if(message.equals("Sorry, try again later"))
                {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }

                    noticesFragment.mRecyclerView.setVisibility(View.GONE);
                    noticesFragment.mTextViewData.setVisibility(View.VISIBLE);

             /*   snackbar = Snackbar.make(parentLayout, R.string.noDelivery, Snackbar.LENGTH_LONG);
                snackbar.show();*/
                }
                else if (message.equals("Access Denied. Invalid Api key")) {
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    snackbar = Snackbar.make(parentLayout,R.string.warning, Snackbar.LENGTH_LONG);
                    snackbar.show();

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
