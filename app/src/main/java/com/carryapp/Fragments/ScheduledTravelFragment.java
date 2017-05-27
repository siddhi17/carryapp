package com.carryapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carryapp.Adapters.ScheduledTravelAdapter;
import com.carryapp.Adapters.TransportListAdapter;
import com.carryapp.Classes.Transport;
import com.carryapp.Classes.Trips;
import com.carryapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduledTravelFragment extends Fragment {

    private RecyclerView mRecyclerView_list;
    private ArrayList<Trips> mTripsList;
    private ScheduledTravelAdapter mTripsAdapter;


    public ScheduledTravelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scheduled_travel, container, false);

        mRecyclerView_list = (RecyclerView) view.findViewById(R.id.rv_tripList);
        mTripsList = new ArrayList<Trips>();
        mTripsAdapter = new ScheduledTravelAdapter(getActivity(),mTripsList, ScheduledTravelFragment.this);
        mRecyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView_list.setAdapter(mTripsAdapter);
        mRecyclerView_list.setHasFixedSize(true);
        mRecyclerView_list.setItemViewCacheSize(50);
        mRecyclerView_list.setDrawingCacheEnabled(true);
        mRecyclerView_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        demoData();

        return view;
    }


    public void demoData()
    {
        Trips trips = new Trips("Mumbai","Pune","12 March,2017","");

        mTripsList.add(trips);

        trips = new Trips("Pune","Mumbai","15 March,2017","");

        mTripsList.add(trips);

        trips = new Trips("Nasik","pune","17 March,2017","");

        mTripsList.add(trips);

    }
}
