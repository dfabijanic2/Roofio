package com.example.roofio;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roofio.Listeners.OnFirebaseDataRetrievedListener;
import com.example.roofio.adapters.CategoryAdapter;
import com.example.roofio.models.Category;
import com.example.roofio.models.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CodeListManager {
    private List<Category> categories;
    private List<Status> statuses;

    private static CodeListManager instance;
    private DatabaseReference database;

    private static OnFirebaseDataRetrievedListener listener;

    private CodeListManager() {
        database = FirebaseDatabase.getInstance().getReference();
        categories = new ArrayList<>();
        statuses= new ArrayList<>();

        Statuses();
        Categories();

        dataReceived();
    }

    public static CodeListManager getInstance(){
        if(instance == null){
            instance = new CodeListManager();
        }
        return instance;
    }

    public static CodeListManager getInstance(OnFirebaseDataRetrievedListener listener){
        CodeListManager.listener = listener;
        if(instance == null){
            instance = new CodeListManager();
        }

        try{
            return instance;
        }
        finally {
            instance.dataReceived();
        }
    }

    private void Categories() {
        database.child("Kategorije").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                }else{
                    categories.clear();
                    task.getResult().getChildren().forEach(c ->
                            categories.add(new Category(c.getKey(), c.getValue(Category.class).getNaziv(), c.getValue(Category.class).getImageUrl())));
                    dataReceived();
                }
            }
        });

    }

    private void Statuses() {
        database.child("Status").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                }else{
                    statuses.clear();
                    task.getResult().getChildren().forEach(c -> statuses.add(new Status(c.getKey(), c.getValue(String.class))));
                    dataReceived();
                }
            }
        });
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Status> getStatuses(){
        return statuses;
    }

    public Category GetCategoryByKey(Integer key){
        return categories.stream().filter(c -> c.getKey().equals(key.toString())).findFirst().get();
    }

    public Status GetStatusByKey(Integer key){
        return statuses.stream().filter(c -> c.getKey().equals(key.toString())).findFirst().get();
    }

    private void dataReceived(){
        if(categories.size() != 0 && statuses.size() != 0){
            listener.FirebaseDataRetrieved();
        }
    }
}
