package com.carryapp.AsyncTasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Activities.LoginActivity;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.Excpetion2JSON;
import com.carryapp.helper.ServerRequest;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by siddhi jambhale on 5/31/2017.
 */

public class RegisterAsyncTask extends AsyncTask<String, Void, JSONObject> {
    String api;
    JSONObject jsonParams;
    Context mContext;
    RegisterCallBack registerCallBack;
    private ProgressDialog loadingDialog;
    private Snackbar snackbar;
    private LinearLayout parentLayout;

    public RegisterAsyncTask(Context context, RegisterCallBack  registerCallBack, LinearLayout linearLayout) {

        this.mContext = context;
        this.registerCallBack = registerCallBack;
        this.parentLayout = linearLayout;

    }

    public interface RegisterCallBack {
        void doPostExecute(ArrayList<String> register);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!isOnline()) {
            //   showAlert(getString(R.string.check_network));
         /*   CommonUtils.showAlert(RegisterCustomerActivity.this, getResources().getString(R.string.check_network), "Check Network");*/
            snackbar = Snackbar.make(parentLayout,R.string.check_network, Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            loadingDialog = ProgressDialog.show(mContext, null,mContext.getString(R.string.wait));
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            api = mContext.getResources().getString(R.string.url) + "signup";

            jsonParams = new JSONObject();
            jsonParams.put("username", params[0]);
            jsonParams.put("email", params[1]);
            jsonParams.put("mobile", params[2]);
            jsonParams.put("password", params[3]);
            jsonParams.put("latitude", params[4]);
            jsonParams.put("longitude", params[5]);
            jsonParams.put("deviceid", params[6]);
            jsonParams.put("photo", params[7]);


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
                        //    showAlert(message);
                    /*    CommonUtils.showAlert(RegisterCustomerActivity.this, message, "Registration");*/

                        if (message.equals("Sorry, this email already existed"))
                        {
                            snackbar = Snackbar.make(parentLayout, R.string.mailExists, Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    else {
                            snackbar = Snackbar.make(parentLayout,R.string.registered, Snackbar.LENGTH_LONG);

                            snackbar.show();
                        }

                    } else {

                        //on successful registration go to sign in

                        SessionData session = new SessionData(mContext);
                        session.add("ur_id", jsonObject.getString("ur_id"));
                        session.add("ur_name", jsonObject.getString("ur_name"));
                        session.add("ur_email", jsonObject.getString("ur_email"));
                        session.add("ur_mob_no", jsonObject.getString("ur_mob_no"));
                        session.add("ur_device_id", jsonObject.getString("ur_device_id"));
                        session.add("ur_photo", jsonObject.getString("ur_photo"));
                        session.add("api_key", jsonObject.getString("api_key"));


                       /* final Dialog dialog = new Dialog(mContext);
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
                                try {*/


                                    Intent intent = new Intent(mContext, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent);


                              /*  } catch (JSONException e) {

                                }
                            }
                        });
*/
                      //  dialog.show();

                        //   Snackbar snackbar = Snackbar.make(parentLayout,R.string.registered, Snackbar.LENGTH_LONG);

                         /*   snackbar.setCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    super.onDismissed(snackbar, event);

                                    try {

                                        finish();
                                        Intent intent = new Intent(RegisterCustomerActivity.this, OtpConfirmation.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("phone_no", jsonObject.getString("phone_no"));
                                        intent.putExtra("email_id", jsonObject.getString("email_id"));
                                        startActivity(intent);
                                    }
                                    catch (JSONException e)
                                    {

                                    }
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                    super.onShown(snackbar);
                                }
                            });

                            snackbar.show();*/
                    }
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