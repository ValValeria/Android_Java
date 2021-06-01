package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.AsyncTask;
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
    private String email;

    public HomeFragment(){
        super(R.layout.fragment_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dishesViewModel = new ViewModelProvider(requireActivity()).get(DishesViewModel.class);

        new HomeFragment.DishesAsyncTask().execute();
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
       NavController navController = Navigation.findNavController(view);
       ListView listView = view.findViewById(R.id.dishesList);

       DishesAdapter dishesAdapter = new DishesAdapter(view.getContext(),
                R.layout.card_item, dishesViewModel.getMutableLiveData().getValue(), navController);
       listView.setAdapter(dishesAdapter);

       FloatingActionButton button = view.findViewById(R.id.floating_action_button);
       button.setOnClickListener(v -> navController.navigate(R.id.addItemFragment));
    }

    private class DishesAsyncTask  extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
