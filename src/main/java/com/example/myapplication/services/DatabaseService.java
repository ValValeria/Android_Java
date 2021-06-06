package com.example.myapplication.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.ReplaySubject;

public class DatabaseService extends Service {
    private DatabaseReference databaseReference;
    private final String DISH = "DISH";
    private List<Dish> data = new ArrayList<>();
    public final static ReplaySubject<Dish> deleteAction = ReplaySubject.create();
    public final static ReplaySubject<Dish> updateAction = ReplaySubject.create();

    public DatabaseService() {
        databaseReference = FirebaseDatabase.getInstance().getReference(DISH);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        deleteAction.subscribe(v -> {
             databaseReference.child(v.getKey()).removeValue((error, ref) -> {
                  Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT);
             });
        });

        updateAction.subscribe(v -> {
            databaseReference.child(v.getKey()).setValue(v)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT))
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Fail to update", Toast.LENGTH_SHORT));
        });

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(firebaseUser != null){
                    if(data.size() > 0){
                        data.clear();
                    }

                    for (DataSnapshot datasnapShot: snapshot.getChildren()) {
                        Dish dish = datasnapShot.getValue(Dish.class);

                        if(dish.getUserUid().equals(firebaseUser.getUid())){
                            data.add(dish);
                            DishesViewModel.getPublishSubject().onNext(dish);
                        }
                    }
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