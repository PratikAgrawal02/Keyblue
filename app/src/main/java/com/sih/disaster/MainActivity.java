package com.sih.disaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

//https://codecanyon.net/item/2-app-templates-docotor-appointment-flutter-ui-template-medico-practo/38503949
public class MainActivity extends AppCompatActivity {

    int selector=3, pages=3;

    private ViewPager slideview;
    private LinearLayout Dotlayout;
    private TextView[] mDots;
    private int currpage=0;
    private Button next ,back;
    private SlideAdapter slideAdapter;
    private Earthquake earthquake;
    private Landslides landslides;
    SharedPreferences preferences;
    private Flood flood;

    private Thunderstorm thunderstorm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("user",MODE_PRIVATE);

        selector = getIntent().getExtras().getInt("mode");
        infoslider();

    }

    public void infoslider(){

        //hook
        next = (Button)findViewById(R.id.nextslide);
        back= (Button) findViewById(R.id.prevslid);
        slideview= (ViewPager) findViewById(R.id.viewpager);
        Dotlayout = (LinearLayout) findViewById(R.id.dotlayout);

        //Setting adapter
        if(selector==0) {
            pages=3;

            slideAdapter = new SlideAdapter(this);
            slideview.setAdapter(slideAdapter);
        }
        else if(selector==1){
            pages=3;
            earthquake = new Earthquake(this);
            slideview.setAdapter(earthquake);
        }
        else if(selector==2){
            pages=3;
            landslides= new Landslides(this);
            slideview.setAdapter(landslides);
        }
        else if(selector==3){
            pages=5;
            flood = new Flood(this);
            slideview.setAdapter(flood);
        }
        else if(selector==4){
            pages=5;
            thunderstorm = new Thunderstorm(this);
            slideview.setAdapter(thunderstorm);
        }


        addDotsIndicator(0);
        slideview.addOnPageChangeListener(viewListner);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(next.getText() !="Finish")
                slideview.setCurrentItem(currpage+1);
                else {
                    if(selector==0) {
                        preferences.edit().putString("first","yes").apply();
                        if(Objects.equals(preferences.getString("id", "no"), "no")){
                            Intent intent=new Intent(MainActivity.this,
                                    Login_Page.class);
                            startActivity(intent);
                            finish();

                        }
                        else {

                            Intent intent=new Intent(MainActivity.this,
                                    Home.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        //startActivity(new Intent(MainActivity.this, Home.class));
                        finish();
                    }

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideview.setCurrentItem(currpage-1);
            }
        });
    }
    public void addDotsIndicator(int position){
        mDots = new TextView[pages];
        Dotlayout.removeAllViews();
        for(int i=0;i<mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.black));
            Dotlayout.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.grey));
        }
    }
    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currpage=position;

            if(position==0){
                next.setEnabled(true);
                back.setEnabled(false);
                back.setVisibility(View.INVISIBLE);
                next.setText("Next");
            }
            else if(position==mDots.length-1){
             //   next.setEnabled(false);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);
                next.setText("Finish");

            }
            else {
                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);
                next.setText("Next");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {


    }
}