package com.carryapp.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carryapp.Classes.Transport;
import com.carryapp.Classes.Trips;
import com.carryapp.Fragments.PostDetailsFragment;
import com.carryapp.Fragments.ScheduledTravelFragment;
import com.carryapp.Fragments.TransportListFragment;
import com.carryapp.Holders.TransportListHolder;
import com.carryapp.Holders.TripsHolder;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by siddhi jambhale on 5/27/2017.
 */

public class ScheduledTravelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //static var
    private Context context;
    private ArrayList<Trips> list;
    private ScheduledTravelFragment tripListFragment;
    //static var
    static final int TYPE_LOAD_TRIP = 0, TYPE_LOAD_PROGRESS = 1;
    boolean isLoading = false, isMoreDataAvailable = true;
    TransportListAdapter.OnLoadMoreListener loadMoreListener;

    public ScheduledTravelAdapter(Context context, ArrayList<Trips> list,ScheduledTravelFragment tripListFragment) {
        this.context = context;
        this.list = list;
        this.tripListFragment = tripListFragment;
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

        //show orders data

        final Trips data = (Trips) list.get(position);

        final String newDate = CommonUtils.formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy", data.getmDate());


        holder.tv_date.setText(newDate);
        holder.tv_from.setText(data.getmFrom());
        holder.tv_to.setText(data.getmTo());

   /*     String url = context.getString(R.string.photo_url) + data.getmImage();
        Log.e("url",url);

        Picasso.with(context)
                .load(url)
                .resize(400,400)
                .placeholder(R.drawable.car_shipping)
                .error(R.drawable.car_shipping)
                .into(holder.carImageView);
*/
        //go to order detail fragment

            holder.lay_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putString("pt_id", data.getmPostId());
                    bundle.putString("pt_name", data.getmPostName());
                    bundle.putString("pt_details", data.getmPostDetails());
                    bundle.putString("pt_photo", data.getmImage());
                    bundle.putString("pt_date", data.getmDate());
                    bundle.putString("from", data.getmFrom());
                    bundle.putString("to", data.getmTo());
                    bundle.putString("pt_charges", data.getmPostCharges());
                    bundle.putString("pt_size", data.getmSize());
                    bundle.putDouble("st_lati", data.getStartLati());
                    bundle.putDouble("st_longi", data.getStartLongi());
                    bundle.putDouble("ed_lati", data.getEndLati());
                    bundle.putDouble("ed_longi", data.getEndLongi());

                    //go to post details screen
                    FragmentManager fragmentManager = tripListFragment.getFragmentManager();
                    PostDetailsFragment fragment = new PostDetailsFragment();
                    fragment.setArguments(bundle);
                   // fragmentManager.beginTransaction().add(R.id.mycontainer, fragment, "POST_DETAILS_FRAGMENT").addToBackStack("O").commit();
                    fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment, "POST_DETAILS_FRAGMENT").addToBackStack("O").commit();

                }
            });

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
