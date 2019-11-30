package com.example.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment implements
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnMyLocationButtonClickListener{

    MapView mapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private static final int Request_User_Location_Code = 99;
    // Database
    private FirebaseDatabase firebaseDatabase;
    // Marker
    private Marker fibomarker;
    private Marker CBmarker;
    private Marker teachermarker;
    private LatLng fibolatlng = new LatLng(13.654664460757083, 100.4945864344711);
    private LatLng teacherlatlng = new LatLng(13.653043274611724, 100.49395879756162);
    private LatLng CBlatlng = new LatLng(13.650780893328243, 100.49339553366849);
    private int yourZIndex = 1;

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
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
        setFirebaseMarker();

         mapView.getMapAsync(new OnMapReadyCallback() {
             @Override
             public void onMapReady(GoogleMap googleMap) {
                 mMap = googleMap;
                 UiSettings uiset = googleMap.getUiSettings();
                 mMap.setMaxZoomPreference(20.0f);
                 mMap.setMinZoomPreference(18.0f);
                 uiset.setZoomGesturesEnabled(true);
                 uiset.setMyLocationButtonEnabled(true);
                 //Show default location
                 mMap.setOnMyLocationButtonClickListener(HomeFragment.this);
                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.654664460757083, 100.4945864344711), 20));


                 locationRequest = new LocationRequest();
                 locationRequest.setInterval(1200);
                 locationRequest.setFastestInterval(900);
                 locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

             }
         });
        return rootView;
    }
    
    // Asked for Location Permission

    private boolean checkUserLocationPermission() {
        this.getActivity();
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions
                        (this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                                , Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions
                        (this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
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
                    if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this.getActivity(), "Permission Denied ...", Toast.LENGTH_SHORT).show();
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
        if (fusedLocationClient != null)
        {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
        return false;
    }

    public void setFirebaseMarker() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference FIBO = firebaseDatabase.getReference("Park").child("1").child("CarIn");
        final DatabaseReference CB = firebaseDatabase.getReference("Park").child("2").child("CarIn");
        final DatabaseReference Teacher = firebaseDatabase.getReference("Park").child("51").child("CarIn");


        FIBO.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                if (fibomarker != null) {
                    fibomarker.remove();
                }
                for (DataSnapshot carin : dataSnapshot.getChildren()) {
                    if (carin.getValue(int.class) == 0) {
                        count++;
                    }
                }
                MarkerOptions fibomarker = new MarkerOptions()
                        .position(fibolatlng);
                if (count == 0) {
                    fibomarker.icon(BitmapDescriptorFactory.fromResource(R.drawable._red));
                    mMap.addMarker(fibomarker);
                }
                if (count == 1) {
                    fibomarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1));
                    mMap.addMarker(fibomarker);
                }
                if (count == 2) {
                    fibomarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2));
                    mMap.addMarker(fibomarker);
                }
                if (count == 3) {
                    fibomarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3));
                    mMap.addMarker(fibomarker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Teacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count2 = 0;
                if (teachermarker != null) {
                    teachermarker.remove();
                }
                for (DataSnapshot carin : dataSnapshot.getChildren()) {
                    if (carin.getValue(int.class) == 0) {
                        count2++;
                    }
                }
                MarkerOptions teachermarker = new MarkerOptions()
                        .position(teacherlatlng);
                if (count2 == 0) {
                    teachermarker.icon(BitmapDescriptorFactory.fromResource(R.drawable._red));
                    mMap.addMarker(teachermarker);
                }
                if (count2 == 1) {
                    teachermarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1));
                    mMap.addMarker(teachermarker);
                }
                if (count2 == 2) {
                    teachermarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2));
                    mMap.addMarker(teachermarker);
                }
                if (count2 == 3) {
                    teachermarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3));
                    mMap.addMarker(teachermarker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count1 = 0;
                if (CBmarker != null) {
                    CBmarker.remove();
                }
                for (DataSnapshot carin : dataSnapshot.getChildren()) {
                    if (carin.getValue(int.class) == 0) {
                        count1++;
                    }
                }
                MarkerOptions CBmarker = new MarkerOptions()
                        .position(CBlatlng);
                if (count1 == 0) {
                    CBmarker.icon(BitmapDescriptorFactory.fromResource(R.drawable._red));
                    mMap.addMarker(CBmarker);
                }
                if (count1 == 1) {
                    CBmarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green1));
                    mMap.addMarker(CBmarker);
                }
                if (count1 == 2) {
                    CBmarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green2));
                    mMap.addMarker(CBmarker);
                }
                if (count1 == 3) {
                    CBmarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green3));
                    mMap.addMarker(CBmarker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
