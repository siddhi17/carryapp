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

import com.carryapp.Adapters.ScheduledTravelAdapter;
import com.carryapp.Adapters.TravelHistoryAdapter;
import com.carryapp.Classes.TravelHistory;
import com.carryapp.Classes.Trips;
import com.carryapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TravelHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TravelHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TravelHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView_list;
    private ArrayList<TravelHistory> mTripsList;
    private TravelHistoryAdapter mTravelHistoryAdapter;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TravelHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TravelHistoryFragment newInstance(String param1, String param2) {
        TravelHistoryFragment fragment = new TravelHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel_history, container, false);

        mRecyclerView_list = (RecyclerView) view.findViewById(R.id.rv_tripHistory);
        mTripsList = new ArrayList<TravelHistory>();
        mTravelHistoryAdapter = new TravelHistoryAdapter(getActivity(),mTripsList, TravelHistoryFragment.this);
        mRecyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView_list.setAdapter(mTravelHistoryAdapter);
        mRecyclerView_list.setHasFixedSize(true);
        mRecyclerView_list.setItemViewCacheSize(50);
        mRecyclerView_list.setDrawingCacheEnabled(true);
        mRecyclerView_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        demoData();

        return view;
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
