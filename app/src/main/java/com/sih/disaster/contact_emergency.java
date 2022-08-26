package com.sih.disaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class contact_emergency extends AppCompatActivity {
    ListView listView;

    String[] names = {"NATIONAL EMERGENCY NUMBER","RAILWAY ACCIDENT EMERGENCY","ROAD ACCIDENT EMERGENCY","NATIONAL HIGHWAY ROAD ACCIDENT","DISASTER MANAGEMENT SERVICES", "AMBULANCE", "POLICE DEPARTMENT","FIRE DEPARTMENT","LPG LEAK HELPLINE"};
    String[] numbers = {"112","1072","1073","1033","108","102","100","101","1906"};
    int image = R.drawable.ic_baseline_call_24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_emergency);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        listView = findViewById(R.id.emergency);
        adapter ada = new adapter(getApplicationContext(),names,image);
        listView.setAdapter(ada);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (numbers[i].equals("112")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:112"));
                    startActivity(intent_01);
                }
                if (numbers[i].equals("1072")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:1072"));
                    startActivity(intent_01);
                }
                if (numbers[i].equals("1073")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:1073"));
                    startActivity(intent_01);
                }
                if (numbers[i].equals("1033")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:1033"));
                    startActivity(intent_01);
                }
                if (numbers[i].equals("108")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:108"));
                    startActivity(intent_01);
                }
                if (numbers[i].equals("102")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:102"));
                    startActivity(intent_01);
                }
                if (numbers[i].equals("100")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:100"));
                    startActivity(intent_01);
                }
                if (numbers[i].equals("101")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:101"));
                    startActivity(intent_01);
                }
                if (numbers[i].equals("1906")){
                    Intent intent_01 = new Intent(Intent.ACTION_DIAL);
                    intent_01.setData(Uri.parse("tel:1906"));
                    startActivity(intent_01);
                }
            }
        });


    }
    public void back(View view){
        finish();
    }

}