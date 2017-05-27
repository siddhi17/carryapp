package com.carryapp.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.carryapp.Fragments.ScheduledTravelFragment;
import com.carryapp.Fragments.TravelHistoryFragment;


/**
 * Created by Siddhi on 11/10/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;
    public TabsPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new ScheduledTravelFragment();
            case 1:
                return new TravelHistoryFragment();
        }
        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
