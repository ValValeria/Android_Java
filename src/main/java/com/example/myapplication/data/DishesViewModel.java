package com.example.myapplication.data;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.models.Dish;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class DishesViewModel extends ViewModel {
    private final MutableLiveData<List<Dish>> mutableLiveData = new MutableLiveData<>();
    private final static PublishSubject<Dish> publishSubject = PublishSubject.create();

    public DishesViewModel() {
        super();

        this.mutableLiveData.setValue(new ArrayList<>());
        publishSubject.subscribe(this::addDish);
    }

    public void setDishes(List<Dish> dishList){
        mutableLiveData.setValue(dishList);
    }

    public void addDish(Dish dish){
        List<Dish> list = mutableLiveData.getValue();
        boolean isPresent = false;

        for (int i = 0; i < list.size(); i++) {
            Dish dish1 = list.get(i);

            if(dish1.getKey().equals(dish.getKey())){
                isPresent = true;
                break;
            }
        }

        if(!isPresent){
            list.add(dish);
            this.setDishes(list);
        }
    }

    public LiveData<List<Dish>> getMutableLiveData() {
        return mutableLiveData;
    }

    public static PublishSubject<Dish> getPublishSubject(){
        return DishesViewModel.publishSubject;
    }
}
