package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;

import com.carryapp.Classes.Notifications;
import com.carryapp.Classes.Trips;
import com.carryapp.Fragments.MyTripsFragment;
import com.carryapp.Fragments.NoticesFragment;
import com.carryapp.Fragments.ScheduledTravelFragment;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/20/2017.
 */

public class GetScheduledTripsAsyncTask  extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private LinearLayout parentLayout;
    private JSONArray list;
    private ArrayList<Trips> tripsArrayList;
    private GetScheduleTripsCallBack getScheduleTripsCallBack;
    private ScheduledTravelFragment tripsFragment;

    public GetScheduledTripsAsyncTask(Context context, ScheduledTravelFragment tripsFragment, GetScheduleTripsCallBack getScheduleTripsCallBack) {

        this.mContext = context;
        this.parentLayout = parentLayout;
        this.tripsFragment = tripsFragment;
        this.getScheduleTripsCallBack = getScheduleTripsCallBack;

    }

    public interface GetScheduleTripsCallBack {
        void doPostExecute(ArrayList<Trips> trips);
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
            api = mContext.getResources().getString(R.string.url) + "tripschedule";

            jsonParams = new JSONObject();

            jsonParams.put("datetime",params[0]);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendPostRequest(params[1]);

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
                    list = response.getJSONArray("schedule");

                    tripsArrayList = new ArrayList<>();

                    for (int j = 0; j < list.length(); j++) {

                        JSONObject jsonObject = list.getJSONObject(j);

                        Trips trips = new Trips();

                        trips.setmPostId(jsonObject.getString("pt_id"));
                        trips.setmPostName(jsonObject.getString("pt_name"));
                        trips.setmPostDetails(jsonObject.getString("pt_detail"));
                        trips.setmPostCharges(jsonObject.getString("pt_charges"));
                        trips.setmPostStatus(jsonObject.getString("pt_status"));
                        trips.setmDate(jsonObject.getString("pt_date"));
                        trips.setmFrom(jsonObject.getString("pt_start_loc"));
                        trips.setmTo(jsonObject.getString("pt_end_loc"));
                        trips.setmImage(jsonObject.getString("pt_photo"));

                        tripsArrayList.add(trips);

                    }
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    tripsFragment.mRecyclerView_list.setVisibility(View.VISIBLE);
                    tripsFragment.mTextViewData.setVisibility(View.GONE);

                    getScheduleTripsCallBack.doPostExecute(tripsArrayList);
                } else if (message.equals("Sorry, no delivery scheduled for you.")) {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }

                    tripsFragment.mRecyclerView_list.setVisibility(View.GONE);
                    tripsFragment.mTextViewData.setVisibility(View.VISIBLE);

                    tripsFragment.mTextViewData.setText(message);
                    
                }

            }
        } catch (JSONException je) {
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