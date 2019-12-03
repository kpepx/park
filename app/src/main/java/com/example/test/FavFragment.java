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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FavFragment extends Fragment {
    SharedPreferences myPrefs;
    int count = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav,container,false);
        final Button image_1 = (Button) view.findViewById(R.id.buttonmap1);
        final TextView txt_1 = (TextView) view.findViewById(R.id.maptext1);
        final Button image_2 = (Button) view.findViewById(R.id.buttonmap2);
        final TextView txt_2 = (TextView) view.findViewById(R.id.maptext2);
        final Button image_3 = (Button) view.findViewById(R.id.buttonmap3);
        final TextView txt_3 = (TextView) view.findViewById(R.id.maptext3);
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        Button add = (Button)view.findViewById(R.id.addbutton);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Add()).commit();
            }
        });

        if(myPrefs.getBoolean("cb1", false) == true){count+=1;}
        if(myPrefs.getBoolean("cb2", false) == true){count+=1;}
        if(myPrefs.getBoolean("cb3", false) == true){count+=1;}
        if(count==0){
            image_1.setVisibility(View.GONE);
            image_2.setVisibility(View.GONE);
            image_3.setVisibility(View.GONE);
            txt_1.setVisibility(View.GONE);
            txt_2.setVisibility(View.GONE);
            txt_3.setVisibility(View.GONE);
        }
        else if(count==1){
            if(myPrefs.getBoolean("cb1", false) == true){
                Button map = (Button)view.findViewById(R.id.buttonmap1);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 2).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                txt_1.setText("CB1");
            }
            else if(myPrefs.getBoolean("cb2", false) == true){
                Button map = (Button)view.findViewById(R.id.buttonmap1);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 1).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                txt_1.setText("FIBO");
            }
            else if(myPrefs.getBoolean("cb3", false) == true){
                Button map = (Button)view.findViewById(R.id.buttonmap1);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 51).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                txt_1.setText("TEACHER");
            }
            txt_2.setVisibility(View.GONE);
            txt_3.setVisibility(View.GONE);
            image_2.setVisibility(View.GONE);
            image_3.setVisibility(View.GONE);
        }
        else if(count==2){
            if(myPrefs.getBoolean("cb1", false) == false){
                Button map = (Button)view.findViewById(R.id.buttonmap1);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 1).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                Button map2 = (Button)view.findViewById(R.id.buttonmap2);
                map2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 51).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                txt_1.setText("FIBO");
                txt_2.setText("TEACHER");
            }
            if(myPrefs.getBoolean("cb2", false) == false){
                Button map = (Button)view.findViewById(R.id.buttonmap1);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 2).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                Button map2 = (Button)view.findViewById(R.id.buttonmap2);
                map2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 51).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                txt_1.setText("CB1");
                txt_2.setText("TEACHER");
            }
            if(myPrefs.getBoolean("cb3", false) == false){
                Button map = (Button)view.findViewById(R.id.buttonmap1);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 2).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                Button map2 = (Button)view.findViewById(R.id.buttonmap2);
                map2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 1).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
                txt_2.setText("FIBO");
                txt_1.setText("CB1");
            }
            txt_3.setVisibility(View.GONE);
            image_3.setVisibility(View.GONE);
        }
        else{
            if(myPrefs.getBoolean("cb1", false) == true){
                Button map = (Button)view.findViewById(R.id.buttonmap1);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 2).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
            }
            if(myPrefs.getBoolean("cb2", false) == true){
                Button map = (Button)view.findViewById(R.id.buttonmap2);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 1).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
            }
            if(myPrefs.getBoolean("cb3", false) == true){
                Button map = (Button)view.findViewById(R.id.buttonmap3);
                map.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        myPrefs.edit().putInt("MapClick", 51).apply();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    }
                });
            }
            txt_1.setText("CB1");
            txt_2.setText("FIBO");
            txt_3.setText("TEACHER");
        }
        return view;
    }
}
