package com.example.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.models.Dish;

import java.util.ArrayList;
import java.util.List;

public class DishesViewModel extends ViewModel {
    private final MutableLiveData<List<Dish>> mutableLiveData = new MutableLiveData<>();

    public DishesViewModel() {
        super();

        this.mutableLiveData.setValue(new ArrayList<>());
    }

    public void setDishes(List<Dish> dishList){
        mutableLiveData.setValue(dishList);
    }

    public void addDish(Dish dish){
        List<Dish> list = mutableLiveData.getValue();
        list.add(dish);

        this.setDishes(list);
    }

    public LiveData<List<Dish>> getMutableLiveData() {
        return mutableLiveData;
    }
}
