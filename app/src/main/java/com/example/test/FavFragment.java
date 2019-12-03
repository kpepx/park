package com.example.test;

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

public class FavFragment extends Fragment {
    SharedPreferences myPrefs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_fav,container,false);
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        Button add = (Button)view.findViewById(R.id.addbutton);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Add()).commit();
            }
        });
        Button map = (Button)view.findViewById(R.id.buttonmap1);
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapExample()).commit();
            }
        });
        if(myPrefs.getBoolean("cb1", false)==true){
            Toast.makeText(getActivity().getApplication(), "ON", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}
