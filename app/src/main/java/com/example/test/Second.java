package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Second extends Activity {
    public DatabaseReference data;
    SharedPreferences sp,myID;
    public static final String PREFS_NAME = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_page);
        final EditText id = (EditText) findViewById(R.id.id);
        final EditText code = (EditText) findViewById(R.id.code);
        final Button sign = (Button) findViewById(R.id.sign);
        myID = getSharedPreferences("ID", 0);
        sp = getSharedPreferences(PREFS_NAME, 0);
        if(sp.getBoolean("logged",false)){
            goToMainpage();
        }
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String valueid = id.getText().toString();
                final String valuecode = code.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                data = database.getReference().child("login").child(valueid);
                if(valueid.equals("") || valuecode.equals("")){
                    Toast.makeText(Second.this, "Please input ID or Code", Toast.LENGTH_SHORT).show();
                }
                else {
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Map map = (Map) dataSnapshot.getValue();
                                String value = String.valueOf(map.get("code"));
                                String value_rfid = String.valueOf(map.get("rfid"));
                                if (value.equals(valuecode)) {
                                    SharedPreferences.Editor editor = myID.edit();
                                    editor.putString("idKey", valueid);
                                    editor.putString("rfid", value_rfid);
                                    editor.apply();
                                    editor.commit();
                                    goToMainpage();
                                    finish();
                                    sp.edit().putBoolean("logged",true).apply();
                                    Toast.makeText(Second.this, "Sucessfull", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Second.this, "Please Enter Code again", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Second.this, "ID not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Second.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    public void goToMainpage(){
        Intent i = new Intent(this,Third.class);
        startActivity(i);
        finish();
    }
}