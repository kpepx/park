package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.Toast;


import androidx.annotation.Nullable;

public class Display extends Activity {
    RadioButton genderradioButton;
    RadioGroup  radioGroup;
    Button buttonback3;//ปุ่มย้อนไปหน้าsetting

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_page);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        buttonback3 = findViewById(R.id.buttonback3); //ปุ่มย้อนไปหน้าsetting
        buttonback3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Display.this, Setting.class));
                finish();
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