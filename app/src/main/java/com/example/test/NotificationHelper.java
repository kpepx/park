package com.example.test;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        SharedPreferences myPrefs = getSharedPreferences("ID", 0);
        String h = myPrefs.getString("hour","Default");
        String m = myPrefs.getString("min","Default");
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("ลานจอดรถกำลังจะปิด")
                .setContentText("โปรดนำรถของท่านออกจากลานจอดรถก่อนเวลา "+h+":"+m+" น.")
                .setSmallIcon(R.drawable.ic_launcher_background);
    }
}
