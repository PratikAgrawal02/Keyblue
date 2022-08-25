package com.sih.disaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Objects;

public class Splash_Screen extends AppCompatActivity {
    int SPLASH_SCREEN_TIME_OUT=2000;
    SharedPreferences firsttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash_screen);
        firsttime = getSharedPreferences("user",MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            public void run() {
               // Toast.makeText(Splash_Screen.this, ""+firsttime.getString("first", "no"), Toast.LENGTH_SHORT).show();
                if(Objects.equals(firsttime.getString("first", "no"), "no")){

                    Intent intent=new Intent(Splash_Screen.this,
                            MainActivity.class);
                    intent.putExtra("mode",0);
                    startActivity(intent);
                    finish();
                }
                else {
                    if(Objects.equals(firsttime.getString("id", "no"), "no")){
                        Intent intent=new Intent(Splash_Screen.this,
                                Login_Page.class);
                        startActivity(intent);
                        finish();

                    }
                    else {

                        Intent intent=new Intent(Splash_Screen.this,
                                Home.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, SPLASH_SCREEN_TIME_OUT);
        ImageView logo=(ImageView)findViewById(R.id.logo);
        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(1000);
        fade_in.setFillAfter(true);
        logo.startAnimation(fade_in);

    }
}