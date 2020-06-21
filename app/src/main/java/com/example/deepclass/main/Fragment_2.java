package com.example.deepclass.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class Fragment_2 extends Fragment {


    /**
     * A simple {@link Fragment} subclass.
     */
    public Fragment_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    LinearLayout start1;
    LinearLayout start2;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start1 = (LinearLayout) getActivity().findViewById(R.id.start1);
        start2 = (LinearLayout) getActivity().findViewById(R.id.start2);

        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), start1.class);

                startActivity(intent);
            }
        });

        start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), start2.class);

                startActivity(intent);
            }
        });
    }
}
