package com.example.deepclass.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class Fragment_1_2 extends Fragment {
    String id=null;

    /**
     * A simple {@link Fragment} subclass.
     */
    public Fragment_1_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        if(isAdded())
            id=getArguments().getString("id");


        return inflater.inflate(R.layout.fragment_1_2, container, false);
    }

    LinearLayout check;
    LinearLayout practiice;
    //LinearLayout discuss;
    //LinearLayout statistics;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        check = (LinearLayout) getActivity().findViewById(R.id.check);
        practiice = (LinearLayout) getActivity().findViewById(R.id.practice);
        //discuss = (LinearLayout) getActivity().findViewById(R.id.discuss);
       //statistics = (LinearLayout) getActivity().findViewById(R.id.statistics);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), check2.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        practiice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),QuestionListActivity.class);
                startActivity(intent);
            }
        });

//        discuss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), discuss2.class);
//                startActivity(intent);
//            }
//        });
//
//        statistics.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), statistics2.class);
//                startActivity(intent);
//            }
//        });
    }
}
