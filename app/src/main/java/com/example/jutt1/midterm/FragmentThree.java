package com.example.jutt1.midterm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentThree extends Fragment {

    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fragment_three, container, false);

        Bundle bundle = getArguments();
        final String Did = bundle.getString("DoctorId");

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
