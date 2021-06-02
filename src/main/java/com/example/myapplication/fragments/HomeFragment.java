package com.example.myapplication.fragments;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


public class HomeFragment extends Fragment{
    private DishesViewModel dishesViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String email;
    private List<Dish> dishList;
    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dishesViewModel = new ViewModelProvider(requireActivity()).get(DishesViewModel.class);
        dishList = dishesViewModel.getMutableLiveData().getValue();

        final Observer<List<Dish>> observer = dishes -> {
              if(dishes.size()>0){
                  dishList.addAll(dishes);
              }

              System.out.println("added");
        };

        dishesViewModel.getMutableLiveData().observe(getActivity(), observer);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        ListView listView = view.findViewById(R.id.dishesList);

        DishesAdapter dishesAdapter = new DishesAdapter(view.getContext(),
                R.layout.card_item, dishList, navController);
        listView.setAdapter(dishesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.loginFragment);
        } else {
            email = firebaseUser.getEmail();

            String textStr = String.format("Dear user, your email is %s", email);
            SpannableString string = new SpannableString(String.format("Dear user, your email is %s", email));
            string.setSpan(new UnderlineSpan(),
                    textStr.indexOf(email),
                    textStr.indexOf(email)+email.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView textView = getActivity().findViewById(R.id.userEmail);
            textView.setText(string, TextView.BufferType.SPANNABLE);
        }
    }
}
