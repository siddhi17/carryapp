package com.carryapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carryapp.Classes.Transport;
import com.carryapp.R;
import com.carryapp.Adapters.TransportListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransportListFragment extends Fragment {

    private RecyclerView mRecyclerView_list;
    private ArrayList<Transport> mTransportList;
    private TransportListAdapter mTransportAdapter;

    public TransportListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_transport_list, container, false);

        mRecyclerView_list = (RecyclerView) view.findViewById(R.id.rv_transportList);
        mTransportList = new ArrayList<Transport>();
        mTransportAdapter = new TransportListAdapter(getActivity(),mTransportList, TransportListFragment.this);
        mRecyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView_list.setAdapter(mTransportAdapter);
        mRecyclerView_list.setHasFixedSize(true);
        mRecyclerView_list.setItemViewCacheSize(50);
        mRecyclerView_list.setDrawingCacheEnabled(true);
        mRecyclerView_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        demoData();

        return view;
    }

    public void demoData()
    {
        Transport transport = new Transport("17/05/2017","","Laptop","siddhi","");

        mTransportList.add(transport);

        transport = new Transport("27/05/2017","","Mobile","sid","");

        mTransportList.add(transport);

        transport = new Transport("25/05/2017","","Bag","Shital","");

        mTransportList.add(transport);

    }

}
