package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.carryapp.Classes.Notifications;
import com.carryapp.Database.NotiTableHelper;
import com.carryapp.R;

/**
 * Created by siddhi jambhale on 6/23/2017.
 */

public class AddNotiAsyncTask  extends AsyncTask<String, String, Notifications> {

    private Context mContext;
    private NotiTableHelper dbConnector;
    private ProgressDialog progressDialog;

    public AddNotiAsyncTask(Context context) {
        this.mContext = context;
        dbConnector = new NotiTableHelper(context);

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(mContext.getString(R.string.wait));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        //   progressDialog.show();

    }

    @Override
    public Notifications doInBackground(String... params) {

        //add item to local database

        Notifications notifications = new Notifications();

        notifications.setNt_id(params[0]);
        notifications.setNt_message(params[1]);
        notifications.setPt_id(params[2]);
        notifications.setSender_id(params[3]);
        notifications.setNt_status(params[4]);

        dbConnector.addNoti(notifications);

        return notifications;
    }

    @Override
    public void onPostExecute(Notifications b) {
        if (b != null) {
            // set the adapter's Cursor

            dbConnector.close();
            //   progressDialog.dismiss();
        }
    }

}
