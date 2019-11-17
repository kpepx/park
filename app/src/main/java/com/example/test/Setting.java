package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class Setting extends Activity {
    public static final String PREFS_NAME = "Login";
    SharedPreferences sp,myPrefs;
    Button buttonback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);
        Button button = (Button)findViewById(R.id.account); //ปุ่มไปหน้าaccount
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Account.class);
                startActivity(i);

            }
        });
        Button buttonAbout = (Button)findViewById(R.id.about); //ปุ่มไปหน้าabout
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), About.class);
                startActivity(i);
            }
        });
        Button buttonDisplay = (Button)findViewById(R.id.display); //ปุ่มไปหน้าdisplay
        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Display.class);
                startActivity(i);
            }
        });
        Button buttonNofi = (Button)findViewById(R.id.notifications); //ปุ่มไปหน้าnoti
        buttonNofi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Notifications.class);
                startActivity(i);
            }
        });
        buttonback = findViewById(R.id.buttonback); //ปุ่มย้อนกลับไปหน้าmap
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting.this,Third.class));
            }
        });
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPrefs = getSharedPreferences("ID", 0);
                sp = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("logged").apply();
                editor.commit();
                Toast.makeText(Setting.this, "Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Setting.this, MainActivity.class);
                startActivity(intent);
                myPrefs.edit().putBoolean("sw",false).apply();
                myPrefs.edit().putBoolean("togglebutton",false).apply();
            }
        });
    }
}
