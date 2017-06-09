package com.carryapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.carryapp.AsyncTasks.SendNotiAsyncTask;
import com.carryapp.Classes.PostDelivery;
import com.carryapp.Classes.Transport;
import com.carryapp.Fragments.TransportListFragment;
import com.carryapp.Holders.TransportListHolder;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.SessionData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by siddhi jambhale on 5/22/2017.
 */

public class TransportListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private Context context;
        private ArrayList<PostDelivery> list;
        private TransportListFragment transportListFragment;
        //static var
        static final int TYPE_LOAD_TRANSPORT = 0, TYPE_LOAD_PROGRESS = 1;
        boolean isLoading = false, isMoreDataAvailable = true;
        OnLoadMoreListener loadMoreListener;
        private int selectedPosition=-1;
        List<LinearLayout> items;
        private boolean clicked;
    private SessionData sessionData;

    public TransportListAdapter(Context context, ArrayList<PostDelivery> list,TransportListFragment transportListFragment) {
            this.context = context;
            this.list = list;
            this.transportListFragment = transportListFragment;
            items = new ArrayList<>();
            sessionData = new SessionData(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (viewType) {

                case TYPE_LOAD_TRANSPORT:
                    View v_order_header = inflater.inflate(R.layout.transport_item, parent, false);
                    viewHolder = new TransportListHolder(v_order_header);
                    break;

            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {

            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }

            if (getItemViewType(position) == TYPE_LOAD_TRANSPORT) {

                TransportListHolder transportListHolder = (TransportListHolder) holder;
                items.add(transportListHolder.lay_row);
                retriveAllList(transportListHolder, position);
            } else {
            }

        }

        @Override
        public int getItemViewType(int position) {

            Object obj = list.get(position);

            if (obj instanceof Transport) {
                return TYPE_LOAD_TRANSPORT;
            }/* else if (obj instanceof LoadProgress) {
                return TYPE_LOAD_PROGRESS;
            }*/

            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        public void retriveAllList(final TransportListHolder holder,final int position) {

            //show transport data

            final PostDelivery data = (PostDelivery) list.get(position);

            final String newDate = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy", data.getmPtDate());

        //  final String time = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "hh:mm a", data.getCreated_at());


            holder.tv_product.setText(data.getmPt_name());
            holder.tv_dateTime.setText(newDate);
            holder.tv_username.setText(String.valueOf(data.getmUserName()));

            String rating = data.getmRating();

            if(rating.equals("1"))
            {
                holder.imageViewR1.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1_empty.setVisibility(View.GONE);

            } else if(rating.equals("2")) {

                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1_empty.setVisibility(View.GONE);
                holder.imageViewR2_empty.setVisibility(View.GONE);

                holder.imageViewR1.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR2.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));

            }
            else if(rating.equals("3"))
            {

                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1_empty.setVisibility(View.GONE);
                holder.imageViewR2_empty.setVisibility(View.GONE);
                holder.imageViewR3_empty.setVisibility(View.GONE);

                holder.imageViewR1.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR2.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR3.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));

            }
            else if(rating.equals("4"))
            {

                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR1_empty.setVisibility(View.GONE);
                holder.imageViewR2_empty.setVisibility(View.GONE);
                holder.imageViewR3_empty.setVisibility(View.GONE);
                holder.imageViewR4_empty.setVisibility(View.GONE);

                holder.imageViewR1.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR2.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR3.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR4.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));

            }
            else if(rating.equals("5"))
            {

                holder.imageViewR1.setVisibility(View.VISIBLE);
                holder.imageViewR2.setVisibility(View.VISIBLE);
                holder.imageViewR3.setVisibility(View.VISIBLE);
                holder.imageViewR4.setVisibility(View.VISIBLE);
                holder.imageViewR5.setVisibility(View.VISIBLE);
                holder.imageViewR1_empty.setVisibility(View.GONE);
                holder.imageViewR2_empty.setVisibility(View.GONE);
                holder.imageViewR3_empty.setVisibility(View.GONE);
                holder.imageViewR4_empty.setVisibility(View.GONE);
                holder.imageViewR5_empty.setVisibility(View.GONE);

                holder.imageViewR1.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR2.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR3.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR4.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));
                holder.imageViewR5.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bird));

            }

                //show product pic

            String url = context.getString(R.string.photo_url) + data.getmPtPhoto();

            Picasso.with(context)
                    .load(url)
                    .resize(250,250)
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .into(holder.img_product);

        /*    if(selectedPosition==position) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGrey));
            }
            else {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }


*/

        final Animation bottomUp = AnimationUtils.loadAnimation(context,
                    R.anim.bottom_up);
        final Animation bottomDown = AnimationUtils.loadAnimation(context,
                    R.anim.bottom_down);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  /*  if(!data.isChecked) {
                        selectedPosition = position;
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGrey));
                        data.setChecked(true);
                        transportListFragment.mBtnRequest.startAnimation(bottomUp);
                        transportListFragment.mBtnRequest.setVisibility(View.VISIBLE);
                    }
                    else {
                        selectedPosition = position;
                        holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                        transportListFragment.mBtnRequest.startAnimation(bottomDown);
                        transportListFragment.mBtnRequest.setVisibility(View.GONE);
                        data.setChecked(false);
                    }*/

                      makeAllWhite();
                      data.setChecked(true);
                      holder.lay_row.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGrey));
                      transportListFragment.mBtnRequest.startAnimation(bottomUp);
                      transportListFragment.mBtnRequest.setVisibility(View.VISIBLE);

                }
            });

            for (int i=0;i< list.size();i++)
            {
                if(i==position) {
                    data.setChecked(true);
                }
                else {
                    data.setChecked(false);
                }
            }

            transportListFragment.mBtnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendNotiAsyncTask sendNotiAsyncTask = new SendNotiAsyncTask(context,transportListFragment.parentLayout);
                    sendNotiAsyncTask.execute(data.getmPt_id(),data.getmUserId(),sessionData.getString("api_key",""));
                    final Animation bottomDown = AnimationUtils.loadAnimation(context,
                            R.anim.bottom_down);
                    transportListFragment.mBtnRequest.startAnimation(bottomDown);
                    transportListFragment.mBtnRequest.setVisibility(View.GONE);
                }
            });

        }
    private void makeAllWhite() {
        for(LinearLayout item : items) {
    /*        final Animation bottomDown = AnimationUtils.loadAnimation(context,
                    R.anim.bottom_down);
            transportListFragment.mBtnRequest.startAnimation(bottomDown);
            transportListFragment.mBtnRequest.setVisibility(View.GONE);*/
            item.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }
        public void setMoreDataAvailable(boolean moreDataAvailable) {
            isMoreDataAvailable = moreDataAvailable;
        }


        public interface OnLoadMoreListener {
            void onLoadMore();
        }

        public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
            this.loadMoreListener = loadMoreListener;
        }

        public void notifyDataChanged() {
            notifyDataSetChanged();
            isLoading = false;
        }

}
