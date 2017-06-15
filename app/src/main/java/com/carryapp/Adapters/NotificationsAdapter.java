package com.carryapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carryapp.Classes.Notifications;
import com.carryapp.Fragments.Notices;
import com.carryapp.Holders.NotificationsHolder;
import com.carryapp.R;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 6/12/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //static var
    private Context context;
    private ArrayList<Notifications> list;
    private Notices notices;
    //static var
    static final int TYPE_LOAD_NOTI = 0, TYPE_LOAD_PROGRESS = 1;
    boolean isLoading = false, isMoreDataAvailable = true;
    TransportListAdapter.OnLoadMoreListener loadMoreListener;

    public NotificationsAdapter(Context context, ArrayList<Notifications> list,Notices notices) {
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

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if (getItemViewType(position) == TYPE_LOAD_NOTI) {

            NotificationsHolder notificationsHolder = (NotificationsHolder) holder;
            retriveAllNotifications(notificationsHolder, position);
        } else {
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

    }

}
