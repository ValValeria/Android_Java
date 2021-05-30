package com.example.myapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.navigation.NavController;
import com.example.myapplication.R;
import com.example.myapplication.models.Dish;
import com.google.android.material.button.MaterialButton;
import androidx.navigation.NavController;
import java.util.List;

public class DishesAdapter  extends ArrayAdapter<Dish> {
    private LayoutInflater inflater;
    private int layout;
    private List<Dish> dishes;
    private Context context;
    private NavController navController;

    public DishesAdapter(Context context, int resource, List<Dish> dishes, NavController navController){
       super(context,resource, dishes);

       this.inflater = LayoutInflater.from(context);
       this.layout = resource;
       this.dishes = dishes;
       this.navController = navController;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);
        Dish dish = dishes.get(position);

        TextView title = (TextView) view.findViewById(R.id.title_card);
        title.setText(dish.getTitle());

        TextView long_description = (TextView) view.findViewById(R.id.long_description_card);
        long_description.setText(dish.getDescription());

        MaterialButton materialButton = view.findViewById(R.id.btn);
        materialButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", dish.getId());

            navController.navigate(R.id.secondaryFragment, bundle);
        });

        return view;
    }
}
