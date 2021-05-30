package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DishesAdapter;
import com.example.myapplication.data.DishesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment{
    private DishesViewModel dishesViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private NavController navController;

    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dishesViewModel = new ViewModelProvider(requireActivity()).get(DishesViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.loginFragment);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
       NavController navController = Navigation.findNavController(view);
       ListView listView = view.findViewById(R.id.dishesList);

       DishesAdapter dishesAdapter = new DishesAdapter(view.getContext(),
                R.layout.card_item, dishesViewModel.getMutableLiveData().getValue(), navController);
       listView.setAdapter(dishesAdapter);

       FloatingActionButton button = view.findViewById(R.id.floating_action_button);
       button.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View v) {
               navController.navigate(R.id.addItemFragment);
           }
       });
    }
}
