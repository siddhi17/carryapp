package com.carryapp.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.Adapters.TabsPagerAdapter;
import com.carryapp.R;
import com.carryapp.helper.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


public class MyTripsFragment extends Fragment {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private CharSequence Titles[]={"Scheduled Travel","Travel History"};
    private int Numboftabs =2;
    private SlidingTabLayout tabs;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_trips, container, false);

        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView) getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.GONE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.myTrips);

        mAdapter =  new TabsPagerAdapter(getActivity().getFragmentManager(),Titles,Numboftabs);
        viewPager = (ViewPager)view.findViewById(R.id.pager);

        setupViewPager(viewPager);

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
    @Override
    public void onPause()
    {
        super.onPause();

    }

    @Override
    public void onResume()
    {
        super.onResume();


        setupViewPager(viewPager);
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new ScheduledTravelFragment(), "Scheduled Travel");
        adapter.addFragment(new TravelHistoryFragment(), "Travel History");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
