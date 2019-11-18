package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.widget.Button;
import android.widget.Toast;

public class SettingFragment extends Fragment{
    public static final String PREFS_NAME = "Login";
    SharedPreferences myPrefs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
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
        Button notifications = (Button)view.findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Notifications()).commit();
            }
        });
        Button logout = (Button)view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPrefs = getActivity().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.remove("logged").apply();
                editor.commit();
                Toast.makeText(getActivity().getApplication(), "Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

}
