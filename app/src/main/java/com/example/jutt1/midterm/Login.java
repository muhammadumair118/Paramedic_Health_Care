package com.example.jutt1.midterm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jutt1.midterm.Adapters.PersonAdapter;
import com.example.jutt1.midterm.PersonModel.PersonModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login, register;


    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.user);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.registerParient);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidation();
            }
        });
    }

    private void CheckValidation() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Email)){
            email.setError("Please enter Email");
        }
        else if (!(Patterns.EMAIL_ADDRESS.matcher(Email).matches())){
            email.setError("Please Enter Valid Email");
        }
        else if (TextUtils.isEmpty(Password)){
            password.setError("Please Enter Password");
        }
        else{
            PerformAuthentication(Email,Password);
        }
    }

    private void PerformAuthentication(final String email, final String password) {
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            SharedPreferences sharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("EMAIL", email);
                            editor.putString("PASSWORD", password);
                            editor.apply();
                            editor.commit();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            reference.child("DoctorsTable")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                String Email = snapshot.child("email").getValue().toString();
                                                String Password = snapshot.child("password").getValue().toString();

                                                if (Email.matches(email) && Password.matches(password)) {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(Login.this, AllComplain.class));
                                                    finish();
                                                    break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(Login.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                            reference1.child("PatientTable")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                String Email = snapshot.child("email").getValue().toString();
                                                String Password = snapshot.child("password").getValue().toString();

                                                if (Email.matches(email) && Password.matches(password)) {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(Login.this, TabbedActivity.class));
                                                    finish();
                                                    break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(Login.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Not Authenticated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
