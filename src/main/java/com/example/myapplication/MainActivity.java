package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.example.myapplication.services.DatabaseService;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LifecycleObserver{
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DatabaseService.class);
        startService(intent);

        this.addEventHandler();
    }

    @Override
    protected void onStart() {
        super.onStart();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Toast.makeText(this, "You are not authenticated", Toast.LENGTH_LONG);
            navController.navigate(R.id.loginFragment);
        }

        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_logout:
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        if(firebaseUser != null){
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                            navController.navigate(R.id.signupFragment);
                        }
                }

                return false;
            }
        });
    }

    public void addEventHandler(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    navController.navigate(R.id.homeFragment);
                    break;
                case R.id.addItem:
                    navController.navigate(R.id.addItemFragment);
            }

            return false;
        });
    }
}