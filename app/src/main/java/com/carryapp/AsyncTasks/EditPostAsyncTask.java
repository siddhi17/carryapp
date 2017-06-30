package com.carryapp.AsyncTasks;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.FrameLayout;

import com.carryapp.Activities.EditPostActivity;
import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.MainActivity;
import com.carryapp.Classes.PostDelivery;
import com.carryapp.Fragments.BlankFragment;
import com.carryapp.Fragments.MyTripsFragment;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddhi jambhale on 6/30/2017.
 */

public class EditPostAsyncTask extends AsyncTask<String, Void, JSONObject> {

    String api;
    JSONObject jsonParams;
    Context mContext;
    RegisterAsyncTask.RegisterCallBack registerCallBack;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private FrameLayout parentLayout;

    private SessionData sessionData;

    public EditPostAsyncTask(Context context, FrameLayout linearLayout) {

        this.mContext = context;
        this.registerCallBack = registerCallBack;
        this.parentLayout = linearLayout;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        if (!isOnline()) {

            snackbar = Snackbar.make(parentLayout, R.string.check_network, Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            loadingDialog = ProgressDialog.show(mContext, null, mContext.getString(R.string.wait));
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            api = mContext.getResources().getString(R.string.url) + "editPost";

            jsonParams = new JSONObject();

            jsonParams.put("pt_id", params[0]);
            jsonParams.put("pt_name", params[1]);
            jsonParams.put("pt_detail", params[2]);
            jsonParams.put("pt_start_loc", params[3]);
            jsonParams.put("pt_end_loc", params[4]);
            jsonParams.put("pt_date", params[5]);
            jsonParams.put("pt_photo", params[6]);
            jsonParams.put("pt_size", params[7]);
            jsonParams.put("pt_weight", params[8]);
            jsonParams.put("pt_charges", params[9]);
            jsonParams.put("pt_track", params[10]);
            jsonParams.put("st_lati", params[11]);
            jsonParams.put("st_longi", params[12]);
            jsonParams.put("ed_lati", params[13]);
            jsonParams.put("ed_longi", params[14]);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendPostRequest(params[15]);

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

                if (message.equals("Access Denied. Invalid Api key")) {
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();


                    LoginManager.getInstance().logOut();


                    sessionData = new SessionData(mContext);
                    sessionData.clearSession();


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



                } else if (message.equals("Information updated successfully !")) {
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();


                    snackbar = Snackbar.make(parentLayout, R.string.editPostConfirm, Snackbar.LENGTH_LONG);
                    snackbar.show();

                    snackbar.addCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            //see Snackbar.Callback docs for event details

                            ((EditPostActivity) mContext).finish();

                            ((EditPostActivity) mContext).startActivity(new Intent(mContext,HomeActivity.class).putExtra("editPost",true));
                        }
                    });

                }
            }

        } catch (JSONException je) {
            je.printStackTrace();
        }
    } //end of onPostExecute

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}