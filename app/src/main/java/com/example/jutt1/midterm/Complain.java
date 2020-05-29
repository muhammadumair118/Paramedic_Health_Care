package com.example.jutt1.midterm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jutt1.midterm.PersonModel.PersonModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Complain extends AppCompatActivity {

    EditText Cnic, BloodGroup, Description;
    Button submitComplain;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    String pName;
    String pEmail;
    String pPhone;
    String pImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        Cnic = findViewById(R.id.cnic);
        BloodGroup = findViewById(R.id.bloodGroup);
        Description = findViewById(R.id.description);



        submitComplain = findViewById(R.id.submitBtn);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();


        submitComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidations();
            }
        });
    }

    private void CheckValidations() {

        String userCnic = Cnic.getText().toString().trim();
        String userBloodGroup = BloodGroup.getText().toString().trim();
        String userComplain = Description.getText().toString().trim();
        String Status = "Pending";

        if (TextUtils.isEmpty(userCnic)) {
            Cnic.setError("Enter Your CNIC");
        } else if (TextUtils.isEmpty(userBloodGroup)) {
            BloodGroup.setError("Enter Your Blood Group");
        } else if (TextUtils.isEmpty(userComplain)) {
            Description.setError("Please Enter Your Desired Complain Here!");
        } else {
            RegisterComplain(Status,userCnic,userBloodGroup,userComplain);
        }
    }

    private void RegisterComplain(final String Status, final String userCnic, final String userBloodGroup, final String userComplain) {
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final SharedPreferences sharedPreferences = getSharedPreferences("Patient", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("PatientTable")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String Id = snapshot.child("id").getValue().toString();
                            String Name = snapshot.child("name").getValue().toString();
                            String Email = snapshot.child("email").getValue().toString();
                            String Phone = snapshot.child("phone").getValue().toString();
                            String ImgUrl = snapshot.child("imageURL").getValue().toString();
                            pName = Name;
                            pEmail = Email;
                            pPhone = Phone;
                            pImgUrl = ImgUrl;

                            if (auth.getCurrentUser().getUid().matches(Id)) {
                                editor.clear();
                                editor.apply();
                                editor.putString("PatientName", pName);
                                editor.putString("PatientEmail", pEmail);
                                editor.putString("PatientPhone", pPhone);
                                editor.putString("PatientImage", pImgUrl);
                                editor.apply();
                                break;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Complain.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        final Bundle bundle = getIntent().getExtras();

        PersonModel values = new PersonModel(
                bundle.getString("DoctorId"),
                sharedPreferences.getString("PatientName", pName),
                sharedPreferences.getString("PatientEmail", pEmail),
                sharedPreferences.getString("PatientPhone", pPhone),
                sharedPreferences.getString("PatientImage", pImgUrl),
                Status,
                userCnic,
                userBloodGroup,
                userComplain
        );
        editor.clear();
        editor.apply();
        FirebaseDatabase.getInstance().getReference("ComplainTable").child(auth.getCurrentUser().getUid()).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(Complain.this, "Your Complain Status is Pending", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Complain.this, TabbedActivity.class));
                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(Complain.this, "Complain Not Registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}