package com.carryapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.carryapp.AsyncTasks.SearchPostsAsyncTask;
import com.carryapp.Classes.PostDelivery;
import com.carryapp.Classes.Transport;
import com.carryapp.R;
import com.carryapp.Adapters.TransportListAdapter;
import com.carryapp.helper.SessionData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransportListFragment extends Fragment implements SearchPostsAsyncTask.SearchPostsCallBack{

    private RecyclerView mRecyclerView_list;
    private ArrayList<PostDelivery> mTransportList;
    private TransportListAdapter mTransportAdapter;
    private LinearLayout parentLayout;
    private SessionData sessionData;

    public TransportListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_transport_list, container, false);

        sessionData = new SessionData(getActivity());

        setUpUI(view);


        SearchPostsAsyncTask searchPostsAsyncTask = new SearchPostsAsyncTask(getActivity(),parentLayout,TransportListFragment.this);
        searchPostsAsyncTask.execute("19.878454","73.836708","20.012709","73.791389","2017-05-30 06:30",sessionData.getString("api_key",""));

      //  demoData();

        return view;
    }

    public void setUpUI(View view)
    {

        mRecyclerView_list = (RecyclerView) view.findViewById(R.id.rv_transportList);
        mTransportList = new ArrayList<PostDelivery>();
        mTransportAdapter = new TransportListAdapter(getActivity(),mTransportList, TransportListFragment.this);
        mRecyclerView_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView_list.setAdapter(mTransportAdapter);
        mRecyclerView_list.setHasFixedSize(true);
        mRecyclerView_list.setItemViewCacheSize(50);
        mRecyclerView_list.setDrawingCacheEnabled(true);
        mRecyclerView_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        parentLayout = (LinearLayout) view.findViewById(R.id.parentPanel);

    }

 /*   public void demoData()
    {
        Transport transport = new Transport("17/05/2017","","Laptop","siddhi","");

        mTransportList.add(transport);

        transport = new Transport("27/05/2017","","Mobile","sid","");

        mTransportList.add(transport);

        transport = new Transport("25/05/2017","","Bag","Shital","");

        mTransportList.add(transport);

    }*/

    @Override
    public void doPostExecute(ArrayList<PostDelivery> list)
    {
        mTransportList.addAll(list);

    }

}
