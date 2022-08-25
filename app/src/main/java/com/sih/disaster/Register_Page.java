package com.sih.disaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register_Page extends AppCompatActivity {
    EditText fullName,password,confirmationPassword,EmailID,phoneNumber,userDateofBirth;
    Button registerButton;
    TextView loginHere;
    SharedPreferences preferences;

    String[] genders = {"Male","Female","Others"};
    String[] bloodGroups = {"O+","O-","A+","A-","B+","B-","AB+","AB-"};


    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter_02;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    SharedPreferences sharedPreferences;
    String Gen = "",URL="https://technophilesapi.herokuapp.com/user/create";
    String BloodGrp = "";
    String dater = "";

    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sharedPreferences= getSharedPreferences("user",MODE_PRIVATE);
        AutoCompleteTextView autoCompleteTextView;
        AutoCompleteTextView autoCompleteTextView_02;
        //adddatatoapi();
        myAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        confirmationPassword = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        loginHere = findViewById(R.id.loginHere);
        EmailID = findViewById(R.id.userEmailID);
        phoneNumber = findViewById(R.id.userPhoneNumber);
        userDateofBirth = findViewById(R.id.userDateOfBirth);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        userDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Register_Page.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        dater = day + "/" + month + "/" + year;
                        userDateofBirth.setText(dater);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        autoCompleteTextView = findViewById(R.id.gender_Selector);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item,genders);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gen = arrayAdapter.getItem(i);
            }
        });

        autoCompleteTextView_02 = findViewById(R.id.bloodGroup);
        arrayAdapter_02 = new ArrayAdapter<String>(this,R.layout.list_item,bloodGroups);
        autoCompleteTextView_02.setAdapter(arrayAdapter_02);

        autoCompleteTextView_02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BloodGrp = arrayAdapter_02.getItem(i);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = fullName.getText().toString();
                String pass = password.getText().toString();
                String confPass = confirmationPassword.getText().toString();
                String email = EmailID.getText().toString();
                String number = phoneNumber.getText().toString();

                if (userName.isEmpty() || pass.isEmpty() || confPass.isEmpty() || email.isEmpty() || number.isEmpty() || dater.isEmpty() || Gen.isEmpty() || BloodGrp.isEmpty()){
                    Toast.makeText(Register_Page.this, "Please Enter Every Detail", Toast.LENGTH_SHORT).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Register_Page.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    EmailID.setError("Enter Valid Email");
                    EmailID.requestFocus();
                }
                else if (number.length()!=10){
                    Toast.makeText(Register_Page.this, "Mobile Number should have 10 Digits", Toast.LENGTH_SHORT).show();
                    phoneNumber.setError("Mobile Number should have 10 Digits");
                    phoneNumber.requestFocus();
                }
                else if (pass.length()<8){
                    Toast.makeText(Register_Page.this, "Password should be of atleast 8 Characters", Toast.LENGTH_SHORT).show();
                    password.setError("Password should be of atleast 8 Characters");
                    password.requestFocus();
                }
                else if (!pass.equals(confPass)){
                    Toast.makeText(Register_Page.this, "Password MisMatch!!", Toast.LENGTH_SHORT).show();
                    confirmationPassword.setError("Password Mismatch");
                    confirmationPassword.setText("");
                    confirmationPassword.requestFocus();
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(number)){
                                Toast.makeText(Register_Page.this, "User With this ID Already Registered!!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                myAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Register_Page.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                preferences.edit().putString("number",number).apply();
                                databaseReference.child("users").child(number).child("Full Name").setValue(userName);
                                databaseReference.child("users").child(number).child("Email ID").setValue(email);
                                databaseReference.child("users").child(number).child("Date of Birth").setValue(dater);
                                databaseReference.child("users").child(number).child("Gender").setValue(Gen);
                                databaseReference.child("users").child(number).child("BloodGroup").setValue(BloodGrp);
                                Toast.makeText(Register_Page.this, "Registration Successful!!", Toast.LENGTH_SHORT).show();
//                                adddatatoapi(number,userName,email,pass);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void adddatatoapi(String number, String userName, String email, String password) {
        try {

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("userName", ""+number);
            jsonBody.put("firstName",""+userName);
            jsonBody.put("lastName"," ");
            jsonBody.put("password", ""+password);
            jsonBody.put("email", ""+email);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Register_Page.this,  ""+error.toString(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();

        }
        // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

    }

}
