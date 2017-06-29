package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.carryapp.Classes.Notifications;
import com.carryapp.Database.NotiTableHelper;
import com.carryapp.Fragments.NoticesFragment;
import com.carryapp.R;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/22/2017.
 */

public class GetLocalNotificationsAsyncTask extends AsyncTask<Void, Void, ArrayList<Notifications>> {

    private Context mContext;
    private NotiTableHelper dbConnector;
    private GetLocalNotificationsCallBack getLocalNotificationsCallBack;
    private ProgressDialog progressDialog;
    private NoticesFragment noticesFragment;

    public GetLocalNotificationsAsyncTask(Context context,NoticesFragment noticesFragment, GetLocalNotificationsCallBack getLocalNotificationsCallBack) {

        this.mContext = context;
        this.getLocalNotificationsCallBack = getLocalNotificationsCallBack;
        dbConnector = new NotiTableHelper(context);
        this.noticesFragment = noticesFragment;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage(mContext.getString(R.string.wait));
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public interface GetLocalNotificationsCallBack {
        void doPostExecute(ArrayList<Notifications> noti, Boolean b);
    }

    @Override
    public ArrayList<Notifications> doInBackground(Void... params) {


        //get all notifications from local database

        ArrayList<Notifications> notifications = new ArrayList<>();

        notifications = dbConnector.getAllNoti();

        return notifications;
    }

    @Override
    public void onPostExecute(ArrayList<Notifications> b) {
        if (b != null) {
            // set the adapter's Cursor

            getLocalNotificationsCallBack.doPostExecute(b,true);

            dbConnector.close();
            progressDialog.dismiss();
        }
    }
}
