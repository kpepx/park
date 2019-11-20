package com.example.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import android.app.Fragment;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

public class SettingFragment extends Fragment{
    public DatabaseReference data;
    public static final String PREFS_NAME = "Login";
    SharedPreferences myPrefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_page, container, false);
        Button about = (Button)view.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new About()).commit();
            }
        });
        Button account = (Button)view.findViewById(R.id.account);
        account.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Account()).commit();
            }
        });
        Button display = (Button)view.findViewById(R.id.display);
        display.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Display()).commit();
            }
        });
        Button logout = (Button)view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPrefs = getActivity().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear().apply();
                editor.commit();
                Toast.makeText(getActivity().getApplication(), "Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
//        mTextView = view.findViewById(R.id.textView);
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        Switch sw = (Switch) view.findViewById(R.id.swbutton);
        if(myPrefs.getBoolean("sw", false)){
            sw.setChecked(myPrefs.getBoolean("sw", false));
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
        return view;
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
                        if(value_status.equals("in")){
                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.HOUR_OF_DAY, value_hour-1);
                            c.set(Calendar.MINUTE, value_min);
                            c.set(Calendar.SECOND, 0);
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
        myPrefs.edit().putString("time",DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime())).apply();
//        mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplication(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplication(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);

        alarmManager.cancel(pendingIntent);
//        mTextView.setText("Alarm canceled");
    }
}
