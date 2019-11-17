package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.annotation.Nullable;

public class Display extends Activity {
    SharedPreferences myPrefs;
    RadioButton genderradioButton;
    RadioGroup  radioGroup;
    Button buttonback3;//ปุ่มย้อนไปหน้าsetting

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_page);
        myPrefs = getSharedPreferences("ID", 0);
        ToggleButton togglebutton = (ToggleButton) findViewById(R.id.toggleButton);
        if(myPrefs.getBoolean("togglebutton", false)){
            togglebutton.setChecked(myPrefs.getBoolean("togglebutton", false));
        }
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        buttonback3 = findViewById(R.id.buttonback3); //ปุ่มย้อนไปหน้าsetting
        buttonback3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Display.this, Setting.class));
                finish();
            }
        });
        togglebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myPrefs.edit().putBoolean("togglebutton",true).apply();
                }
                else {
                    myPrefs.edit().putBoolean("togglebutton",false).apply();
                }
            }
        });
    }

    public void onclickbuttonMethod(View v) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        if (selectedId == -1) {
            Toast.makeText(Display.this, "Nothing selected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Display.this, genderradioButton.getText(), Toast.LENGTH_SHORT).show();
        }

    }
}