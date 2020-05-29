package com.example.jutt1.midterm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jutt1.midterm.PersonModel.PersonModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {

    Button Register, Choose_Image, changeView;
    EditText Name, Email, Phone, Password, Confirm_Password, Address, Specialization;
    Uri FileData = null;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Choose_Image = findViewById(R.id.imagebtn);
        Register = findViewById(R.id.register);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        Password = findViewById(R.id.password);
        Confirm_Password = findViewById(R.id.cpassword);
        Address = findViewById(R.id.address);
        Specialization = findViewById(R.id.specialization);
        changeView = findViewById(R.id.doctorView);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Specialization.setVisibility(View.VISIBLE);
                changeView.setVisibility(View.GONE);
            }
        });

        Register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CheckAllValidation();
            }
        });

        Choose_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Choose_Image();
            }
        });
    }

    private void Choose_Image() {
        Intent ImagePick = new Intent();
        ImagePick.setType("image/*");
        ImagePick.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(ImagePick, 01);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 01 && resultCode == RESULT_OK){
            FileData = data.getData();
        }
    }

    private void CheckAllValidation() {
        String userName = Name.getText().toString().trim();
        String userEmail = Email.getText().toString().trim();
        String userPhone = Phone.getText().toString().trim();
        String userPassword = Password.getText().toString().trim();
        String userCon_Pass = Confirm_Password.getText().toString().trim();
        String userAddress = Address.getText().toString().trim();
        String doctorSpecialization = Specialization.getText().toString().trim();


        if (TextUtils.isEmpty(userName)) {
            Name.setError("Enter Name Here");
        }
        else if (TextUtils.isEmpty(userEmail)) {
            Email.setError("Enter Email Here");
        }
        else if (!(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())){
            Email.setError("Enter Valid Email");
        }
        else if (TextUtils.isEmpty(userPassword)) {
            Password.setError("Enter Password Here");
        }
        else if (TextUtils.isEmpty(userCon_Pass)) {
            Confirm_Password.setError("Re-Enter Password Here");
        }
        else if (!(userPassword.equals(userCon_Pass))){
            Toast.makeText(this, "Password Not Matched", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhone)) {
            Phone.setError("Enter Phone Here");
        }
        else if (TextUtils.isEmpty(userAddress)) {
            Address.setError("Enter Your Address Here");
        }
        else if (FileData == null){
            Toast.makeText(this, "Choose an Image", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(doctorSpecialization)) {
            InsertInDataBasePatient(userName,userEmail,userPassword,userPhone,FileData,userAddress);
        }
        else{
            InsertInDataBase(userName,userEmail,userPassword,userPhone,FileData,userAddress,doctorSpecialization);
        }
    }
    private void InsertInDataBasePatient(final String userName, final String userEmail, final String userPassword, final String userPhone, Uri fileData, final String userAddress) {
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            SendImageInStoragePatient(userName,userEmail,userPassword,userPhone,FileData,userAddress);
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Authentication not Complete", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void SendImageInStoragePatient(final String userName, final String userEmail, final String userPassword, final String userPhone, Uri fileData, final String userAddress) {
        final StorageReference reference = FirebaseStorage.getInstance().getReference("PersonImages/"+auth.getCurrentUser().getUid());
        reference.putFile(FileData).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    Uri DownloadedURL = task.getResult();
                    InsertInRealTimeDataBasePatient(userName, userEmail, userPassword, userPhone, DownloadedURL, userAddress);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Url Not Generated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InsertInRealTimeDataBasePatient(String userName, String userEmail, String userPassword, String userPhone, Uri downloadedURL, String userAddress) {
        PersonModel values = new PersonModel(auth.getCurrentUser().getUid(),userName,userEmail,userPassword,userPhone,downloadedURL.toString(),userAddress);
        FirebaseDatabase.getInstance().getReference("PatientTable").child(auth.getCurrentUser().getUid()).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, Login.class));
                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "User Not Created", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void InsertInDataBase(final String userName, final String userEmail, final String userPassword, final String userPhone, Uri fileData, final String userAddress, final String doctorSpecialization) {
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            SendImageInStorage(userName,userEmail,userPassword,userPhone,FileData,userAddress,doctorSpecialization);
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Authentication not Complete", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void SendImageInStorage(final String userName, final String userEmail, final String userPassword, final String userPhone, Uri fileData, final String userAddress, final String doctorSpecialization) {
        final StorageReference reference = FirebaseStorage.getInstance().getReference("PersonImages/"+auth.getCurrentUser().getUid());
        reference.putFile(FileData).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    Uri DownloadedURL = task.getResult();
                    InsertInRealTimeDataBase(userName, userEmail, userPassword, userPhone, DownloadedURL, userAddress, doctorSpecialization, "5", "Pending");
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "Url Not Generated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InsertInRealTimeDataBase(String userName, String userEmail, String userPassword, String userPhone, Uri downloadedURL, String userAddress, String doctorSpecialization, String rating_bar, String account_status) {
        PersonModel values = new PersonModel(auth.getCurrentUser().getUid(),userName,userEmail,userPassword,userPhone,downloadedURL.toString(),userAddress,doctorSpecialization,rating_bar,account_status);
        FirebaseDatabase.getInstance().getReference("DoctorsTable").child(auth.getCurrentUser().getUid()).setValue(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Doctor Account Status is Pending", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, Login.class));
                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Doctor Not Created", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
