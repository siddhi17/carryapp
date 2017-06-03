package com.carryapp.AsyncTasks;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.LinearLayout;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.LoginActivity;
import com.carryapp.Activities.RegisterActivity;
import com.carryapp.Classes.PostDelivery;
import com.carryapp.Fragments.CarPickerFragment;
import com.carryapp.Fragments.MainFragment;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/3/2017.
 */

public class PostDeliveryAsyncTask extends AsyncTask<String, Void, JSONObject> {

    String api;
    JSONObject jsonParams;
    Context mContext;
    RegisterAsyncTask.RegisterCallBack registerCallBack;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private LinearLayout parentLayout;

    public PostDeliveryAsyncTask(Context context, LinearLayout linearLayout) {

        this.mContext = context;
        this.registerCallBack = registerCallBack;
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
            loadingDialog.show(mContext, null,mContext.getString(R.string.wait));
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            api = mContext.getResources().getString(R.string.url) + "signup";

            jsonParams = new JSONObject();
            jsonParams.put("pt_name", params[0]);
            jsonParams.put("pt_detail", params[1]);
            jsonParams.put("pt_start_loc", params[2]);
            jsonParams.put("pt_end_loc", params[3]);
            jsonParams.put("pt_date", params[4]);
            jsonParams.put("pt_photo", params[5]);
            jsonParams.put("pt_size", params[6]);
            jsonParams.put("pt_weight", params[7]);
            jsonParams.put("pt_charges", params[8]);
            jsonParams.put("pt_track", params[9]);


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

                        if (message.equals("Access Denied. Invalid Api key"))
                        {
                            snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }

                    } else {

                        PostDelivery postDelivery = new PostDelivery();

                        postDelivery.setmPt_name(jsonObject.getString("pt_name"));
                        postDelivery.setmPtDetail(jsonObject.getString("pt_detail"));
                        postDelivery.setmPtSize(jsonObject.getString("pt_size"));
                        postDelivery.setmPtWeight(jsonObject.getString("pt_weight"));
                        postDelivery.setmPtStartLoc(jsonObject.getString("pt_start_loc"));
                        postDelivery.setmPtEndLoc(jsonObject.getString("pt_end_loc"));


                        FragmentManager fragmentManager =  ((HomeActivity)mContext).getFragmentManager();
                        CarPickerFragment fragment = new CarPickerFragment();
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment,"CAR_PICKER_FRAGMENT").addToBackStack("J").commit();


                    }
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
