package com.carryapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryapp.Fragments.AccountFragment;
import com.carryapp.Fragments.CarPickerFragment;
import com.carryapp.Fragments.MainFragment;
import com.carryapp.Fragments.TransfersFragment;
import com.carryapp.R;
import com.carryapp.Fragments.TransportFragment;

public class HomeActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,TransportFragment.OnFragmentInteractionListener,CarPickerFragment.OnFragmentInteractionListener{

    public static Toolbar toolbar;
    public static ImageView mLogo;
    private TextView mTxtTitle;
    private boolean mBackPressCancelled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLogo = (ImageView)findViewById(R.id.imgLogo);
        mTxtTitle = (TextView)findViewById(R.id.textTitle);



        FragmentManager fragmentManager = HomeActivity.this.getFragmentManager();
        MainFragment fragment = new MainFragment();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment).commitAllowingStateLoss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_account:

                FragmentManager fragmentManager = HomeActivity.this.getFragmentManager();
                AccountFragment fragment1 = new AccountFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment1).addToBackStack("A").commit();


                break;

            case R.id.menu_transfers:

                fragmentManager = HomeActivity.this.getFragmentManager();
                TransfersFragment fragment2 = new TransfersFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment2).addToBackStack("F").commit();


                break;

        }

        return true;
    }
    @Override
    public void onBackPressed() {

        // Do nothing if the back button is disabled.
        if (!mBackPressCancelled) {

            // Pop fragment if the back stack is not empty.

            if (getFragmentManager().getBackStackEntryCount() > 0) {
                mTxtTitle.setVisibility(View.GONE);
                mLogo.setVisibility(View.VISIBLE);
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri u)
    {

    }

}
