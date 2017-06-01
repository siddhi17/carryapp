package com.carryapp.AsyncTasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by siddhi jambhale on 6/1/2017.
 */

public class LoginAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private CoordinatorLayout parentLayout;

    public LoginAsyncTask(Context context, CoordinatorLayout linearLayout) {

        this.mContext = context;
        this.parentLayout = linearLayout;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!isOnline()) {
           // showAlert(getString(R.string.check_network));

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
            api = mContext.getResources().getString(R.string.url) + "signin";

            jsonParams = new JSONObject();

            jsonParams.put("email", params[0]);
            jsonParams.put("password", params[1]);
            jsonParams.put("deviceid", params[2]);

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
                        if (message.equals("Login failed. Incorrect credentials")) {

                            snackbar = Snackbar.make(parentLayout,R.string.alertEmail, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                        else if(message.equals("This Email ID is not valid !"))
                        {

                            snackbar = Snackbar.make(parentLayout,R.string.emailAlert, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } else if (message.equals("Required field Password is missing or empty !")) {

                            snackbar = Snackbar.make(parentLayout,message, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                        else if(message.equals("Login failed. Blank password"))
                        {

                            snackbar = Snackbar.make(parentLayout,message, Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }

                    } else {


                        SessionData session = new SessionData(mContext);
                        session.add("ur_id", jsonObject.getString("ur_id"));
                        session.add("ur_name", jsonObject.getString("ur_name"));
                        session.add("ur_email", jsonObject.getString("ur_email"));
                        session.add("ur_mob_no", jsonObject.getString("ur_mob_no"));
                        session.add("ur_device_id", jsonObject.getString("ur_device_id"));
                        session.add("ur_photo", jsonObject.getString("ur_photo"));
                        session.add("api_key", jsonObject.getString("api_key"));


                        //if user is active, go to home activity

                            //  Snackbar snackbar = Snackbar.make(parentLayout,getString(R.string.welcome), Snackbar.LENGTH_LONG);


                         /*   final Dialog dialog = new Dialog(LoginActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.layout_dialogue_alert);
                            dialog.setCancelable(true);
                            dialog.show();
                            Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                            tv_title.setText("Welcome");
                            tv_msg.setText(getResources().getString(R.string.welcome));
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                    finish();*/

                                    Intent intent = new Intent(mContext, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent);


                                //    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                   /*             }
                            });

                            dialog.show();*/


                              /*  snackbar.setCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        super.onDismissed(snackbar, event);

                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                     //   intent.putExtra("phone_no", jsonObject.getString("phone_no"));
                                       // intent.putExtra("email_id", jsonObject.getString("email_id"));
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onShown(Snackbar snackbar) {
                                        super.onShown(snackbar);
                                    }
                                });

                                snackbar.show();*/



                            //if user is  not active, generate OTP

                           /* Intent intent = new Intent(LoginActivity.this, OtpConfirmation.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("email_id", jsonObject.getString("email_id"));
                            intent.putExtra("phone_no", jsonObject.getString("phone_no"));
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/

                    }
                }

            }
        } catch (JSONException je) {

            je.printStackTrace();
            //  showAlert("Please Enter correct Email Id and Password.");

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

