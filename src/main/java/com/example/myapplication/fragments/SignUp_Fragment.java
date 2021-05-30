package com.example.myapplication.fragments;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public void signup(View view){
        String email = ((TextInputEditText)getActivity().findViewById(R.id.email)).getText().toString();
        String password = ((TextInputEditText)getActivity().findViewById(R.id.password)).getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    goHome();
                } else {
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goHome(){
        NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        navController.navigate(R.id.homeFragment);
    }

}