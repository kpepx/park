package com.example.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class Add extends Fragment {
    SharedPreferences myPrefs;
    boolean check1,check2,check3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add,container,false);
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        CheckBox cb1 = (CheckBox) view.findViewById(R.id.checkBox);
        CheckBox cb2 = (CheckBox) view.findViewById(R.id.checkBox2);
        CheckBox cb3 = (CheckBox) view.findViewById(R.id.checkBox3);
        if(myPrefs.getBoolean("cb1", false)){
            check1 = myPrefs.getBoolean("cb1", false);
            cb1.setChecked(myPrefs.getBoolean("cb1", false));
        }
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check1 = true;
//                    myPrefs.edit().putBoolean("cb1",true).apply();
                    Toast.makeText(getActivity().getApplication(), "cb1 ON", Toast.LENGTH_SHORT).show();
                }
                else {
                    check1 = false;
//                    myPrefs.edit().putBoolean("cb1",false).apply();
                    Toast.makeText(getActivity().getApplication(), "cb1 OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        if(myPrefs.getBoolean("cb2", false)){
            check2 = myPrefs.getBoolean("cb2", false);
            cb2.setChecked(myPrefs.getBoolean("cb2", false));
        }
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check2 = true;
                    Toast.makeText(getActivity().getApplication(), "cb2 ON", Toast.LENGTH_SHORT).show();
                }
                else {
                    check2 = false;
                    Toast.makeText(getActivity().getApplication(), "cb2 OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        if(myPrefs.getBoolean("cb3", false)){
            check3 = myPrefs.getBoolean("cb3", false);
            cb3.setChecked(myPrefs.getBoolean("cb3", false));
        }
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check3 = true;
                    Toast.makeText(getActivity().getApplication(), "cb3 ON", Toast.LENGTH_SHORT).show();
                }
                else {
                    check3 = false;
                    Toast.makeText(getActivity().getApplication(), "cb3 OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button complete= (Button)view.findViewById(R.id.completebutton);
        complete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPrefs.edit().putBoolean("cb1",check1).apply();
                myPrefs.edit().putBoolean("cb2",check2).apply();
                myPrefs.edit().putBoolean("cb3",check3).apply();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavFragment()).commit();
            }
        });
        return view;
    }
}