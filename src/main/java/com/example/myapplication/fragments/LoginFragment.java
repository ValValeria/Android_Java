package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public LoginFragment(){
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Toast.makeText(getActivity(), "You has already authenticated", Toast.LENGTH_LONG);
        }

        Button button = getActivity().findViewById(R.id.login_btn);
        button.setOnClickListener(v->{
            Runnable runnable = () -> {
                TextInputEditText textInputEditText = getActivity().findViewById(R.id.loginEmail);
                TextInputEditText textInputEditText1 = getActivity().findViewById(R.id.loginPassword);

                String email = textInputEditText.getText().toString();
                String password = textInputEditText1.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(getActivity(), task -> {
                            if (task.isSuccessful()) {
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment);
                            } else {
                                view.post(()->{
                                    Toast.makeText(getActivity(), "You are not in our database",
                                            Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
            };

            new Thread(runnable).start();
        });

        MaterialCardView materialCardView = getActivity().findViewById(R.id.message_login);
        materialCardView.setOnClickListener(v->{
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.signupFragment);
        });
    }
}