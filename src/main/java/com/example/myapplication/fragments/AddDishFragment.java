package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.data.DishesViewModel;
import com.example.myapplication.models.Dish;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AddDishFragment extends Fragment {
    private DatabaseReference databaseReference;
    private final String DISH = "DISH";
    private static int countOfVisists = 0;

    public AddDishFragment(){
          super(R.layout.fragment_additem);
      }

    @Override
    public void onStart() {
        super.onStart();

        Button button = this.getActivity().findViewById(R.id.click);
        button.setOnClickListener(this::addItem);

        databaseReference = FirebaseDatabase.getInstance().getReference(DISH);

        countOfVisists++;

        if(countOfVisists == 1){
            new WarningDialogFragment().show(getParentFragmentManager(), WarningDialogFragment.getTagName());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void addItem(View view){
          Dish dish = new Dish();

          TextInputEditText textInputEditText = view.getRootView().findViewById(R.id.titleInput);
          String[] title = {"title", textInputEditText.getText().toString()};
          dish.setTitle(textInputEditText.getText().toString());

          TextInputEditText textInputEditText2 = view.getRootView().findViewById(R.id.descriptionInput);
          String[] description = {"description", Objects.requireNonNull(textInputEditText2.getText()).toString()};

          TextInputEditText textInputEditText3 = view.getRootView().findViewById(R.id.ingredientsInput);
          String[] ingredients = {"ingredients", Objects.requireNonNull(textInputEditText3.getText()).toString()};

          Observable.just(title, description, ingredients).subscribe(new Observer<String[]>() {
              private boolean isValid = true;
              private List<String> errorsList = new ArrayList<>();

              @Override
              public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

              }

              @Override
              public void onNext(@io.reactivex.annotations.NonNull String s[]) {
                  String inputText = s[0].trim();

                  if(inputText.isEmpty() || inputText.length() < 5 || inputText.length() > 200){
                      errorsList.add(String.format("Please check the validity of %s", s[0]));
                      isValid = false;
                  }
              }

              @Override
              public void onError(@io.reactivex.annotations.NonNull Throwable e) {
              }

              @Override
              public void onComplete() {
                  if(this.isValid){
                      dish.setTitle(title[1]);
                      dish.setIngredients(ingredients[1]);
                      dish.setDescription(description[1]);

                      DishesViewModel dishesViewModel = new ViewModelProvider(requireActivity()).get(DishesViewModel.class);
                      dishesViewModel.addDish(dish);

                      Toast toast = Toast.makeText(requireActivity(), "The recipe is added", Toast.LENGTH_LONG);
                      toast.show();

                      Runnable runnable = () -> {
                          String key = databaseReference.push().getKey();
                          dish.setKey(key);
                          databaseReference.push().setValue(dish);
                      };

                      new Thread(runnable).start();
                  } else {
                      ListView listView = view.getRootView().findViewById(R.id.listErrors);
                      ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, new ArrayList());
                      listView.setAdapter(arrayAdapter);
                  }
              }
          });
      }
}