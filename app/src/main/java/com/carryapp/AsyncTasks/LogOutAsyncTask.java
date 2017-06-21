package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.LoginActivity;
import com.carryapp.Activities.MainActivity;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddhi jambhale on 6/20/2017.
 */

public class LogOutAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private CoordinatorLayout parentLayout;


    public LogOutAsyncTask(Context context, CoordinatorLayout parentLayout) {

        this.mContext = context;
        this.parentLayout = parentLayout;
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
            api = mContext.getResources().getString(R.string.url) + "signout";

            jsonParams = new JSONObject();

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendPostRequest(params[0]);

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

                if (message.equals("Signout Successfully.")) {

                    LoginManager.getInstance().logOut();
                    SharedPreferences pref = mContext.getSharedPreferences("appdata", mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();

                    ((HomeActivity) mContext).finish();

                    Intent mIntent = new Intent(mContext, MainActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mContext.startActivity(mIntent);

                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                }
                else if (message.equals("Access Denied. Invalid Api key")) {
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    LoginManager.getInstance().logOut();
                    SharedPreferences pref = mContext.getSharedPreferences("appdata", mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();

                    snackbar = Snackbar.make(parentLayout,R.string.warning, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    ((HomeActivity) mContext).finish();

                    snackbar.addCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            //see Snackbar.Callback docs for event details
                            Intent mIntent = new Intent(mContext, MainActivity.class);
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            mContext.startActivity(mIntent);
                        }
                    });


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