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
        if (loadingDialog.isShowing())
            loadingDialog.dismiss();
        try {
            list = new ArrayList<>();


                    int result = response.getInt("result");
                    String message = response.getString("message");
                    if (result == 1) {
                        //  Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        //code after getting profile details goes here

                        listsArray = response.getJSONArray("cost");

                        for (int j = 0; j < listsArray.length(); j++) {

                            jsonObject = listsArray.getJSONObject(j);

                /*        SessionData session = new SessionData(mContext);
                        session.add("ur_id", jsonObject.getString("ur_id"));
                        session.add("ur_name", jsonObject.getString("ur_name"));
                        session.add("ur_email", jsonObject.getString("ur_email"));
                        session.add("ur_mob_no", jsonObject.getString("ur_mob_no"));
                        session.add("ur_device_id", jsonObject.getString("ur_device_id"));
                        session.add("ur_photo", jsonObject.getString("ur_photo"));
                        session.add("api_key", jsonObject.getString("api_key"));
                        session.add("ur_car_model", jsonObject.getString("ur_car_model"));
                        session.add("ur_car_type", jsonObject.getString("ur_car_type"));
                        session.add("ur_car_photo", jsonObject.getString("ur_car_photo"));
                        session.add("ur_dni_photo", jsonObject.getString("ur_dni_photo"));
                        session.add("ur_birth_date", jsonObject.getString("ur_birth_date"));*/



                    PostDelivery postDelivery = new PostDelivery();

                    postDelivery.setmPt_id(jsonObject.getString("pt_id"));
                    postDelivery.setmPt_name(jsonObject.getString("pt_name"));
                    postDelivery.setmPtDetail(jsonObject.getString("pt_detail"));
                    postDelivery.setmPtStartLoc(jsonObject.getString("pt_start_loc"));
                    postDelivery.setmPtEndLoc(jsonObject.getString("pt_end_loc"));
                    postDelivery.setmPtDate(jsonObject.getString("pt_date"));


                    list.add(postDelivery);

                    searchPostsCallBack.doPostExecute(list);

                }
            }

                snackbar = Snackbar.make(parentLayout, "sorry", Snackbar.LENGTH_LONG);

                snackbar.show();


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
