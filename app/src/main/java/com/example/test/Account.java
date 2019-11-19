package com.example.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.SharedPreferences;

import java.util.Map;

public class Account extends Fragment {
    public DatabaseReference data;
    private TextView text_name,text_surname,text_phone,text_id,text_code,text_email;
    SharedPreferences myPrefs;
    Button buttonback1; //ปุ่มย้อนไปหน้าsetting
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_page, container, false);
        buttonback1 = view.findViewById(R.id.buttonback1); //ปุ่มย้อนไปหน้าsetting
        buttonback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingFragment()).commit();
            }
        });
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        String name = myPrefs.getString("idKey","Default");
        text_name = (TextView) view.findViewById(R.id.name);
        text_surname = (TextView) view.findViewById(R.id.surname);
        text_phone = (TextView) view.findViewById(R.id.phone);
        text_id = (TextView) view.findViewById(R.id.stid);
        text_code = (TextView) view.findViewById(R.id.codesign);
        text_email = (TextView) view.findViewById(R.id.email);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        data = database.getReference().child("login").child(name);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                String value_name = String.valueOf(map.get("name"));
                String value_surname = String.valueOf(map.get("surname"));
                String value_phone = String.valueOf(map.get("tel"));
                String value_id = String.valueOf(map.get("id"));
                String value_code = String.valueOf(map.get("code"));
                String value_email = String.valueOf(map.get("email"));
                text_name.setText("Name: "+value_name);
                text_surname.setText("Surname: "+value_surname);
                text_phone.setText("Phone: "+value_phone);
                text_id.setText("ID: "+value_id);
                text_code.setText("Code: "+value_code);
                text_email.setText("Email: "+value_email);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
