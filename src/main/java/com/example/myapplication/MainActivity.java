package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.example.myapplication.services.DatabaseService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LifecycleObserver{
    private List<Dish> dishes = new LinkedList<>();
    private DatabaseReference databaseReference;
    private final String DISH = "DISH";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference(DISH);

        Intent intent = new Intent(this, DatabaseService.class);
        startService(intent);

        this.addEventHandler();
    }

    @Override
    protected void onStart() {
        super.onStart();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        DishesViewModel dishesViewModel = new ViewModelProvider(this).get(DishesViewModel.class);

        dishesViewModel.getMutableLiveData().observe(this, lists->{
            if(lists != null){
                for (int i = 0; i < lists.size(); i++) {
                    Dish dish = lists.get(i);

                    if(!dishes.contains(dish)){
                        addNewDishToDb(dish);
                    }
                }

                this.dishes = lists;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Toast.makeText(this, "You are not authenticated", Toast.LENGTH_LONG);
            navController.navigate(R.id.loginFragment);
        }
    }

    public void addEventHandler(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    navController.navigate(R.id.homeFragment);
                    break;
                case R.id.recipies_list:
                    break;
                case R.id.favorities:
                    break;
            }

            return false;
        });
    }

    private void addNewDishToDb(Dish dish) {
        databaseReference.push().setValue(dish);
    }
}