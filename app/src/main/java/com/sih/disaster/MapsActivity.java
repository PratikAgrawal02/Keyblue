package com.sih.disaster;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sih.disaster.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    Location livelocation;
    FirebaseDatabase database;
    Double longi=0.0,lati=0.0;
    TextView safetext;
    ImageView red_blink;
    String namelocation,URL="https://technophilesapi.up.railway.app/ngo/findAll";
    DatabaseReference databaseReference,unsafe=FirebaseDatabase.getInstance().getReference("unsafe");
    RadiusAnimation groundAnimation;
    Disasteranimation disasteranim;
    LatLng latLng,currlocation,dirn;

    SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);


     int camerapos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        red_blink= (ImageView) findViewById(R.id.red_blinkd);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        red_blink.startAnimation(animation1);



        getlistofngo();


        //initialize textveiw
        safetext =(TextView)findViewById(R.id.safetext);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);


        getLocationPermission();


        database = FirebaseDatabase.getInstance();
        databaseReference= database.getReference("disaster");
        disastermodefirebase();

    }

    public void disastermodefirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Toast.makeText(MapsActivity.this, ""+snapshot.toString(), Toast.LENGTH_LONG).show();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(Objects.equals(dataSnapshot.getKey(), "long")){
                        longi=dataSnapshot.getValue(Double.class);
                    }
                    else if(Objects.equals(dataSnapshot.getKey(), "lat")){
                        lati= dataSnapshot.getValue(Double.class);
                    }
                    else if(Objects.equals(dataSnapshot.getKey(), "name")){
                        namelocation= dataSnapshot.getValue(String.class);
                    }


                }
                if(lati!=null) {
                    //Toast.makeText(MapsActivity.this, ""+lati.toString(), Toast.LENGTH_SHORT).show();
                    adddisasterloaction(lati, longi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getlistofngo(){
        StringRequest stringRequest= new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  Toast.makeText(MapsActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("col");
                    if(jsonArray.length()>0){
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("location");

                            String lat1= jsonObject1.getString("lat"),longi1 = jsonObject1.getString("long"),name= jsonArray.getJSONObject(i).getString("name");
                            addngolocation(Double.parseDouble(lat1),Double.parseDouble(longi1),name);
                            //Toast.makeText(MapsActivity.this, ""+jsonArray.getJSONObject(i).getString("name"), Toast.LENGTH_SHORT).show();

                        }
                    }
                    else {
                        Toast.makeText(MapsActivity.this, "finding ngos..", Toast.LENGTH_SHORT).show();
                        getlistofngo();
                    }
                }catch (Exception e){

                    Toast.makeText(MapsActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            getlivelocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    44);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == 44) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        getlivelocation();
    }

    public void getlivelocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Toast.makeText(this, "no permissions", Toast.LENGTH_SHORT).show();
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    livelocation=location;
                    mapFragment.getMapAsync(MapsActivity.this);

                    //Toast.makeText(MapsActivity.this, ""+location.toString(), Toast.LENGTH_SHORT).show();
                }
                else { 
                    Toast.makeText(MapsActivity.this, "location not got", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void addngolocation(Double lati, Double longi, String name){

        LatLng ngolatLng = new LatLng(lati,longi);
        mMap.addMarker(new MarkerOptions().position(ngolatLng).title(""+name).icon(BitmapDescriptorFactory.defaultMarker(270)));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(ngolatLng));

    }
    public void adddisasterloaction(Double lati, Double longi){
        try {
            latLng = new LatLng(lati,longi);

            GroundOverlayOptions groundOverlayOptions= new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.red_deot))
                    .position( latLng,500);
          //  Toast.makeText(this, ""+groundOverlayOptions.toString(), Toast.LENGTH_SHORT).show();
            mMap.addMarker(new MarkerOptions().position(latLng).title(""+namelocation));
            double distinmile = distance(lati,longi,livelocation.getLatitude(),livelocation.getLongitude())*1.609;
            if(distinmile>=3.8616){//Safe condition
                safetext.setTextColor(getResources().getColor(R.color.green));
                safetext.setText("You Are safe\nEpicenter: "+String.valueOf((int)distinmile)+" km away");
            }
            else {//Unsafe condition
//                unsafe.child().setValue()
                safetext.setTextColor(getResources().getColor(R.color.red));
                safetext.setText("You Are not safe\nEpicenter: "+String.valueOf((int)distinmile)+" km away");
            }
            try {
                GroundOverlay groundOverlay = mMap.addGroundOverlay(groundOverlayOptions);
                disasteranim = new Disasteranimation(groundOverlay);
                disasteranim.setRepeatCount(Animation.INFINITE);
                disasteranim.setRepeatMode(Animation.RESTART);
                disasteranim.setDuration(2000);
            }
            catch (Exception e){
                Log.i("disloc",e.toString());
            }

            //Toast.makeText(this, ""+String.valueOf(distinmile), Toast.LENGTH_SHORT).show();
            // mapFragment.getView().startAnimation(disasteranim);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12f));


            //2.4 miles minimum

        }
        catch (Exception e){
            Log.i("error",e.toString());


        }
    }


    Boolean locationPermissionGranted;

    public void changecamera(View view){
        Button button = (Button) view;
        //0 means your location
        if(latLng == null){
            disastermodefirebase();
        }
        else {
            if(camerapos==0){
                button.setText("Your Location");
                mapFragment.getView().startAnimation(disasteranim);
                groundAnimation.cancel();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng),3000,null);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12f),4000,null);
                camerapos = 1;
                button.setBackgroundColor(getResources().getColor(R.color.red));
            }
            else {
                button.setText("Disaster Zone");
                disasteranim.cancel();
                mapFragment.getView().startAnimation(groundAnimation);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(currlocation),3000,null);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currlocation,12f),4000,null);
                camerapos = 0;
                button.setBackgroundColor(getResources().getColor(R.color.green));


            }
        }


    }

    public void directionshow(View view){
        if(dirn!=null){
            try {
                Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+String.valueOf(dirn.latitude)+","+String.valueOf(dirn.longitude)+"&origin="+String.valueOf(currlocation.latitude)+","+String.valueOf(currlocation.longitude));
                // Toast.makeText(this, ""+uri.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//intent.setPackage("com.google.apps.maps");
                startActivity(intent);
            }catch (Exception e){
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        currlocation = new LatLng(livelocation.getLatitude(),livelocation.getLongitude());



       mMap.addMarker(new MarkerOptions().position(currlocation).title("Your current Location").icon(BitmapDescriptorFactory.defaultMarker(205))).setTag("curr");

       // Bitmap bitmap = BitmapFactory.decodeResource(getResources(R.drawable.blue_circle));


        GroundOverlayOptions groundOverlayOptions= new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.blue_dot))
                .position( currlocation,50);


        GroundOverlay groundOverlay = mMap.addGroundOverlay(groundOverlayOptions);
       // mMap.addGroundOverlay(groundOverlayOptions);
        groundAnimation = new RadiusAnimation(groundOverlay);
        groundAnimation.setRepeatCount(Animation.INFINITE);
        groundAnimation.setRepeatMode(Animation.RESTART);
        groundAnimation.setDuration(2000);

        mapFragment.getView().startAnimation(groundAnimation);
       mMap.moveCamera(CameraUpdateFactory.newLatLng(currlocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currlocation,12f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //Toast.makeText(MapsActivity.this, ""+marker.getTag(), Toast.LENGTH_SHORT).show();
                dirn=new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                return false;
            }

        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(@NonNull CameraPosition cameraPosition) {
               // Toast.makeText(MapsActivity.this, ""+cameraPosition.toString(), Toast.LENGTH_SHORT).show();
                if(camerapos==1){
                    if(cameraPosition.zoom>=14.5 ){
                        disasteranim.cancel();


                    }
                    else{
                        mapFragment.getView().startAnimation(disasteranim);
                    }
                }


            }
        });






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}