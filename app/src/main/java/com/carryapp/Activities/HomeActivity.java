package com.carryapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryapp.Fragments.AccountFragment;
import com.carryapp.Fragments.MainFragment;
import com.carryapp.Fragments.MyTripsFragment;
import com.carryapp.Fragments.NoticesFragment;
import com.carryapp.Fragments.TransfersFragment;
import com.carryapp.R;
import com.carryapp.Fragments.TransportFragment;
import com.facebook.login.LoginManager;


public class HomeActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,TransportFragment.OnFragmentInteractionListener{

    public static Toolbar toolbar;
    public static ImageView mLogo;
    private TextView mTxtTitle;
    private boolean mBackPressCancelled = false;
    Snackbar snackbar;
    private static final long BACK_PRESS_DELAY = 10000;
    private long mBackPressTimestamp;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLogo = (ImageView)findViewById(R.id.imgLogo);
        mTxtTitle = (TextView)findViewById(R.id.textTitle);


        FragmentManager fragmentManager = getFragmentManager();
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

                FragmentManager fragmentManager = getFragmentManager();
                AccountFragment fragment1 = new AccountFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment1,"ACCOUNT_FRAGMENT").addToBackStack("A").commit();


                break;

            case R.id.menu_transfers:

                fragmentManager = getFragmentManager();
                TransfersFragment fragment2 = new TransfersFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment2,"TRANSFERS_FRAGMENT").addToBackStack("F").commit();

                break;

            case R.id.menu_notifications:

                fragmentManager = getFragmentManager();
                NoticesFragment fragment3 = new NoticesFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment3,"NOTICES_FRAGMENT").addToBackStack("H").commit();

                break;

            case R.id.menu_myTrips:

                fragmentManager = getFragmentManager();
                MyTripsFragment fragment4 = new MyTripsFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment4,"MY_TRIPS_FRAGMENT").addToBackStack("H").commit();

                break;
            case R.id.menu_sign_off:

                LoginManager.getInstance().logOut();

                SharedPreferences pref = getSharedPreferences("appdata", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                finish();
                mIntent = new Intent(HomeActivity.this, MainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(mIntent);
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
