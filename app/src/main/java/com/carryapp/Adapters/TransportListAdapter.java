package com.carryapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carryapp.Classes.PostDelivery;
import com.carryapp.Classes.Transport;
import com.carryapp.Fragments.TransportListFragment;
import com.carryapp.Holders.TransportListHolder;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

        public TransportListAdapter(Context context, ArrayList<PostDelivery> list,TransportListFragment transportListFragment) {
            this.context = context;
            this.list = list;
            this.transportListFragment = transportListFragment;
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true;
                loadMoreListener.onLoadMore();
            }

            if (getItemViewType(position) == TYPE_LOAD_TRANSPORT) {
                TransportListHolder transportListHolder = (TransportListHolder) holder;
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


        public void retriveAllList(final TransportListHolder holder, int position) {

            //show transport data

            final PostDelivery data = (PostDelivery) list.get(position);

            final String newDate = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy", data.getmPtDate());

        //  final String time = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "hh:mm a", data.getCreated_at());


            holder.tv_product.setText(data.getmPt_name());
            holder.tv_dateTime.setText(newDate);
            holder.tv_username.setText(String.valueOf(data.getmUserName()));

            //show product pic

            String url = context.getString(R.string.photo_url) + data.getmPtPhoto();

            Picasso.with(context)
                    .load(url)
                    .resize(250,250)
                    .placeholder(R.drawable.product)
                    .error(R.drawable.product)
                    .into(holder.img_product);


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

        public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
            this.loadMoreListener = loadMoreListener;
        }

        public void notifyDataChanged() {
            notifyDataSetChanged();
            isLoading = false;
        }

}
