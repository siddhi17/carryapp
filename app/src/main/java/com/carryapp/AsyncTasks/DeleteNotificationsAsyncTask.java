package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.carryapp.Classes.Notifications;
import com.carryapp.Database.NotiTableHelper;
import com.carryapp.R;

/**
 * Created by siddhi jambhale on 6/30/2017.
 */

public class DeleteNotificationsAsyncTask extends AsyncTask<String, String,Boolean> {

    private Context mContext;
    private NotiTableHelper dbConnector;
    private ProgressDialog progressDialog;

    public DeleteNotificationsAsyncTask(Context context) {
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
    public Boolean doInBackground(String... params) {

        //add item to local database

        dbConnector.deleteAllNoti();

        return true;
    }

    @Override
    public void onPostExecute(Boolean b) {
        if (b != null) {
            // set the adapter's Cursor

            dbConnector.close();
            //   progressDialog.dismiss();
        }
    }

}