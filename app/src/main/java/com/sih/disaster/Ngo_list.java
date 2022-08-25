package com.sih.disaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Ngo_list extends AppCompatActivity {
    ArrayList<String> namer = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int reader = inputStreamReader.read();

                while (reader != -1){
                    char current = (char) reader;
                    result += current;
                    reader = inputStreamReader.read();
                }
                return result;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String chooser = jsonObject.getString("col");
                JSONArray array = new JSONArray(chooser);
                for (int i = 0; i < array.length(); i++){
                    JSONObject jName = array.getJSONObject(i);

                    String resNGO = "";
                    String name_NGO = jName.getString("name");
                    resNGO += "Name : " +  name_NGO + "\n";

                    String addNGO = jName.getString("address");
                    JSONObject nameandall = new JSONObject(addNGO);
                    String vil = nameandall.getString("state");
                    resNGO += ".\tState : " +  vil + ".\n";

                    vil = nameandall.getString("city_village");
                    resNGO += ".\tCity/Village : " +  vil + ".\n";

                    String NGOPhone = jName.getString("phone");
                    resNGO += ".\tMobile Number : " +  NGOPhone + ".\n";

                    String NGOemail = jName.getString("email");
                    resNGO += ".\tEmail ID : " +  NGOemail + ".\n";

                    namer.add(resNGO);
                    adapter.notifyDataSetChanged();
//                    Log.i("NGO", String.valueOf(namer));
                }
//                Log.i("NGO", String.valueOf(namer));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_list);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ListView listNGO = findViewById(R.id.ngoList);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_view,namer);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("https://technophilesapi.herokuapp.com/ngo/findAll");

        listNGO.setAdapter(adapter);
    }
}