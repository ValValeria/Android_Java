package com.example.myapplication.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.models.Dish;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService extends Service {
    private DatabaseReference databaseReference;
    private final String DISH = "DISH";
    private List<Dish> data = new ArrayList<>();

    public DatabaseService() {}

    @Override
    public void onCreate() {
        super.onCreate();

        databaseReference = FirebaseDatabase.getInstance().getReference(DISH);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(data.size() > 0){
                    data.clear();
                }

                for (DataSnapshot datasnapShot: snapshot.getChildren()) {
                    Dish dish = datasnapShot.getValue(Dish.class);
                    data.add(dish);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Data is loading", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }
}