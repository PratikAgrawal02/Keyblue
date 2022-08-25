package com.sih.disaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Contact_Us extends AppCompatActivity {
    private EditText Name,Email,Query;
    private Button Send;
    FirebaseDatabase root;
    DatabaseReference reference;
    String name;
    int totalq=0;
    User_entry userEntry;
    public void back(View view){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Name=(EditText)findViewById(R.id.Name);
        Email=(EditText)findViewById(R.id.Email);
        Query=(EditText)findViewById(R.id.Query);

        ImageButton Back=(ImageButton)findViewById(R.id.Back);
        TextView heading=(TextView) findViewById(R.id.heading);
        TextView info=(TextView) findViewById(R.id.info);
        TextView contact=(TextView) findViewById(R.id.contact);
        TextView subheading1=(TextView) findViewById(R.id.subheading1);
        TextView subheading2=(TextView) findViewById(R.id.subheading2);
        TextView subheading3=(TextView) findViewById(R.id.subheading3);
        ImageView support=(ImageView) findViewById(R.id.support);
        root=FirebaseDatabase.getInstance();
        reference=root.getReference("query");
        userEntry=new User_entry();
        Send=findViewById(R.id.Send);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String name = Name.getText().toString();
                String email = Email.getText().toString();
                String query = Query.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(query)) {
                    Toast.makeText(Contact_Us.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(name,email,query);
                }
            }
        });
        reference.child("totalq").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalq = snapshot.getValue(Integer.class);
               // Toast.makeText(Contact_Us.this, ""+String.valueOf(totalq), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addDatatoFirebase(String name, String email, String query) {
        userEntry.setName(name);
        userEntry.setEmail(email);
        userEntry.setQuery(query);
        reference.child(""+String.valueOf(totalq+1)).setValue(userEntry);
        Toast.makeText(Contact_Us.this, "Query added", Toast.LENGTH_SHORT).show();
        clearall();
        reference.child("totalq").setValue(totalq+1);
        totalq++;

    }

    private void clearall() {
        Name.setText("");
        Email.setText("");
        Query.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}