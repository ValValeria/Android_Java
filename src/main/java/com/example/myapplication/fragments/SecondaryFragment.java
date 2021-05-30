package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class SecondaryFragment extends Fragment {
    private int ID;

    public SecondaryFragment(){
        super(R.layout.fragment_secondary);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            this.ID = savedInstanceState.getInt("id");
        }
    }
}
