package com.carryapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryapp.AsyncTasks.SearchPostsAsyncTask;
import com.carryapp.AsyncTasks.SendNotiAsyncTask;
import com.carryapp.Classes.PostDelivery;
import com.carryapp.Classes.Transport;
import com.carryapp.R;
import com.carryapp.Adapters.TransportListAdapter;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.SessionData;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransportListFragment extends Fragment implements SearchPostsAsyncTask.SearchPostsCallBack,SendNotiAsyncTask.SendNotificationCallBack{

    public RecyclerView mRecyclerView_list;
    private ArrayList<PostDelivery> mTransportList;
    private TransportListAdapter mTransportAdapter;
    public LinearLayout parentLayout;
    private SessionData sessionData;
    public TextView textViewFrom,textViewTo,textViewDate,textViewData;
    private String mFrom,mTo,mDate;
    private LatLng mToLatLang,mFromLocation;
    private double startLatitude,startLongitude,endLatitude,endLongitude;
    public static Button mBtnRequest;
    double precision;

    public TransportListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_transport_list, container, false);

        sessionData = new SessionData(getActivity());

        setUpUI(view);

        precision =  Math.pow(10,6);

        startLatitude =  (int)(precision * mFromLocation.latitude)/precision;
        startLongitude =  (int)(precision * mFromLocation.longitude)/precision;
        endLatitude =  (int)(precision * mToLatLang.latitude)/precision;
        endLongitude =  (int)(precision * mToLatLang.longitude)/precision;

        SearchPostsAsyncTask searchPostsAsyncTask = new SearchPostsAsyncTask(getActivity(),parentLayout,TransportListFragment.this,TransportListFragment.this);
        searchPostsAsyncTask.execute(String.valueOf(startLatitude),String.valueOf(startLongitude),
                String.valueOf(endLatitude),String.valueOf(endLongitude),mDate,sessionData.getString("api_key",""));

     /*   SearchPostsAsyncTask searchPostsAsyncTask = new SearchPostsAsyncTask(getActivity(),parentLayout,TransportListFragment.this,TransportListFragment.this);
        searchPostsAsyncTask.execute(String.valueOf(mFromLocation.latitude),String.valueOf(mFromLocation.longitude),
                String.valueOf(mToLatLang.latitude),String.valueOf(mToLatLang.longitude),mDate,sessionData.getString("api_key",""));*/

      //  demoData();

        return view;
    }

    public void setUpUI(View view)
    {

        textViewFrom = (TextView) view.findViewById(R.id.tv_from);
        textViewTo = (TextView) view.findViewById(R.id.tv_to);
        textViewDate = (TextView) view.findViewById(R.id.tv_dateTime);
        mBtnRequest = (Button) view.findViewById(R.id.btnRequest);
        textViewData = (TextView) view.findViewById(R.id.textViewData);

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

        final Bundle bundle = this.getArguments();

        if (bundle != null) {

            mFrom = bundle.getString("from");
            mTo = bundle.getString("to");
            mFromLocation = bundle.getParcelable("fromLatLang");
            mToLatLang = bundle.getParcelable("toLatLang");
            mDate = bundle.getString("date");

        }

        String date = CommonUtils.formateDateFromstring("yyyy-MM-dd", "dd MMM, yyyy", mDate);

        textViewFrom.setText(mFrom);
        textViewTo.setText(mTo);
        textViewDate.setText(date);

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

        mTransportAdapter.notifyDataChanged();

    }


    @Override
    public void doPostExecute(Boolean notification)
    {
        if(notification)
        {
            SearchPostsAsyncTask searchPostsAsyncTask = new SearchPostsAsyncTask(getActivity(),parentLayout,TransportListFragment.this,TransportListFragment.this);
            searchPostsAsyncTask.execute(String.valueOf(mFromLocation.latitude),String.valueOf(mFromLocation.longitude),
                    String.valueOf(mToLatLang.latitude),String.valueOf(mToLatLang.longitude),mDate,sessionData.getString("api_key",""));
        }
    }

}
