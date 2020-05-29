package com.example.jutt1.midterm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth auth;
    String docId, patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("DoctorsTable")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String Id = snapshot.child("id").getValue().toString();
                                        docId = Id;

                                        if (auth.getCurrentUser().getUid().matches(docId)) {
                                            startActivity(new Intent(SplashScreen.this, AllComplain.class));
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(SplashScreen.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                    reference1.child("PatientTable")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String Id = snapshot.child("id").getValue().toString();
                                        patientId = Id;

                                        if (auth.getCurrentUser().getUid().matches(patientId)) {
                                            startActivity(new Intent(SplashScreen.this, TabbedActivity.class));
                                            break;
                                        }



                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(SplashScreen.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                else {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                    finish();
                }
            }
        },1000);


    }
}
