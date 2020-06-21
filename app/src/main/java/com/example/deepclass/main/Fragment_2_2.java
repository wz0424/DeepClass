package com.example.deepclass.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class Fragment_2_2 extends Fragment {


    /**
     * A simple {@link Fragment} subclass.
     */
    public Fragment_2_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        return inflater.inflate(R.layout.fragment_2_2, container, false);
    }

    LinearLayout enter_class;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        enter_class = (LinearLayout) getActivity().findViewById(R.id.Time_Master);

        enter_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimeMasterActivity.class);
                startActivity(intent);
            }
        });
    }
}
