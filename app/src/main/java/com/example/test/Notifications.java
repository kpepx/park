package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.PendingIntent;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import java.util.Calendar;
import android.app.AlarmManager;
import android.widget.Switch;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Map;

public class Notifications extends AppCompatActivity {
    public DatabaseReference data;
    SharedPreferences myPrefs;
    private TextView mTextView;
    Button buttonback4; //ปุ่มย้อนไปหน้าsetting
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
        mTextView = findViewById(R.id.textView);
        myPrefs = getSharedPreferences("ID", 0);
        Switch sw = (Switch) findViewById(R.id.swbutton);
        if(myPrefs.getBoolean("sw", true)){
            sw.setChecked(myPrefs.getBoolean("sw", true));
            onTimeSet();
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myPrefs.edit().putBoolean("sw",true).apply();
                    onTimeSet();
                }
                else {
                    myPrefs.edit().putBoolean("sw",false).apply();
                    cancelAlarm();
                }
            }
        });
    }
    public void onTimeSet() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String rfid = myPrefs.getString("rfid","Default");
        data = database.getReference().child("User").child(rfid);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                final String value_status = String.valueOf(map.get("status"));
                String value_place = String.valueOf(map.get("place"));
                myPrefs.edit().putString("place",value_place).apply();
                String place = myPrefs.getString("place","Default");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference data_place = database.getReference().child("Park").child(place).child("close");
                data_place.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map map = (Map) dataSnapshot.getValue();
                        int value_hour = Integer.parseInt(String.valueOf(map.get("hour")));
                        int value_min = Integer.parseInt(String.valueOf(map.get("min")));
                        String value_hour_txt = String.valueOf(map.get("hour"));
                        String value_min_txt = String.valueOf(map.get("min"));
                        if(value_status.equals("in")){
                            myPrefs.edit().putString("hour",value_hour_txt).apply();
                            myPrefs.edit().putString("min",value_min_txt).apply();
                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.HOUR_OF_DAY, value_hour-1);
                            c.set(Calendar.MINUTE, value_min);
                            c.set(Calendar.SECOND, -45);
                            updateTimeText(c);
                            startAlarm(c);
                        }
                        else {
                            cancelAlarm();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        mTextView.setText("Alarm canceled");
    }
}
