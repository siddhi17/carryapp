package com.carryapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    Snackbar snackbar;
    private static final long BACK_PRESS_DELAY = 10000;
    private long mBackPressTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLogo = (ImageView)findViewById(R.id.imgLogo);
        mTxtTitle = (TextView)findViewById(R.id.textTitle);


        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragment fragment = new MainFragment();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment,"MAIN_FRAGMENT").commitAllowingStateLoss();


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

                FragmentManager fragmentManager = getSupportFragmentManager();
                AccountFragment fragment1 = new AccountFragment();
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment1,"ACCOUNT_FRAGMENT").addToBackStack("A").commit();


                break;

            case R.id.menu_transfers:

                fragmentManager = getSupportFragmentManager();
                TransfersFragment fragment2 = new TransfersFragment();
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment2,"TRANSFERS_FRAGMENT").addToBackStack("F").commit();


                break;

        }

        return true;
    }
    @Override
    public void onBackPressed() {

        // Do nothing if the back button is disabled.
        if (!mBackPressCancelled) {

            // Pop fragment if the back stack is not empty.
            mTxtTitle.setVisibility(View.GONE);
            mLogo.setVisibility(View.VISIBLE);


            if (getFragmentManager().getBackStackEntryCount() > 0) {



                super.onBackPressed();
            }
            else {

                super.onBackPressed();

               /* if (snackbar != null) {
                    snackbar.dismiss();
                }
                long currentTimestamp = System.currentTimeMillis();

                if (currentTimestamp < mBackPressTimestamp + BACK_PRESS_DELAY) {
                    super.onBackPressed();
                } else {
                    mBackPressTimestamp = currentTimestamp;

                    Toast.makeText(this,"press again",Toast.LENGTH_LONG).show();
                }*/
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri u)
    {

    }

}
