package com.carryapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carryapp.Adapters.ScheduledTravelAdapter;
import com.carryapp.Adapters.TravelHistoryAdapter;
import com.carryapp.AsyncTasks.GetScheduledTripsAsyncTask;
import com.carryapp.AsyncTasks.GetTripHistoryAsyncTask;
import com.carryapp.Classes.TravelHistory;
import com.carryapp.Classes.Trips;
import com.carryapp.R;
import com.carryapp.helper.SessionData;

import java.util.ArrayList;


public class TravelHistoryFragment extends Fragment implements GetTripHistoryAsyncTask.GetTripsHistoryCallBack{


    public RecyclerView mRecyclerView_list;
    private ArrayList<TravelHistory> mTripsList;
    private TravelHistoryAdapter mTravelHistoryAdapter;
    public TextView mTextViewData;
    private SessionData sessionData;
    private RelativeLayout parentLayout;

    public static TravelHistoryFragment newInstance(String param1, String param2) {
        TravelHistoryFragment fragment = new TravelHistoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel_history, container, false);


        setUpUI(view);


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        GetTripHistoryAsyncTask getTripHistoryAsyncTask = new GetTripHistoryAsyncTask(getActivity(),parentLayout,TravelHistoryFragment.this,TravelHistoryFragment.this);
        getTripHistoryAsyncTask.execute(sessionData.getString("api_key",""));

    }

    public void setUpUI(View view)
    {

        mRecyclerView_list = (RecyclerView) view.findViewById(R.id.rv_tripHistory);
        mTextViewData = (TextView) view.findViewById(R.id.textViewData);
        parentLayout = (RelativeLayout) view.findViewById(R.id.parentPanel);

        mTripsList = new ArrayList<TravelHistory>();
        mTravelHistoryAdapter = new TravelHistoryAdapter(getActivity(),mTripsList, TravelHistoryFragment.this);
        mRecyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView_list.setAdapter(mTravelHistoryAdapter);
        mRecyclerView_list.setHasFixedSize(true);
        mRecyclerView_list.setItemViewCacheSize(50);
        mRecyclerView_list.setDrawingCacheEnabled(true);
        mRecyclerView_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        sessionData = new SessionData(getActivity());


    }

    @Override
    public void doPostExecute(ArrayList<TravelHistory> list)
    {
        mTripsList.addAll(list);

        mTravelHistoryAdapter.notifyDataChanged();

    }

    public void demoData()
    {
        TravelHistory trips = new TravelHistory("Aurangabad","Pune","20 March,2017","");

        mTripsList.add(trips);

        trips = new TravelHistory("Miraroad","Nasik","16 March,2017","");

        mTripsList.add(trips);

        trips = new TravelHistory("Nasik","pune","17 March,2017","");

        mTripsList.add(trips);

    }
}
