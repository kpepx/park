package com.example.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.Nullable;
import android.app.Fragment;

public class Display extends Fragment {
    SharedPreferences myPrefs;
    RadioButton genderradioButton;
    RadioGroup  radioGroup;
    Button buttonback3;//ปุ่มย้อนไปหน้าsetting
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_page, container, false);
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        ToggleButton togglebutton = (ToggleButton) view.findViewById(R.id.toggleButton);
        if(myPrefs.getBoolean("togglebutton", false)){
            togglebutton.setChecked(myPrefs.getBoolean("togglebutton", false));
        }
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        buttonback3 = view.findViewById(R.id.buttonback3); //ปุ่มย้อนไปหน้าsetting
        buttonback3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingFragment()).commit();
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
        return view;
    }

//    public void onclickbuttonMethod(View v) {
//        int selectedId = radioGroup.getCheckedRadioButtonId();
//        genderradioButton = (RadioButton) findViewById(selectedId);
//        if (selectedId == -1) {
//            Toast.makeText(Display.this, "Nothing selected", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(Display.this, genderradioButton.getText(), Toast.LENGTH_SHORT).show();
//        }
//
//    }
}