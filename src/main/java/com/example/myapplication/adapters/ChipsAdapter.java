package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ChipsAdapter extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    private final int layout;
    private final List<String> listString;

    public ChipsAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);

        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.listString = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);
        Chip chip = view.findViewById(R.id.chip);
        chip.setText(listString.get(position));
        return view;
    }
}
