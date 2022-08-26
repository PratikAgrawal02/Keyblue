package com.sih.disaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Ngo_list extends AppCompatActivity {
    ArrayList<String> namer = new ArrayList<String>();
    List<String> numbers = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    AlertDialog dialog;

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
            dialog.cancel();

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
                    numbers.add(NGOPhone);
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

        setProgressDialog();
        ListView listNGO = findViewById(R.id.ngoList);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_view,namer);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("https://technophilesapi.herokuapp.com/ngo/findAll");

        listNGO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + numbers.get(i)));
                startActivity(intent);
            }
        });

        listNGO.setAdapter(adapter);


    }
    public void back(View view){
        finish();
    }
    public void setProgressDialog() {


        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(0, 20, 20, 20);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, 60, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(18);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(ll);

        dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        Handler hdl= new Handler();
//        hdl.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.cancel();
//            }
//        },4500);
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

}