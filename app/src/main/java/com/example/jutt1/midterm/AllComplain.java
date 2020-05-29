package com.example.jutt1.midterm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jutt1.midterm.Adapters.PersonAdapter;
import com.example.jutt1.midterm.Adapters.PersonAdapter1;
import com.example.jutt1.midterm.PersonModel.PersonModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllComplain extends AppCompatActivity implements PersonAdapter.OnItemClickListener {

    PersonAdapter1 adapter;
    ArrayList<PersonModel> list;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    FirebaseAuth auth;
    String Cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_complain);

        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.myRecyclerView);
        manager = new LinearLayoutManager(this);
        list = new ArrayList<>();

        recyclerView.setLayoutManager(manager);

        Correction();
        //FetchComplains();

        
    }

    private void Correction() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("ComplainTable")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String Id = snapshot.child("id").getValue().toString();
                            Cid = Id;

                            if (auth.getCurrentUser().getUid().matches(Cid)) {
                                FetchComplains();
                                break;
                            }
                            else {
                                Toast.makeText(AllComplain.this, "No Complains To Show!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AllComplain.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            if (auth.getCurrentUser() != null) {
                auth.signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("SESSION", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("EMAIL");
                editor.remove("PASSWORD");
                editor.apply();
                editor.commit();
                startActivity(new Intent(this,Login.class));
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void FetchComplains() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("ComplainTable")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String Name = snapshot.child("name").getValue().toString();
                            String Email = snapshot.child("email").getValue().toString();
                            String Complain = snapshot.child("complain").getValue().toString();
                            String ImgUrl = snapshot.child("imageURL").getValue().toString();

                            list.add(new PersonModel(Name,ImgUrl,Complain,Email));
                        }

                        adapter = new PersonAdapter1(AllComplain.this, list, new PersonAdapter1.OnItemClickListener() {

                            @Override
                            public void onClick(int position) {
                                list.get(position);
                                Intent intent = new Intent(AllComplain.this, DoctorResponsePageToPatient.class);
                                intent.putExtra("list", list.get(position));
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AllComplain.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(int position) {
        list.get(position);
    }
}
