package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.concurrent.atomic.AtomicInteger;


public class HomeFragment extends Fragment{
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private List<Dish> dishList = new ArrayList<>();
    private ListView listView;
    private DishesAdapter dishesAdapter;
    private int spacing;
    private final int DISHES_MAX_COUNT = 1;
    private View view;
    private LinearLayout linearLayout;

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

        linearLayout = getActivity().findViewById(R.id.linear);
        view = LayoutInflater.from(getContext()).inflate(R.layout.empty_results, linearLayout, false);
        linearLayout.addView(view);

        listView = getActivity().findViewById(R.id.dishesList);
        dishesAdapter = new DishesAdapter(view.getContext(), R.layout.card_item, dishList, navController);
        listView.setAdapter(dishesAdapter);

        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(0);

        View finalView = view;
        DishesViewModel.getPublishSubject()
                .distinct()
                .filter(v -> {
                    boolean isUnique = true;

                    for (int i = 0; i < dishList.size(); i++) {
                         Dish v1 = dishList.get(i);

                         if(v1.getKey().equals(v.getKey()) && dishList.size() < DISHES_MAX_COUNT){
                             isUnique = false;
                         }
                    }

                    return isUnique;
                })
                .subscribe(v -> {
                    atomicInteger.set(atomicInteger.get()+1);

                    if(dishList.size() < DISHES_MAX_COUNT){
                        dishesAdapter.add(v);
                        dishList.add(v);

                        Set set = new LinkedHashSet(dishList);
                        set.add(v);
                        dishList.clear();
                        dishList.addAll(set);
                    }

                    if(finalView != null){
                        linearLayout.removeView(finalView);
                        linearLayout.invalidate();
                        linearLayout.requestLayout();
                    }

                    addPostCount(atomicInteger.get());
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

        if(textView != null){
            textView.setText(spannableString, TextView.BufferType.SPANNABLE);
        }
    }
}
