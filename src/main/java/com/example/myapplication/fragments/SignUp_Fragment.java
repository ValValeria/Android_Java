package com.example.myapplication.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUp_Fragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public SignUp_Fragment() {
        super(R.layout.fragment_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Toast.makeText(getActivity(), "You has already authenticated", Toast.LENGTH_LONG);
            goHome();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialCardView materialCardView = getActivity().findViewById(R.id.message_sign_up);
        materialCardView.setOnClickListener(v->{
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.signupFragment);
        });

        Button button = getActivity().findViewById(R.id.signup_btn);
        button.setOnClickListener(this::signUp);
    }

    public void signUp(View view){
        String email = ((TextInputEditText)getActivity().findViewById(R.id.email)).getText().toString();
        String password = ((TextInputEditText)getActivity().findViewById(R.id.password)).getText().toString();

        Toast.makeText(getActivity(), "Processing authentication. Please, wait.",
                Toast.LENGTH_SHORT).show();

        Runnable runnable = () -> {
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), task -> {
                if (task.isSuccessful()) {
                    view.post(()->{
                        Toast.makeText(getActivity(), "Authentication is successful.",
                                Toast.LENGTH_SHORT).show();

                        goHome();
                    });
                } else {
                    view.post(()->{
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    });
                }
            });
        };

        new Thread(runnable).start();
    }

    private void goHome(){
        NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        navController.navigate(R.id.homeFragment);
    }
}