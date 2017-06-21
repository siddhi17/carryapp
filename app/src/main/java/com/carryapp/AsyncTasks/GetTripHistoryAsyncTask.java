package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carryapp.Classes.TravelHistory;
import com.carryapp.Classes.Trips;
import com.carryapp.Fragments.ScheduledTravelFragment;
import com.carryapp.Fragments.TravelHistoryFragment;
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/21/2017.
 */

public class GetTripHistoryAsyncTask  extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private RelativeLayout parentLayout;
    private JSONArray list;
    private ArrayList<TravelHistory> tripsArrayList;
    private GetTripsHistoryCallBack getTripsHistoryCallBack;
    private TravelHistoryFragment travelHistoryFragment;

    public GetTripHistoryAsyncTask(Context context,RelativeLayout parentLayout, TravelHistoryFragment travelHistoryFragment, GetTripsHistoryCallBack getTripsHistoryCallBack) {

        this.mContext = context;
        this.parentLayout = parentLayout;
        this.travelHistoryFragment = travelHistoryFragment;
        this.getTripsHistoryCallBack = getTripsHistoryCallBack;

    }

    public interface GetTripsHistoryCallBack {
        void doPostExecute(ArrayList<TravelHistory> trips);
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
            api = mContext.getResources().getString(R.string.url) + "triphistory";

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
                    list = response.getJSONArray("posts");

                    tripsArrayList = new ArrayList<>();

                    for (int j = 0; j < list.length(); j++) {

                        JSONObject jsonObject = list.getJSONObject(j);

                        TravelHistory travelHistory = new TravelHistory();

                      /*  travelHistory.setmPostId(jsonObject.getString("pt_id"));
                        travelHistory.setmPostName(jsonObject.getString("pt_name"));
                        travelHistory.setmPostDetails(jsonObject.getString("pt_detail"));
                        travelHistory.setmPostCharges(jsonObject.getString("pt_charges"));
                        travelHistory.setmPostStatus(jsonObject.getString("pt_status"));*/
                        travelHistory.setmDate(jsonObject.getString("pt_date"));
                        travelHistory.setmFrom(jsonObject.getString("pt_start_loc"));
                        travelHistory.setmTo(jsonObject.getString("pt_end_loc"));
                        travelHistory.setmImage(jsonObject.getString("pt_photo"));

                        tripsArrayList.add(travelHistory);

                    }
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    travelHistoryFragment.mRecyclerView_list.setVisibility(View.VISIBLE);
                    travelHistoryFragment.mTextViewData.setVisibility(View.GONE);

                    getTripsHistoryCallBack.doPostExecute(tripsArrayList);

                } else if (message.equals("Sorry, try again later")) {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }

                    travelHistoryFragment.mRecyclerView_list.setVisibility(View.GONE);
                    travelHistoryFragment.mTextViewData.setVisibility(View.VISIBLE);

                } else if (message.equals("Access Denied. Invalid Api key")) {
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                    snackbar = Snackbar.make(parentLayout, R.string.warning, Snackbar.LENGTH_LONG);
                    snackbar.show();

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