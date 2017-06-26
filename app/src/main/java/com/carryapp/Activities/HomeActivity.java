package com.carryapp.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carryapp.AsyncTasks.GetNotificationsAsyncTask;
import com.carryapp.AsyncTasks.LogOutAsyncTask;
import com.carryapp.Classes.Notifications;
import com.carryapp.Fragments.AccountFragment;
import com.carryapp.Fragments.MainFragment;
import com.carryapp.Fragments.MyTripsFragment;
import com.carryapp.Fragments.NoticesFragment;
import com.carryapp.Fragments.TransfersFragment;
import com.carryapp.R;
import com.carryapp.Fragments.TransportFragment;
import com.carryapp.helper.MyFirebaseMessagingService;
import com.carryapp.helper.MyReceiver;
import com.carryapp.helper.SessionData;
import com.facebook.login.LoginManager;
import com.testfairy.TestFairy;

import java.util.ArrayList;
import java.util.Collections;


public class HomeActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,AccountFragment.OnFragmentInteractionListener,TransportFragment.OnFragmentInteractionListener{

    public static Toolbar toolbar;
    public static ImageView mLogo,mNoti;
    public TextView mTxtTitle,mNotificationCount;
    private boolean mBackPressCancelled = false;
    Snackbar snackbar;
    private static final long BACK_PRESS_DELAY = 10000;
    private long mBackPressTimestamp;
    private Intent mIntent;
    private CoordinatorLayout parentLayout;
    private SessionData sessionData;
    private int notificationCount;
    private Bundle bundle;
    private ArrayList<Notifications> notificationsArrayList;
    private Intent intent;
    private Boolean mNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();


        TestFairy.begin(this, "4c99880b3a9b2a94314896853e36f1a62f35c318");

        sessionData = new SessionData(HomeActivity.this);

        mLogo = (ImageView) findViewById(R.id.imgLogo);
        mTxtTitle = (TextView) findViewById(R.id.textTitle);
        parentLayout = (CoordinatorLayout) findViewById(R.id.parentPanel);
        mNoti = (ImageView) findViewById(R.id.noti);

        mNotificationCount = (TextView) findViewById(R.id.textNotification);

        notificationCount = sessionData.getInt("notificationCount",0);

        if(notificationCount > 0)
        {

            mNotificationCount.setVisibility(View.VISIBLE);
            mNotificationCount.setText(String.valueOf(notificationCount));
        }

/*

        GetNotificationsAsyncTask getNotificationsAsyncTask = new GetNotificationsAsyncTask(HomeActivity.this,HomeActivity.this);
        getNotificationsAsyncTask.execute(sessionData.getString("api_key", ""));

*/

            // if order's notification, show orders fragment

            if (intent.getStringExtra("title") != null) {


                FragmentManager fragmentManager = getFragmentManager();
                MainFragment fragment = new MainFragment();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment, "MAIN_FRAGMENT").commitAllowingStateLoss();

                fragmentManager = getFragmentManager();
                NoticesFragment fragment3 = new NoticesFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("title",intent.getStringExtra("title"));
                fragment3.setArguments(bundle1);
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment3, "NOTICES_FRAGMENT").addToBackStack("H").commit();

            }
            else {

            FragmentManager fragmentManager = getFragmentManager();
            MainFragment fragment = new MainFragment();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment, "MAIN_FRAGMENT").commitAllowingStateLoss();
        }

        mNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    // if order's notification, show orders fragment

                    if (intent.getStringExtra("title") != null) {


                        FragmentManager fragmentManager = getFragmentManager();
                        MainFragment fragment = new MainFragment();
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment, "MAIN_FRAGMENT").commitAllowingStateLoss();

                        fragmentManager = getFragmentManager();
                        NoticesFragment fragment3 = new NoticesFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("title",intent.getStringExtra("title"));
                        fragment3.setArguments(bundle1);
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment3, "NOTICES_FRAGMENT").addToBackStack("H").commit();
                    }

                else {

                    FragmentManager fragmentManager = getFragmentManager();
                    NoticesFragment fragment3 = new NoticesFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("icon","icon");
                        fragment3.setArguments(bundle1);
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentManager.beginTransaction().replace(R.id.mycontainer, fragment3, "NOTICES_FRAGMENT").addToBackStack("H").commit();
                }

            }
        });

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



                LogOutAsyncTask logOutAsyncTask = new LogOutAsyncTask(HomeActivity.this,parentLayout);
                logOutAsyncTask.execute(sessionData.getString("api_key",""));


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
