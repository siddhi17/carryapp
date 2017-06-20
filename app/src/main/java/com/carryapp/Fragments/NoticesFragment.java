package com.carryapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Adapters.NotificationsAdapter;
import com.carryapp.AsyncTasks.GetNotificationsAsyncTask;
import com.carryapp.Classes.Notifications;
import com.carryapp.R;
import com.carryapp.helper.SessionData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticesFragment extends Fragment implements GetNotificationsAsyncTask.GetNotificationsCallBack{


    private RelativeLayout mLayoutMessages,mLayoutNotifications,mRecyclerViewLayout;
    private LinearLayout mMessagesLayout,parentPanel;
    public RecyclerView mRecyclerView;
    public TextView mTextViewNotifications,mTextViewMessages,mTextViewData;
    private ArrayList<Notifications> notificationsArrayList;
    private NotificationsAdapter mNotificationsAdapter;
    private SessionData sessionData;

    public NoticesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notices, container, false);

        setUpUI(view);

        listeners();

        return view;
    }


    public void setUpUI(View view)
    {

        sessionData = new SessionData(getActivity());

        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView) getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.GONE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.notifications);


        mLayoutMessages = (RelativeLayout) view.findViewById(R.id.linearLayoutMessages);
        mLayoutNotifications = (RelativeLayout) view.findViewById(R.id.linearLayoutNotifications);

        mMessagesLayout = (LinearLayout) view.findViewById(R.id.messagesLayout);
        mRecyclerViewLayout = (RelativeLayout) view.findViewById(R.id.recyclerViewLayout);
        parentPanel = (LinearLayout) view.findViewById(R.id.parentPanel);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_notificationsList);
        mTextViewNotifications = (TextView) view.findViewById(R.id.notifications);
        mTextViewMessages = (TextView) view.findViewById(R.id.messages);
        mTextViewData = (TextView) view.findViewById(R.id.textViewData);
/*
        mRecyclerView.setVisibility(View.GONE);
        mMessagesLayout.setVisibility(View.VISIBLE);*/
        notificationsArrayList = new ArrayList<Notifications>();

        mNotificationsAdapter = new NotificationsAdapter(getActivity(),notificationsArrayList, NoticesFragment.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNotificationsAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(50);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        Bundle bundle = this.getArguments();

        if(bundle != null) {

            if (bundle.getBoolean("notification", false)) {

                setNotifications();
            }
        }

    }

    public void listeners()
    {

        mLayoutMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerView.setVisibility(View.GONE);
                mMessagesLayout.setVisibility(View.VISIBLE);
                mRecyclerViewLayout.setVisibility(View.GONE);
                mLayoutNotifications.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorButton));
                mLayoutMessages.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.border));
                mTextViewMessages.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorButton));
                mTextViewNotifications.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
            }
        });

        mLayoutNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setNotifications();

            }
        });

    }

    public void setNotifications()
    {


        GetNotificationsAsyncTask getNotificationsAsyncTask = new GetNotificationsAsyncTask(getActivity(),NoticesFragment.this,NoticesFragment.this,parentPanel);
        getNotificationsAsyncTask.execute(sessionData.getString("api_key",""));


        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerViewLayout.setVisibility(View.VISIBLE);

        mLayoutNotifications.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.border));
        mLayoutMessages.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorButton));
        mTextViewNotifications.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorButton));
        mTextViewMessages.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));


    }

    @Override
    public void doPostExecute(ArrayList<Notifications> list)
    {

        notificationsArrayList.clear();

        notificationsArrayList.addAll(list);

        mNotificationsAdapter.notifyDataSetChanged();

    }
}
