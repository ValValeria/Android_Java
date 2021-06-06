package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.example.myapplication.services.DatabaseService;
import com.google.android.material.chip.Chip;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class SecondaryFragment extends Fragment {
    private String ID;
    private final String KEY = "KEY";
    private Dish dish = new Dish();
    private List<String> ingredientsList;
    private EditText descriptionView;
    private EditText titleView;
    private NavController navController;

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

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        descriptionView = getView().findViewById(R.id.secondaryDishDescription);
        titleView = getView().findViewById(R.id.dishTitle);

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
                        navController.navigate(R.id.homeFragment);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        Button button = getActivity().findViewById(R.id.update_btn);
        button.setOnClickListener(v -> {
            String description = descriptionView.getText().toString();
            String title = titleView.getText().toString();

            dish.setTitle(title);
            dish.setDescription(description);

            DatabaseService.updateAction.onNext(dish);
        });

        Button button1 = getActivity().findViewById(R.id.delete_btn);
        button1.setOnClickListener(v -> {
            String description = descriptionView.getText().toString();
            String title = titleView.getText().toString();

            dish.setTitle(title);
            dish.setDescription(description);

            DatabaseService.deleteAction.onNext(dish);

            navController.navigate(R.id.homeFragment);
        });
    }

    private void setupText(){
        LinearLayout linearLayout = getActivity().findViewById(R.id.gridLayout);

        for (String ingredient : ingredientsList) {
            View view = getLayoutInflater().inflate(R.layout.chip_item, linearLayout, false);
            view.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;

            Chip chip = view.findViewById(R.id.chip);
            chip.setText(ingredient);
            chip.setText(ingredient);

            linearLayout.addView(view, linearLayout.getChildCount());
        }

        titleView.setText(dish.getTitle());
        descriptionView.setText(dish.getDescription());
    }
}
