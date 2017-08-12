package com.sanju.developer.inventrom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import android.app.Fragment;

/**
 * Created by Sanju on 11-Aug-17.
 */



public class Action extends Fragment{
        Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.action, container, false);

         button=(Button)rootView.findViewById(R.id.go);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),CamActivity.class);       // calling Camactivity
                startActivity(intent);
            }
        });
        return rootView;


    }




}
