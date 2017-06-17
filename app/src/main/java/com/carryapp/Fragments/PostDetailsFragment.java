package com.carryapp.Fragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailsFragment extends Fragment {

    private Button mBtnPosts;

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        setUpUI(view);

        listeners();

        return view;
    }


    public void setUpUI(View view)
    {

        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView)getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.VISIBLE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.GONE);

        mBtnPosts = (Button) view.findViewById(R.id.btn_posts);

    }

    public void listeners()
    {

        mBtnPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                MyTripsFragment fragment1 = new MyTripsFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment1,"MY_TRIPS_FRAGMENT").addToBackStack("L").commit();

            }
        });
    }

}
