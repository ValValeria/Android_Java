package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import com.example.myapplication.adapters.DishesAdapter;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.example.myapplication.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class HomeFragment extends Fragment{
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private List<Dish> dishList = new ArrayList<>();
    private ProgressBar progressBar;
    private ListView listView;
    private DishesAdapter dishesAdapter;
    private int spacing;
    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getActivity(), DatabaseService.class);
        getActivity().startService(intent);

        spacing = (int) getResources().getDimension(R.dimen.margin_half);
    }

    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        listView = getActivity().findViewById(R.id.dishesList);
        dishesAdapter = new DishesAdapter(view.getContext(), R.layout.card_item, dishList, navController);
        listView.setAdapter(dishesAdapter);

        progressBar = getActivity().findViewById(R.id.indicator);

        if(listView.getAdapter().isEmpty()){
            listView.setVisibility(View.INVISIBLE);
        }

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
                    dishesAdapter.add(v);
                    dishList.add(v);

                    Set set = new LinkedHashSet(dishList);
                    set.add(v);
                    dishList.clear();
                    dishList.addAll(set);

                    addPostCount(dishList.size());

                    progressBar.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.loginFragment);
        } else {
            addPostCount(dishList.size());
        }
    }
    
    private void addPostCount(int size){
        String textStr = String.format("Dear user, the total amount of posts is %d ", size);

        SpannableString spannableString = new SpannableString(textStr);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                 textStr.length() - 4,
                textStr.length()-1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = getActivity().findViewById(R.id.userEmail);
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }
}
