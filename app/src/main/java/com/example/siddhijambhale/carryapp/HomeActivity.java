package com.example.siddhijambhale.carryapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;

public class HomeActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,TransportFragment.OnFragmentInteractionListener{

    private Toolbar toolbar;
    private ImageView mLogo;
    private boolean mBackPressCancelled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLogo = (ImageView)findViewById(R.id.imageViewLogo);

        FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
        MainFragment fragment = new MainFragment();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment).commitAllowingStateLoss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        //     menu.clear();
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_account:

                toolbar.setTitle(R.string.menu_account);

                FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
                AccountFragment fragment1 = new AccountFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment1).addToBackStack("A").commit();
                return true;

        }

        return true;
    }
    @Override
    public void onBackPressed() {

        // Do nothing if the back button is disabled.
        if (!mBackPressCancelled) {

            // Pop fragment if the back stack is not empty.

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();

                //       goToOrderFrag();
            } /*else {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                long currentTimestamp = System.currentTimeMillis();

                if (currentTimestamp < mBackPressTimestamp + BACK_PRESS_DELAY) {
                    super.onBackPressed();
                } else {
                    mBackPressTimestamp = currentTimestamp;

                    showAlert(getString(R.string.msg_backpress));
                }
            }*/
        }
    }

    @Override
    public void onFragmentInteraction(Uri u)
    {

    }

}
