package com.sanju.developer.inventrom;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Sanju on 11-Aug-17.
 */

public class Show extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sshow, container, false);
        TextView tv=(TextView)rootView.findViewById(R.id.section_label);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Inventrom".toString();  // my path where I saved the files from here I get the files
      //  Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
       // String ss = null;
     //   Log.d("Files", "Size: " + file.length);
        StringBuffer buffer = new StringBuffer();                   // string Buffer is used because of MUTABLE feature(changeable) in nature
        int count = 0;
        for (int i = file.length-2; i >=0; i--) {
            String.valueOf(count++);                                //indexing for files
            buffer.append(count);
            buffer.append(". ");
            buffer.append(file[i].getName());                       //add name's
            buffer.append(System.getProperty("line.separator"));    // separated by 1 line gap
            buffer.append(System.getProperty("line.separator"));

            Log.d("Files", "FileName:" + file[i].getName());        //testing

        }
        tv.setText(buffer);                                         // buffer is saved to TEXTVIEW

        return rootView;
    }
}
