package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import java.util.Calendar;
import android.app.AlarmManager;
import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

public class Notifications extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    public static String MY_PREFS_NAME= "nameOfSharedPreferences";
    Button buttonback4; //ปุ่มย้อนไปหน้าsetting
    Button show;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);
        buttonback4 = findViewById(R.id.buttonback4); //ปุ่มย้อนไปหน้าsetting
        buttonback4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notifications.this,Setting.class));
            }
        });
        Button btnCreateNotification = findViewById(R.id. btnCreateNotification ) ;
        btnCreateNotification.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent notificationIntent = new Intent(Notifications.this,
                        Second.class ) ;
                notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
                PendingIntent resultIntent = PendingIntent. getActivity (Notifications. this, 0 , notificationIntent , 0 );
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Notifications. this, default_notification_channel_id )
                        .setSmallIcon(R.drawable. ic_launcher_foreground )
                        .setContentTitle( "Test" )
                        .setContentIntent(resultIntent)
                        .setStyle( new NotificationCompat.InboxStyle())
                        .setContentText( "Hello! This is my first push notification" ) ;
                NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context. NOTIFICATION_SERVICE );
                if (android.os.Build.VERSION. SDK_INT >=android.os.Build.VERSION_CODES. O ) {
                    int importance = NotificationManager. IMPORTANCE_HIGH ;
                    NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
                    mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
                    assert mNotificationManager != null;
                    mNotificationManager.createNotificationChannel(notificationChannel) ;
                }
                assert mNotificationManager != null;
                mNotificationManager.notify(( int ) System. currentTimeMillis () ,
                        mBuilder.build()) ;
            }
        });
        show = (Button)findViewById(R.id.btn_show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlarm(true,true);
            }
        });
    }
    private void startAlarm(boolean isNotification, boolean isRepeat) {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        // SET TIME HERE
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,17);
        calendar.set(Calendar.MINUTE,23);

        Toast.makeText(Notifications.this, "Yes", Toast.LENGTH_SHORT).show();
        myIntent = new Intent(Notifications.this,AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);


        if(isRepeat){
            Toast.makeText(Notifications.this, calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000,pendingIntent);}
        else{
            Toast.makeText(Notifications.this, "Get", Toast.LENGTH_SHORT).show();
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);}
    }
}
