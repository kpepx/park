package com.example.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.app.Fragment;
import java.util.Map;


public class Third extends FragmentActivity {
    public DatabaseReference data;
    public DatabaseReference dat;
    SharedPreferences myPrefs;
    public Fragment back_map;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_page);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(NavListener);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit(); // setให้เปิดมาละเป็นหน้าhome
        back_map = new HomeFragment();
        myPrefs = getSharedPreferences("ID", 0);
        final String rfid = myPrefs.getString("rfid","Default");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        data = database.getReference().child("User").child(rfid);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map map = (Map) dataSnapshot.getValue();
                    String value_status = String.valueOf(map.get("status"));
                    if (value_status.equals("in")) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        data = database.getReference().child("User").child(rfid);
                        data.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map map = (Map) dataSnapshot.getValue();
                                String value_place = String.valueOf(map.get("place"));
                                String value_park = String.valueOf(map.get("park"));
                                myPrefs.edit().putInt("MapClick", Integer.parseInt(value_place)).apply();
                                myPrefs.edit().putInt("park", Integer.parseInt(value_park)).apply();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new MapParking()).commit();
                    } else {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                back_map).commit();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener NavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_fav:
                            selectedFragment = new FavFragment();
                            break;
                        case R.id.nav_set:
                            selectedFragment = new SettingFragment();
                            break;
                    }
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    back_map = selectedFragment;
                    return true;
                }
            };
}
