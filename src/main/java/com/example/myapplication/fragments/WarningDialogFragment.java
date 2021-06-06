package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class WarningDialogFragment extends DialogFragment{
    private final static String TAG = "WARNING_DIALOG";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);

        return alert.create();
    }

    public static String getTagName(){
        return WarningDialogFragment.TAG;
    }
}