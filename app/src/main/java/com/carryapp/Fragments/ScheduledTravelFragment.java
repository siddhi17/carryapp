package com.carryapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Adapters.ScheduledTravelAdapter;
import com.carryapp.Adapters.TransportListAdapter;
import com.carryapp.AsyncTasks.GetScheduledTripsAsyncTask;
import com.carryapp.Classes.PostDelivery;
import com.carryapp.Classes.Transport;
import com.carryapp.Classes.Trips;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.SessionData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduledTravelFragment extends Fragment implements GetScheduledTripsAsyncTask.GetScheduleTripsCallBack{

    public RecyclerView mRecyclerView_list;
    private ArrayList<Trips> mTripsList;
    private ScheduledTravelAdapter mTripsAdapter;
    public TextView mTextViewData;
    private String mDateTime;
    private SessionData sessionData;

    private RelativeLayout parentLayout;

    public ScheduledTravelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scheduled_travel, container, false);


        setUpUI(view);


        return view;
    }
    @Override
    public void onStart()
    {
        super.onStart();

        Log.e("onStart","onstart");

        GetScheduledTripsAsyncTask getScheduledTripsAsyncTask = new GetScheduledTripsAsyncTask(getActivity(),parentLayout,ScheduledTravelFragment.this,ScheduledTravelFragment.this);
        getScheduledTripsAsyncTask.execute(mDateTime,sessionData.getString("api_key",""));
    }

    @Override
    public void onPause()
    {
        super.onPause();

    }


    public void setUpUI(View view)
    {

        mRecyclerView_list = (RecyclerView) view.findViewById(R.id.rv_tripList);
        mTextViewData = (TextView) view.findViewById(R.id.textViewData);
        parentLayout = (RelativeLayout) view.findViewById(R.id.parentPanel);

        mTripsList = new ArrayList<Trips>();
        mTripsAdapter = new ScheduledTravelAdapter(getActivity(),mTripsList, ScheduledTravelFragment.this);
        mRecyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView_list.setAdapter(mTripsAdapter);
        mRecyclerView_list.setHasFixedSize(true);
        mRecyclerView_list.setItemViewCacheSize(50);
        mRecyclerView_list.setDrawingCacheEnabled(true);
        mRecyclerView_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        sessionData = new SessionData(getActivity());
        mDateTime = String.valueOf(CommonUtils.getCurrentDateTime());

    }

    @Override
    public void doPostExecute(ArrayList<Trips> list)
    {
        mTripsList.clear();

        mTripsList.addAll(list);

        mTripsAdapter.notifyDataChanged();

    }


}
