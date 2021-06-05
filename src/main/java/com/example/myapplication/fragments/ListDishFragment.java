package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;


public class ListDishFragment extends Fragment {
    private List<Dish> dishList = new ArrayList<>();
    private List<Dish> initDishList = new ArrayList<>();
    private NavController navController;
    private LinearLayout linearLayout;
    private final String KEY = "KEY";


    public ListDishFragment(){
        super(R.layout.fragment_list_dish);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayout = getActivity().findViewById(R.id.search_results);

        SearchView searchView = getActivity().findViewById(R.id.search);
        searchView.focusSearch(View.FOCUS_UP);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String txt = searchView.getQuery().toString();

                linearLayout.removeAllViews();

                for (int i = 0; i < dishList.size(); i++) {
                    Dish dish = dishList.get(i);

                    if(dish.getTitle().contains(txt) || dish.getDescription().contains(txt) || dish.getIngredients().contains(txt)){
                        addCard(dish);
                    }
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();

        DishesViewModel.getPublishSubject()
                .distinct()
                .filter(v -> {
                    boolean isUnique = true;

                    for (int i = 0; i < dishList.size(); i++) {
                        Dish v1 = dishList.get(i);

                        if(v1.getKey().equals(v.getKey())){
                            isUnique = false;
                        }
                    }

                    return isUnique;
                })
                .subscribe(v -> {
                    dishList.add(v);
                    initDishList.add(v);
                    addCard(v);
                });
    }


    private void addCard(Dish dish){
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.card_item, linearLayout, false);
        view.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;

        TextView title = (TextView) view.findViewById(R.id.title_card);
        title.setText(dish.getTitle());

        TextView long_description = (TextView) view.findViewById(R.id.long_description_card);
        long_description.setText(dish.getDescription());

        MaterialButton materialButton = view.findViewById(R.id.btn);
        materialButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(KEY, dish.getKey());

            navController.navigate(R.id.secondaryFragment, bundle);
        });

        linearLayout.addView(view);
    }
}
