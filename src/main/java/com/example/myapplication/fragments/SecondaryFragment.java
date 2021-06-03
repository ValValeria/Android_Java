package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ChipsAdapter;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.example.myapplication.services.DatabaseService;
import java.util.Arrays;
import java.util.List;


public class SecondaryFragment extends Fragment {
    private String ID;
    private final String KEY = "KEY";
    private Dish dish = new Dish();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DishesViewModel.getPublishSubject()
                .filter(v -> v.getKey().equals(ID))
                .first(new Dish())
                .subscribe(v -> {
                    if(v.getTitle().length() > 0){
                        dish = v;

                        String[] ingredients = dish.getIngredients().split(",");

                        if(ingredients.length > 0) {
                            ingredientsList = Arrays.asList(dish.getIngredients().split(","));
                        } else {
                            Toast.makeText(getContext(), "No ingredients", Toast.LENGTH_LONG);
                        }

                        setupText();
                    } else {
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.homeFragment);
                    }
                });
    }

    private void setupText(){
        TextView textView = getView().findViewById(R.id.dishTitle);
        textView.setText(dish.getTitle());

        ChipsAdapter chipsAdapter = new ChipsAdapter(getContext(), R.layout.chip_item, ingredientsList);
        GridView gridView = getView().findViewById(R.id.chipsListView);
        int spacing = (int) getResources().getDimension(R.dimen.margin_half);
        gridView.setAdapter(chipsAdapter);
        gridView.setVerticalSpacing(spacing/2);
        gridView.setHorizontalSpacing(spacing/2);
        gridView.setNumColumns(GridView.AUTO_FIT);

        TextView textView1 = getView().findViewById(R.id.secondaryDishDescription);
        textView1.setText(dish.getDescription());
    }
}
