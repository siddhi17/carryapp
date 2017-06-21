package com.carryapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carryapp.Classes.Transport;
import com.carryapp.Classes.TravelHistory;
import com.carryapp.Classes.Trips;
import com.carryapp.Fragments.ScheduledTravelFragment;
import com.carryapp.Fragments.TravelHistoryFragment;
import com.carryapp.Holders.TripsHolder;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 5/27/2017.
 */

public class TravelHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        //static var
        private Context context;
        private ArrayList<TravelHistory> list;
        private TravelHistoryFragment travelHistoryFragment;
        //static var
        static final int TYPE_LOAD_TRIP = 0, TYPE_LOAD_PROGRESS = 1;
        boolean isLoading = false, isMoreDataAvailable = true;
        TransportListAdapter.OnLoadMoreListener loadMoreListener;

        public TravelHistoryAdapter(Context context, ArrayList<TravelHistory> list, TravelHistoryFragment travelHistoryFragment) {
            this.context = context;
            this.list = list;
            this.travelHistoryFragment = travelHistoryFragment;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (viewType) {

                case TYPE_LOAD_TRIP:
                    View v_order_header = inflater.inflate(R.layout.trip_layout, parent, false);
                    viewHolder = new TripsHolder(v_order_header);
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

            if (getItemViewType(position) == TYPE_LOAD_TRIP) {
                TripsHolder tripsHolder = (TripsHolder) holder;
                retriveAllTrips(tripsHolder, position);
            } else {
            }

        }

        @Override
        public int getItemViewType(int position) {

            Object obj = list.get(position);

            if (obj instanceof Transport) {
                return TYPE_LOAD_TRIP;
            }/* else if (obj instanceof LoadProgress) {
                return TYPE_LOAD_PROGRESS;
            }*/

            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        public void retriveAllTrips(final TripsHolder holder, int position) {

            //show trips data

            final TravelHistory data = (TravelHistory) list.get(position);

            final String newDate = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy", data.getmDate());


            holder.tv_date.setText(newDate);
            holder.tv_from.setText(data.getmFrom());
            holder.tv_to.setText(data.getmTo());

/*
            String url = context.getString(R.string.photo_url) + data.getmImage();
            Log.e("url",url);

            Picasso.with(context)
                    .load(url)
                    .resize(400,400)
                    .placeholder(R.drawable.car_shipping)
                    .error(R.drawable.car_shipping)
                    .into(holder.carImageView);
*/


            //go to order detail fragment

          /*  holder.lay_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putString("name", data.getMerchant_name());
                    bundle.putString("date", newDate);
                    bundle.putString("time", time);
                    bundle.putLong("quantity", data.getItem_quantity());
                    bundle.putInt("status", data.getStatus());
                    bundle.putLong("orderID", data.getId());
                    bundle.putLong("amount", data.getTotalAmount());
                    bundle.putString("avatar", data.getAvatar());
                    bundle.putString("receipt", data.getReceipt());
                    //go to home screen
                    FragmentManager fragmentManager = ordersFragment.getFragmentManager();
                    OrderDetailFragment fragment = new OrderDetailFragment();
                    fragment.setArguments(bundle);

                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment, "RETRIEVE_ORDERS_ITEMS_FRAGMENT").addToBackStack("A").commit();

                }
            });*/

        }

        public void setMoreDataAvailable(boolean moreDataAvailable) {
            isMoreDataAvailable = moreDataAvailable;
        }


        public interface OnLoadMoreListener {
            void onLoadMore();
        }

        public void setLoadMoreListener(TransportListAdapter.OnLoadMoreListener loadMoreListener) {
            this.loadMoreListener = loadMoreListener;
        }

        public void notifyDataChanged() {
            notifyDataSetChanged();
            isLoading = false;
        }
}
