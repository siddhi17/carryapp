package com.carryapp.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.carryapp.Classes.Notifications;
import com.carryapp.R;
import com.carryapp.helper.SessionData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_blank, container, false);

      /*  mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_notificationsList);


        sessionData = new SessionData(getActivity());

        parentPanel = (LinearLayout) view.findViewById(R.id.parentPanel);
        notificationsArrayList = new ArrayList<Notifications>();*/
        return view;
}

}
