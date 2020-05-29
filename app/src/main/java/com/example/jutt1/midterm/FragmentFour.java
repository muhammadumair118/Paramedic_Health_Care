package com.example.jutt1.midterm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jutt1.midterm.PersonModel.PersonModel;

import static android.content.Intent.getIntent;

public class FragmentFour extends Fragment {

    Button btn;
    TextView doctorSpecialization;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment_four, container, false);

        doctorSpecialization = view.findViewById(R.id.doctorSpecialization);

        Bundle bundle = getArguments();
        bundle.getParcelable("specialization");
        final String Did = bundle.getString("DoctorId");
        doctorSpecialization.setText(bundle.getString("specialization"));


        btn = view.findViewById(R.id.complainBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Complain.class);
                intent.putExtra("DoctorId", Did);
                startActivity(intent);

            }
        });

        return view;
    }
}


