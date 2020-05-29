package com.example.jutt1.midterm;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jutt1.midterm.Adapters.PersonAdapter;
import com.example.jutt1.midterm.PersonModel.PersonModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentOne extends Fragment implements PersonAdapter.OnItemClickListener {

    PersonAdapter adapter;
    ArrayList<PersonModel> list;
    RecyclerView recyclerView;
    LinearLayoutManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment_one, container, false);

        recyclerView = view.findViewById(R.id.myRecyclerView);
        manager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        list = new ArrayList<>();

        recyclerView.setLayoutManager(manager);

        FetchDataDoctors();

        return view;
    }

    private void FetchDataDoctors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("DoctorsTable")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String Id = snapshot.child("id").getValue().toString();
                            String Name = snapshot.child("name").getValue().toString();
                            String Email = snapshot.child("email").getValue().toString();
                            String Phone = snapshot.child("phone").getValue().toString();
                            String Password = snapshot.child("password").getValue().toString();
                            String ImgUrl = snapshot.child("imageURL").getValue().toString();
                            String Address = snapshot.child("address").getValue().toString();
                            String Specialization = snapshot.child("specialization").getValue().toString();
                            String RatingBar = snapshot.child("rating_bar").getValue().toString();
                            String AccountStatus = snapshot.child("account_Status").getValue().toString();

                            list.add(new PersonModel(Id,Name,Email,Password,Phone,ImgUrl,Address,Specialization,RatingBar,AccountStatus));

                        }

                        adapter = new PersonAdapter(getActivity(), list, new PersonAdapter.OnItemClickListener() {

                            @Override
                            public void onClick(int position) {
                                list.get(position);
                                Intent intent = new Intent(getActivity(), DoctorDataTab.class);
                                intent.putExtra("list", list.get(position));
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(int position) {
        list.get(position);
        startActivity(new Intent(getActivity(), DoctorDataTab.class));
    }
}
