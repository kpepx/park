package com.example.test;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Fragment;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class HomeFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener {

    MapView mapView;
    public GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private static final int Request_User_Location_Code = 99;
    boolean dark;
    // Database
    private FirebaseDatabase firebaseDatabase;
    // Marker
    private Marker fibomarker;
    private Marker CBmarker;
    private Marker teachermarker;
    private int FIBO_value_hour;
    private int FIBO_value_min;
    private int CB_value_hour;
    private int CB_value_min;
    private int teacher_value_hour;
    private int teacher_value_min;
    private int FIBO_value_hour1;
    private int FIBO_value_min1;
    private int CB_value_hour1;
    private int CB_value_min1;
    private int teacher_value_hour1;
    private int teacher_value_min1;
    private LatLng fibolatlng = new LatLng(13.654664460757083, 100.4945864344711);
    private LatLng teacherlatlng = new LatLng(13.653043274611724, 100.49395879756162);
    private LatLng CBlatlng = new LatLng(13.650780893328243, 100.49339553366849);
    private int yourZIndex = 1;
    SharedPreferences myPrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());

        }catch (Exception e){
            e.printStackTrace();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

        checkUserLocationPermission();

         mapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(20.0f);
        mMap.setMinZoomPreference(18.0f);
        mMap.setOnMarkerClickListener(this);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
            setFirebaseMarker();
        }

        // Dark mode enable
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        boolean mode = myPrefs.getBoolean("togglebutton",true);
        if(mode){
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.darkmode));
        }

