package com.carryapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.R;
import com.carryapp.helper.ServerRequest;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by siddhi jambhale on 6/3/2017.
 */

public class UploadFileAsyncTask extends AsyncTask<String, String, String> {
    private ProgressDialog progressDialog;
    File file = null;
    String getfile;
    private Context mContext;
    private UploadFileCallBack uploadFileCallBack;
    private Snackbar snackbar;
    private FrameLayout parentLayout;

    public UploadFileAsyncTask(Context context,UploadFileCallBack uploadFileCallBack,FrameLayout parentLayout)
    {
        this.mContext = context;
        this.uploadFileCallBack = uploadFileCallBack;
        this.parentLayout = parentLayout;
    }

    public interface UploadFileCallBack {
        void doPostExecute(String file);
    }

    protected void onPreExecute() {

        if (!isOnline()) {

            snackbar = Snackbar.make(parentLayout, R.string.check_network, Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        else {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(mContext.getResources().getString(R.string.wait));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

    }

    @Override
    protected String doInBackground(String... params) {

        String api = mContext.getResources().getString(R.string.url) + "upload";
        try {

            // using MultipartEntity to upload file
            JSONObject object = new JSONObject();
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            file = new File(params[1]);
            entity.addPart("image", new FileBody(file));
            ServerRequest request = new ServerRequest(api,object);
            return request.sendFileRequest1(params[0],entity);

        } catch (Exception e) {

             Log.e(e.getClass().getName(), e.getMessage(), e);

            return null;
        }

    }


    @Override
    protected void onPostExecute(String sResponse) {
        progressDialog.dismiss();
        if(sResponse == null) {
            //Log.e(Application.TAG,"sResponsenull"+sResponse);
            return;
        }
        try {
            //Log.e(Application.TAG,"sResponse"+sResponse);

            getfile=sResponse;
            String uploadedFile = getfile.replace('"',' ');
            uploadFileCallBack.doPostExecute(uploadedFile.replace(" ",""));

        }catch (Exception e){

            Log.e("Exception",String.valueOf(e));
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
