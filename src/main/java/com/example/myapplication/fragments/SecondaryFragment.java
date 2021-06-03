package com.example.myapplication.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ChipsAdapter;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;

import java.util.Arrays;
import java.util.List;

public class SecondaryFragment extends Fragment {
    private String ID;
    private final String KEY = "KEY";
    private Dish dish;
    private DishesViewModel dishesViewModel;
    private List<String> ingredientsList;

    public SecondaryFragment(){
        super(R.layout.fragment_secondary);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.ID = getArguments().getString(KEY);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dishesViewModel = new ViewModelProvider(getActivity()).get(DishesViewModel.class);

        dishesViewModel.getMutableLiveData().getValue().forEach(v -> {
            if(v.getKey().equalsIgnoreCase(ID)){
                dish = v;
                String[] ingredients = dish.getIngredients().split(",");

                if(ingredients.length > 0){
                    ingredientsList = Arrays.asList(dish.getIngredients().split(","));
                } else {
                    Toast.makeText(getContext(), "No ingredients", Toast.LENGTH_LONG);
                }
            }
        });

        TextView textView = view.findViewById(R.id.dishTitle);
        textView.setText(dish.getTitle());

        ChipsAdapter chipsAdapter = new ChipsAdapter(getContext(), R.layout.chip_item, ingredientsList);

        ListView listView = view.findViewById(R.id.chipsListView);
        listView.setAdapter(chipsAdapter);
    }
}
