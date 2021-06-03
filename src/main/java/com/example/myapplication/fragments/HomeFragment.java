package com.example.myapplication.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import com.example.myapplication.adapters.DishesAdapter;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import java.util.Optional;


public class HomeFragment extends Fragment{
    private DishesViewModel dishesViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private List<Dish> dishList;
    private ProgressBar progressBar;
    private ListView listView;
    private DishesAdapter dishesAdapter;

    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dishesViewModel = new ViewModelProvider(requireActivity()).get(DishesViewModel.class);
        dishList = dishesViewModel.getMutableLiveData().getValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        listView = getActivity().findViewById(R.id.dishesList);
        progressBar = getActivity().findViewById(R.id.indicator);

        dishesAdapter = new DishesAdapter(view.getContext(),
                R.layout.card_item, dishList, navController);
        listView.setAdapter(dishesAdapter);

        if(listView.getAdapter().isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
        }

        final Observer<List<Dish>> observer = dishes -> {
            for (int i = 0; i < dishes.size(); i++) {
                 Dish dish = dishes.get(i);

                for (int j = 0; j < dishList.size(); j++) {
                     if(!dishList.get(i).getKey().equalsIgnoreCase(dish.getKey())){
                         dishList.add(dish);
                         addPostCount(dishList.size());
                     }
                }
            }

            if(dishList.size()> 0 && progressBar.getVisibility() == View.VISIBLE){
                progressBar.setVisibility(View.INVISIBLE);
            }
        };

        dishesViewModel.getMutableLiveData().observe(getActivity(), observer);
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
