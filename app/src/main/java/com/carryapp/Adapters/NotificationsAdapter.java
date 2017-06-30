package com.carryapp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.carryapp.Activities.PaymentActivity;
import com.carryapp.AsyncTasks.NotificationResponseAsyncTask;
import com.carryapp.Classes.Notifications;
import com.carryapp.Fragments.NoticesFragment;
import com.carryapp.Holders.NotificationsHolder;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.SessionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by siddhi jambhale on 6/12/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements NotificationResponseAsyncTask.NotiResponseCalBack{

    //static var
    private Context context;
    private ArrayList<Notifications> list;
    private NoticesFragment notices;
    //static var
    static final int TYPE_LOAD_NOTI = 0, TYPE_LOAD_PROGRESS = 1;
    boolean isLoading = false, isMoreDataAvailable = true;

    private SessionData sessionData;

    public NotificationsAdapter(Context context, ArrayList<Notifications> list,NoticesFragment notices) {
        this.context = context;
        this.list = list;
        this.notices = notices;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {

            case TYPE_LOAD_NOTI:
                View v_order_header = inflater.inflate(R.layout.notification_item, parent, false);
                viewHolder = new NotificationsHolder(v_order_header);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        sessionData = new SessionData(context);
        if (getItemViewType(position) == TYPE_LOAD_NOTI) {

            NotificationsHolder notificationsHolder = (NotificationsHolder) holder;
            retriveAllNotifications(notificationsHolder, position);
        }

        else {
        }

    }

    @Override
    public int getItemViewType(int position) {

        Object obj = list.get(position);

        if (obj instanceof Notifications) {
            return TYPE_LOAD_NOTI;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void retriveAllNotifications(final NotificationsHolder holder, int position) {

        //show notifications data

        final Notifications data = (Notifications) list.get(position);

        holder.textViewMessage.setText(data.getNt_message());




        if(data.getNt_status().equals("1"))
        {
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);

            holder.textViewStatus.setText(context.getString(R.string.requestAccepted));
            holder.textViewStatus.setTextColor(ContextCompat.getColor(context,R.color.colorButton));
            holder.textViewStatus.setVisibility(View.VISIBLE);

        }
        else if(data.getNt_status().equals("2"))
        {
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);

            holder.textViewStatus.setText(context.getString(R.string.requestRejected));
            holder.textViewStatus.setTextColor(Color.RED);
            holder.textViewStatus.setVisibility(View.VISIBLE);

        }
        else {

            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.textViewStatus.setVisibility(View.GONE);
        }


        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationResponseAsyncTask notificationResponseAsyncTask = new NotificationResponseAsyncTask(context,notices,NotificationsAdapter.this);
                notificationResponseAsyncTask.execute(data.getPt_id(),data.getSender_id(),"1",sessionData.getString("api_key",""),data.getNt_id());

            }
        });


        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationResponseAsyncTask notificationResponseAsyncTask = new NotificationResponseAsyncTask(context,notices,NotificationsAdapter.this);
                notificationResponseAsyncTask.execute(data.getPt_id(),data.getSender_id(),"2",sessionData.getString("api_key",""),data.getNt_id());

            }
        });
    }

    @Override
    public void doPostExecute(JSONObject response) {
        try {

            if (response.has("message")) {
                String message = response.getString("message");

                if (message.equals("Success")) {

                    Log.e("aaaaaaaaaaaaaaaa",""+response.toString());

                   /* {"status":1,"message":"Success","acceptance":"1",
                    "payment":[{"ur_name":"Siddhi Jambhale","pt_date":"2017-06-30 17:48:00",
                            "pt_start_loc":"Nashik","pt_end_loc":"Pune","pt_charges":"6.33"}]}*/

                    String ur_name="",pt_date,date="",time="",pt_start_loc="",pt_end_loc="",pt_charges="";

                    JSONArray jsonArray=response.getJSONArray("payment");

                    if (jsonArray.length()>0){

                        for (int i=0; i<jsonArray.length();i++){

                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            ur_name=jsonObject.getString("ur_name");
                            pt_start_loc=jsonObject.getString("pt_start_loc");
                            pt_end_loc=jsonObject.getString("pt_end_loc");
                            pt_charges=jsonObject.getString("pt_charges");
                            pt_date=jsonObject.getString("pt_date");
                            date = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy", pt_date);
                            time = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "HH:mm", pt_date);

                        }
                    }


                    showdialouge(ur_name,pt_start_loc,pt_end_loc,pt_charges,date,time);

                }
            }

        } catch (JSONException je) {

            je.printStackTrace();

        }
    }


    /**
     * dialog to select message from template
     */
    public  void  showdialouge(final String ur_name, final String pt_start_loc, final String pt_end_loc,
                               final String pt_charges, final String date, final String time){
        //create dialogue box
        final Dialog dialog = new Dialog(notices.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//we have provided custom title
        dialog.setContentView(R.layout.layout_dialogue_pay);//set custom layout
        dialog.setCancelable(true);
        dialog.show();
        Button btn_cancel= (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_pay= (Button) dialog.findViewById(R.id.btn_pay);
        TextView tv_del_man= (TextView) dialog.findViewById(R.id.tv_del_man);
        TextView tv_date= (TextView) dialog.findViewById(R.id.tv_date);
        TextView tv_time= (TextView) dialog.findViewById(R.id.tv_time);
        TextView tv_from= (TextView) dialog.findViewById(R.id.tv_from);
        TextView tv_to= (TextView) dialog.findViewById(R.id.tv_to);
        TextView tv_charge= (TextView) dialog.findViewById(R.id.tv_charge);

        tv_del_man.setText("Delivery man : "+ur_name);
        tv_date.setText("Date : "+date);
        tv_time.setText("Time : "+time);
        tv_from.setText("From : "+pt_start_loc);
        tv_to.setText("To : "+pt_end_loc);
        tv_charge.setText("Amount : "+pt_charges);


        //dismiss dialog
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(notices.getActivity(), PaymentActivity.class);
                intent.putExtra("ur_name",ur_name);
                intent.putExtra("pt_start_loc",pt_start_loc);
                intent.putExtra("pt_end_loc",pt_end_loc);
                intent.putExtra("pt_charges",pt_charges);
                intent.putExtra("date",date);
                intent.putExtra("time",time);
                notices.startActivity(intent);
            }
        });



        dialog.show();
    }
}
