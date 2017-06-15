package com.carryapp.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by siddhi jambhale on 6/11/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {

    String ACCEPT,REJECT;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(ACCEPT.equals(action)) {
                Log.v("Delivery","Accepted");
            }
            else if(REJECT.equals(action)) {
                Log.v("Delivery","Rejected");
            }

        }

}
