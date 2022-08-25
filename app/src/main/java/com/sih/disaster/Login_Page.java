package com.sih.disaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Page extends AppCompatActivity {
    EditText emailID,password;
    Button loginButton;
    TextView registerHere;

    SharedPreferences preferences;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("user",MODE_PRIVATE);
        emailID = findViewById(R.id.userID);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerHere = findViewById(R.id.register);

        myAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = emailID.getText().toString();
                String pass = password.getText().toString();
                
                if (ID.isEmpty() || pass.isEmpty()){
                    Toast.makeText(Login_Page.this, "Please Enter The UserID or Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    myAuth.signInWithEmailAndPassword(ID,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(Login_Page.this, "Logged in!!", Toast.LENGTH_SHORT).show();
                                preferences.edit().putString("id",""+ID).apply();
                                startActivity(new Intent(Login_Page.this,Home.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Login_Page.this, "Login Error!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Page.this,Register_Page.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}