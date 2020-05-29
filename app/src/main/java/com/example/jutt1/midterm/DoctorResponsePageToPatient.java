package com.example.jutt1.midterm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jutt1.midterm.PersonModel.PersonModel;

public class DoctorResponsePageToPatient extends AppCompatActivity {

    TextView patientName, patientEmail;
    EditText Response;
    Button SubmitBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_response_page_to_patient);

        progressDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        PersonModel personModel = intent.getParcelableExtra("list");

        String Name = personModel.getName();
        String Email = personModel.getEmail();

        patientName = findViewById(R.id.pName);
        patientName.setText(Name);
        patientEmail = findViewById(R.id.pEmail);
        patientEmail.setText(Email);

        Response = findViewById(R.id.response);

        SubmitBtn = findViewById(R.id.responseBtn);
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Toast.makeText(DoctorResponsePageToPatient.this, "Your response have successfully been mailed to patient!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                startActivity(new Intent(DoctorResponsePageToPatient.this, AllComplain.class));
                finish();


            }
        });

    }
}
