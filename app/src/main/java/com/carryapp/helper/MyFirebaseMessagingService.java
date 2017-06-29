package com.carryapp.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.AsyncTasks.AddNotiAsyncTask;
import com.carryapp.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


/**
 * Created by Siddhi on 10/18/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String noti;
    public static Boolean mNotification;
    public int notificationCount;
    private SessionData sessionData;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sessionData = new SessionData(getApplicationContext());




        Map<String, String> data = remoteMessage.getData();

        mNotification = true;

        noti = sessionData.getString("notificationCount","0");

        notificationCount = Integer.parseInt(noti);

        String title = data.get("title");
        String message = data.get("text");

            notificationCount ++;

            sessionData.add("notificationCount", String.valueOf(notificationCount));

        Log.e("notificationCount",String.valueOf(notificationCount));

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.describeContents());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //get data from server notification.

        sendNotification(message, title);
    }

    //send notification

    private void sendNotification(String messageBody, String title) {


        mNotification = true;

        Intent intent = new Intent(this,HomeActivity.class);
        if (!messageBody.equals("")) {
            intent = new Intent(this, HomeActivity.class);
            intent.putExtra("title", title);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        long[] pattern = {500, 500, 500, 500, 500};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.login_logo_1)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setSound(defaultSoundUri)
           /*     .addAction(R.string.accept,getString(R.string.accept), pendingIntent)
                .addAction(R.string.reject,getString(R.string.reject), pendingIntent)*/
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.login_logo_1 : R.drawable.login_logo_1;
    }
}