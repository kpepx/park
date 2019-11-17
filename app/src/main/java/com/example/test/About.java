package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class About extends Activity {
    Button buttonback2; //ปุ่มกลับ
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_page);
        buttonback2 = findViewById(R.id.buttonback2);   //ปุ่มย้อนไปหน้าsetting
        buttonback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(About.this,Setting.class));
                finish();
            }
        });
    }
}
