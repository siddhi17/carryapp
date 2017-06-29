package com.carryapp.AsyncTasks;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Fragments.MyTripsFragment;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddhi jambhale on 6/28/2017.
 */

public class DeletePostAsyncTask  extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private LinearLayout parentLayout;
    private GetCostAsyncTask.GetCostCallBack getCostCallBack;
    private FragmentManager fragmentManager;
    public DeletePostAsyncTask(Context context,LinearLayout parentLayout ) {

        this.mContext = context;
        this.parentLayout = parentLayout;
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
            api = mContext.getResources().getString(R.string.url) + "delete/" + params[0];

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendDeleteRequest(params[1]);

        }catch (Exception ue) {
            return Excpetion2JSON.getJSON(ue);
        }
    }  //end of doInBackground

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);


        try {


            if (response.has("message")) {
                String message = response.getString("message");

                if (message.equals("Deleted Successfully.")) {

                    snackbar = Snackbar.make(parentLayout, R.string.deletedPost, Snackbar.LENGTH_LONG);
                    snackbar.show();

                    fragmentManager = ((HomeActivity) mContext).getFragmentManager();
                    MyTripsFragment fragment4 = new MyTripsFragment();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment4,"MY_TRIPS_FRAGMENT").addToBackStack("H").commit();

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