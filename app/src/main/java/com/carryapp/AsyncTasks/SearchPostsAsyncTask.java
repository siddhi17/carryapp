package com.carryapp.AsyncTasks;

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
import com.carryapp.R;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/4/2017.
 */

public class SearchPostsAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private SearchPostsCallBack searchPostsCallBack;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private LinearLayout parentLayout;
    private ArrayList<PostDelivery> list;
    private JSONArray listsArray;
    private JSONObject jsonObject;

    public SearchPostsAsyncTask(Context context, LinearLayout linearLayout,SearchPostsCallBack searchPostsCallBack) {

        this.mContext = context;
        this.searchPostsCallBack = searchPostsCallBack;
        this.parentLayout = linearLayout;

    }

    public interface SearchPostsCallBack {
        void doPostExecute(ArrayList<PostDelivery> list);
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
            api = mContext.getResources().getString(R.string.url) + "requestlist";

            jsonParams = new JSONObject();
            jsonParams.put("st_lati", params[0]);
            jsonParams.put("st_longi", params[1]);
            jsonParams.put("ed_lati", params[2]);
            jsonParams.put("ed_longi", params[3]);
            jsonParams.put("pt_date", params[4]);

            ServerRequest request = new ServerRequest(api, jsonParams);
            return request.sendPostRequest(params[5]);

        } catch (JSONException je) {
            Log.e("exception",je.toString());
            return Excpetion2JSON.getJSON(je);
        } catch (Exception ue) {
            return Excpetion2JSON.getJSON(ue);
        }
    }  //end of doInBackground

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);

        try {
            list = new ArrayList<>();

                    int result = response.getInt("status");
                    String message = response.getString("message");



                    if (result == 1) {

                        listsArray = response.getJSONArray("cost");

                        for (int j = 0; j < listsArray.length(); j++) {

                            jsonObject = listsArray.getJSONObject(j);


                    PostDelivery postDelivery = new PostDelivery();

                    postDelivery.setmPt_id(jsonObject.getString("pt_id"));
                    postDelivery.setmPt_name(jsonObject.getString("pt_name"));
                    postDelivery.setmPtDetail(jsonObject.getString("pt_detail"));
                    postDelivery.setmPtStartLoc(jsonObject.getString("pt_start_loc"));
                    postDelivery.setmPtEndLoc(jsonObject.getString("pt_end_loc"));
                    postDelivery.setmPtDate(jsonObject.getString("pt_date"));
                            postDelivery.setmUserName(jsonObject.getString("ur_name"));
                            postDelivery.setmPtPhoto(jsonObject.getString("pt_photo"));
                            postDelivery.setmRating(jsonObject.getString("ur_rating"));
                            postDelivery.setmUserId(jsonObject.getString("ur_id"));


                    list.add(postDelivery);

                    searchPostsCallBack.doPostExecute(list);

                }
                        if (loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
            }

            else if(message.equals("Sorry, try again later."))
            {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

                snackbar = Snackbar.make(parentLayout, R.string.noDelivery, Snackbar.LENGTH_LONG);
                snackbar.show();
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
