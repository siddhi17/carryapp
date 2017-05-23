package com.carryapp.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.carryapp.R;

import java.util.HashMap;
import java.util.Map;


public class SplashScreen extends Activity {

    private Thread mSplashThread;
    public ImageView mImageView;
    public int mShortAnimationDuration = 5000;
    public LinearLayout mLinearLayout;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        final SplashScreen sPlashScreen = this;

        mImageView = (ImageView) findViewById(R.id.imageViewLogo);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        fadeSplashOut();

      /*  mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(3000);
                    }
                }
                catch(InterruptedException ex){
                }

                finish();


              *//*  Intent intent = new Intent();
                intent.setClass(sPlashScreen,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);*//*


              *//*  new FadeOutAnimation(mImageView).animate();*//*


            }
        };

        mSplashThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }*/

    }
    private void fadeSplashOut() {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        mLinearLayout.setAlpha(1f);
        mLinearLayout.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        mLinearLayout.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        mImageView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mImageView.setVisibility(View.GONE);

                        finish();
                        Intent intent = new Intent();
                        intent.setClass(SplashScreen.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
    }

}