//                 UiSettings uiset = googleMap.getUiSettings();
//                 uiset.setZoomGesturesEnabled(true);
//                 uiset.setMyLocationButtonEnabled(true)
        //Show default location
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnMyLocationButtonClickListener(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1200);
        locationRequest.setFastestInterval(900);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    // Asked for Location Permission
    private boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale
                    (Manifest.permission.ACCESS_FINE_LOCATION)) {
                        requestPermissions
                        (new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                , Request_User_Location_Code);
            } else {
                requestPermissions
                        (new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                , Request_User_Location_Code);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                        setFirebaseMarker();
//
//                        new CountDownTimer(1000, 1000) {
//                            public void onFinish() {
//                                // When timer is finished
//                                // Execute your code here
//                            }
//
//                            public void onTick(long millisUntilFinished) {
//                                // millisUntilFinished    The amount of time until finished.
//                            }
//                        }.start();
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied ...", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // Location change
    LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location: locationResult.getLocations()){
                lastLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    };

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        return false;
    }

    public void setFirebaseMarker() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference FIBO = firebaseDatabase.getReference("Park").child("1").child("CarIn");
        final DatabaseReference CB = firebaseDatabase.getReference("Park").child("2").child("CarIn");
        final DatabaseReference Teacher = firebaseDatabase.getReference("Park").child("51").child("CarIn");

        DatabaseReference Close_time = firebaseDatabase.getReference().child("Park");
        Close_time.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot park : dataSnapshot.getChildren()) {
                    if (park.toString().equals("1")) {
                        Map map = (Map) park.child("close").getValue();
                        FIBO_value_hour = Integer.parseInt(String.valueOf(map.get("hour")));
                        FIBO_value_min = Integer.parseInt(String.valueOf(map.get("min")));
                    }
                    if (park.toString().equals("1")) {
                        Map map = (Map) park.child("close").getValue();
                        CB_value_hour = Integer.parseInt(String.valueOf(map.get("hour")));
                        CB_value_min = Integer.parseInt(String.valueOf(map.get("min")));
                    }
                    if (park.toString().equals("1")) {
                        Map map = (Map) park.child("close").getValue();
                        teacher_value_hour = Integer.parseInt(String.valueOf(map.get("hour")));
                        teacher_value_min = Integer.parseInt(String.valueOf(map.get("min")));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference Open_time = firebaseDatabase.getReference().child("Park");
        Open_time.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot park : dataSnapshot.getChildren()) {
                    if (park.toString() == "1") {
                        Map map = (Map) park.child("open").getValue();
                        FIBO_value_hour1 = Integer.parseInt(String.valueOf(map.get("hour")));
                        FIBO_value_min1 = Integer.parseInt(String.valueOf(map.get("min")));
                    }
                    if (park.toString() == "1") {
                        Map map = (Map) park.child("open").getValue();
                        CB_value_hour1 = Integer.parseInt(String.valueOf(map.get("hour")));
                        CB_value_min1 = Integer.parseInt(String.valueOf(map.get("min")));
                    }
                    if (park.toString() == "1") {
                        Map map = (Map) park.child("open").getValue();
                        teacher_value_hour1 = Integer.parseInt(String.valueOf(map.get("hour")));
                        teacher_value_min1 = Integer.parseInt(String.valueOf(map.get("min")));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            final Calendar currentTime = Calendar.getInstance();

        FIBO.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                if (fibomarker != null) {
                    fibomarker.remove();
                }
                if(currentTime.get(Calendar.HOUR_OF_DAY)< FIBO_value_hour && currentTime.get(Calendar.HOUR_OF_DAY) > FIBO_value_hour1) {
                    for (DataSnapshot carin : dataSnapshot.getChildren()) {
                        if (carin.getValue(int.class) == 0) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                    }
                    if (count == 1) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                    }
                    if (count == 2) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                    }
                    if (count == 3) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                    }
                }
                if(currentTime.get(Calendar.HOUR_OF_DAY) == FIBO_value_hour && currentTime.get(Calendar.MINUTE)< FIBO_value_min) {
                    for (DataSnapshot carin : dataSnapshot.getChildren()) {
                        if (carin.getValue(int.class) == 0) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                    }
                    if (count == 1) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                    }
                    if (count == 2) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                    }
                    if (count == 3) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                    }
                }
                if(currentTime.get(Calendar.HOUR_OF_DAY) == FIBO_value_hour1 && currentTime.get(Calendar.MINUTE) >= FIBO_value_min1){
                    for (DataSnapshot carin : dataSnapshot.getChildren()) {
                        if (carin.getValue(int.class) == 0) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                    }
                    if (count == 1) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                    }
                    if (count == 2) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                    }
                    if (count == 3) {
                        fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                    }
                }
                else {
                    fibomarker = mMap.addMarker(new MarkerOptions().position(fibolatlng).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gray)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        String stickk = myPrefs.getString("sticker","Default");
        if(stickk.equals("teacher")){
            Teacher.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count2 = 0;
                    if (teachermarker != null) {
                        teachermarker.remove();
                    }
                    if(currentTime.get(Calendar.HOUR_OF_DAY)< teacher_value_hour && currentTime.get(Calendar.HOUR_OF_DAY) > teacher_value_hour1){
                        for (DataSnapshot carin : dataSnapshot.getChildren()) {
                            if (carin.getValue(int.class) == 0) {
                                count2++;
                            }
                        }
                        if (count2 == 0) {
                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                        }
                        if (count2 == 1) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                        }
                        if (count2 == 2) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                        }
                        if (count2 == 3) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                        }
                    }
                    if(currentTime.get(Calendar.HOUR_OF_DAY)== teacher_value_hour && currentTime.get(Calendar.MINUTE)< teacher_value_min ){
                        for (DataSnapshot carin : dataSnapshot.getChildren()) {
                            if (carin.getValue(int.class) == 0) {
                                count2++;
                            }
                        }
                        if (count2 == 0) {
                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                        }
                        if (count2 == 1) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                        }
                        if (count2 == 2) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                        }
                        if (count2 == 3) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                        }
                    }
                    if(currentTime.get(Calendar.HOUR_OF_DAY)== teacher_value_hour1&& currentTime.get(Calendar.MINUTE) >= teacher_value_min1){
                        for (DataSnapshot carin : dataSnapshot.getChildren()) {
                            if (carin.getValue(int.class) == 0) {
                                count2++;
                            }
                        }
                        if (count2 == 0) {
                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                        }
                        if (count2 == 1) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                        }
                        if (count2 == 2) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                        }
                        if (count2 == 3) {

                            teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                        }
                    }
                    else {
                        teachermarker = mMap.addMarker(new MarkerOptions().position(teacherlatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gray)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        CB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count1 = 0;
                if (CBmarker != null) {
                    CBmarker.remove();
                }

                if (currentTime.get(Calendar.HOUR_OF_DAY) < CB_value_hour && currentTime.get(Calendar.HOUR_OF_DAY) >= CB_value_hour1) {
                    for (DataSnapshot carin : dataSnapshot.getChildren()) {
                        if (carin.getValue(int.class) == 0) {
                            count1++;
                        }
                    }
                    if (count1 == 0) {
                        CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                    }
                    if (count1 == 1) {
                        CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                    }
                    if (count1 == 2) {
                        CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                    }
                    if (count1 == 3) {
                        CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                    }
                }
                if (currentTime.get(Calendar.HOUR_OF_DAY) == CB_value_hour && currentTime.get(Calendar.MINUTE) < CB_value_min) {
                            for (DataSnapshot carin : dataSnapshot.getChildren()) {
                                if (carin.getValue(int.class) == 0) {
                                    count1++;
                                }
                            }
                            if (count1 == 0) {
                                CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                            }
                            if (count1 == 1) {
                                CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                            }
                            if (count1 == 2) {
                                CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                            }
                            if (count1 == 3) {
                                CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                            }
                        }
                if (currentTime.get(Calendar.HOUR_OF_DAY) == CB_value_hour && currentTime.get(Calendar.MINUTE) > CB_value_min1) {
                            for (DataSnapshot carin : dataSnapshot.getChildren()) {
                                if (carin.getValue(int.class) == 0) {
                                    count1++;
                                }
                            }
                            if (count1 == 0) {
                                CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable._red)));
                            }
                            if (count1 == 1) {
                                CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1)));
                            }
                            if (count1 == 2) {
                                CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2)));
                            }
                            if (count1 == 3) {
                                CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3)));
                            }
                        }
                else {
                    CBmarker = mMap.addMarker(new MarkerOptions().position(CBlatlng).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gray)));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        myPrefs = this.getActivity().getSharedPreferences("ID", 0);
        if(marker.equals(fibomarker))
        {
            Toast.makeText(getActivity(), "Fibo", Toast.LENGTH_SHORT).show();
            myPrefs.edit().putInt("MapClick", 1).apply();
        }
        if(marker.equals(teachermarker))
        {
            Toast.makeText(getActivity(), "teacher", Toast.LENGTH_SHORT).show();
            myPrefs.edit().putInt("MapClick", 51).apply();
        }
        if(marker.equals(CBmarker))
        {
            Toast.makeText(getActivity(), "CB", Toast.LENGTH_SHORT).show();
            myPrefs.edit().putInt("MapClick", 2).apply();
        }
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MapParking()).commit();
        return false;
    }
}

