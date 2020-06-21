package com.example.deepclass.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Fragment_3_2 extends Fragment {

    public static String ID;
    public static String Name;
    /**
     * A simple {@link Fragment} subclass.
     */
    public Fragment_3_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        return inflater.inflate(R.layout.fragment_3_2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        TextView tv_person_studentName =(TextView) getActivity().findViewById(R.id.tv_person_studentName);
        TextView tv_person_studentID =(TextView) getActivity().findViewById(R.id.tv_person_studentID);
        tv_person_studentName.setText(Name);
        tv_person_studentID.setText(ID);

        Button exit=(Button) getActivity().findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);  }
        });
    }
}
