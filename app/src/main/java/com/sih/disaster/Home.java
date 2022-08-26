package com.sih.disaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.Objects;


public class Home extends AppCompatActivity  implements OnMapReadyCallback{

    ImageView red_blink;
    Boolean disastermode=false;
    FirebaseDatabase root;
    SupportMapFragment mapFragment;
    DatabaseReference reference,unsafe=FirebaseDatabase.getInstance().getReference("unsafe");;
    DrawerLayout drawerLayout;
    FusedLocationProviderClient client;
    TextView ngo, deaths, disaster,ranger,user;
    Button dismode;
    Location livelocation;
    RadiusAnimation groundAnimation;
    TextView news;
    SharedPreferences preferences;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        root=FirebaseDatabase.getInstance();
        hook();
        preferences = getSharedPreferences("user",MODE_PRIVATE);
        //Button Onclick Listeners to Contact Them
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        reference=root.getReference("disaster");
        client = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
//        livedataget();

        loadpage();
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                44);
        reference.child("mode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(Boolean.class)){
                    //Disaster mode is ON
                    dismode.setAlpha(1f);

                }
                else {
                    dismode.setAlpha(0f);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                news.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        news = (TextView)findViewById(R.id.news);
        news.setSelected(true);

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.faq_menu:{
                        Intent intent=new Intent(Home.this,
                                Faq.class);
                        startActivity(intent);

                        break;
                    }
                    case R.id.Home_menu: {

                        break;
                    }
                    case R.id.ngo_list:{
                        Intent intent=new Intent(Home.this,
                                Ngo_list.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Contactus_menu: {
                        Intent intent=new Intent(Home.this,
                                Contact_Us.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.developer_menu: {
                        Intent intent=new Intent(Home.this,
                                About_Us.class);
                        startActivity(intent);

                        break;
                    }
                    case R.id.privacy_policy_menu: {

                        break;
                    }case R.id.emergency: {
                        Intent intent=new Intent(Home.this,
                                contact_emergency.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.TnC_menu: {

                        break;
                    }
                    case R.id.share_app_menu: {

                        break;
                    }
                    case R.id.rate: {


                        break;
                    }
                    default: return true;

                }

                return true;
            }
        });

       // final LayoutInflater factory = getLayoutInflater();
        final View header = getLayoutInflater().inflate(R.layout.headerfile,null);
        ImageView insta =  (ImageView) header.findViewById(R.id.instaim);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "insta", Toast.LENGTH_SHORT).show();
            }
        });
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

        getlivelocation();
    } else {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                44);
    }

    }
    public void getlivelocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    mapFragment.getMapAsync(Home.this);

                    //Toast.makeText(MapsActivity.this, ""+location.toString(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Home.this, "location not got", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void openillus(View view){
        //Toast.makeText(this, "coming soon..", Toast.LENGTH_SHORT).show();
        CardView cclicked = (CardView) view;
        String tag = cclicked.getTag().toString();
        Intent intent=new Intent(Home.this, MainActivity.class);
        if(tag.equals("earthquake")){
            intent.putExtra("mode",1);
            startActivity(intent);

        }
        if(tag.equals("flood")){
            intent.putExtra("mode",3);
            startActivity(intent);

        }
        if(tag.equals("landslide")){
            intent.putExtra("mode",2);
            startActivity(intent);

        }
        if(tag.equals("thunderstorm")){
            intent.putExtra("mode",4);
            startActivity(intent);

        }

    }

    public void dismodeclick(View view){
        Intent intent=new Intent(Home.this,
                MapsActivity.class);
        startActivity(intent);

    }
    private void loadpage() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(Home.this);
        View loading = getLayoutInflater().inflate(R.layout.loading,null);
        mbuilder.setView(loading);
        AlertDialog loading_dialog = mbuilder.create();
        loading_dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        loading_dialog.show();
        ImageView loading_image= (ImageView) loading_dialog.findViewById(R.id.loading_image);
        loading_image.animate().rotationBy(1800).setDuration(5000).start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                loading_dialog.cancel();
            }
        },5000);
    }

    public void refresh(View view){
        ImageView refresh_image= (ImageView) view;
        refresh_image.animate().setDuration(1500).rotation(360).start();
        //Toast.makeText(this, "refreshed", Toast.LENGTH_SHORT).show();
        loadpage();
    }
    public void opendrawer(View view){
        drawerLayout.openDrawer(GravityCompat.START);

    }
    public void unsafe(View view){
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        unsafe.child(preferences.getString("number","default")).child("lat").setValue(livelocation.getLatitude());
        unsafe.child(preferences.getString("number","default")).child("long").setValue(livelocation.getLongitude());
        unsafe.child(preferences.getString("number","default")).child("verified").setValue("no");

    }

    private void hook() {
        red_blink= (ImageView) findViewById(R.id.red_blink);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        red_blink.startAnimation(animation1);
         dismode = (Button)findViewById(R.id.dismode);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView= (NavigationView)findViewById(R.id.navigationlayout);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng =  new LatLng(livelocation.getLatitude(),livelocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Your current Location").icon(BitmapDescriptorFactory.defaultMarker(205))).setTag("curr");

        GroundOverlayOptions groundOverlayOptions= new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.blue_dot))
                .position( latLng,50);

        GroundOverlay groundOverlay = googleMap.addGroundOverlay(groundOverlayOptions);
        groundAnimation = new RadiusAnimation(groundOverlay);
        groundAnimation.setRepeatCount(Animation.INFINITE);
        groundAnimation.setRepeatMode(Animation.RESTART);
        groundAnimation.setDuration(2000);
        mapFragment.getView().startAnimation(groundAnimation);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
    }
}