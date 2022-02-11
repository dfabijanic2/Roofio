package com.example.roofio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roofio.Listeners.OnFirebaseDataRetrievedListener;
import com.example.roofio.adapters.CategoryAdapter;
import com.example.roofio.adapters.RecentlyListedAdapter;
import com.example.roofio.models.Category;
import com.example.roofio.models.Property;
import com.example.roofio.models.PropertyInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnFirebaseDataRetrievedListener {

    RecyclerView categoryRecycleView;
    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    RecyclerView recentlyListedRecycleView;
    RecentlyListedAdapter recentlyListedAdapter;
    List<PropertyInfo> recentlyListedList;

    CodeListManager codeListManager;

    DatabaseReference database;

    Button btnAddListing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recentlyListedList = new ArrayList<>();

        database = FirebaseDatabase.getInstance().getReference();

        recentlyListedRecycleView = findViewById(R.id.lastListingsRecycler);
        btnAddListing = findViewById(R.id.btnAddListing);

        RecyclerView.LayoutManager rlLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recentlyListedRecycleView.setLayoutManager(rlLayoutManager);
        recentlyListedAdapter = new RecentlyListedAdapter(MainActivity.this, recentlyListedList);
        recentlyListedRecycleView.setAdapter(recentlyListedAdapter);


        categoryList = new ArrayList<>();
        categoryRecycleView = findViewById(R.id.categoryRecycler);

        RecyclerView.LayoutManager categoryLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        categoryRecycleView.setLayoutManager(categoryLayoutManager);
        categoryAdapter = new CategoryAdapter(MainActivity.this, categoryList);
        categoryRecycleView.setAdapter(categoryAdapter);

        codeListManager = CodeListManager.getInstance(this);

        categoryList = codeListManager.getCategories();
        categoryAdapter.setCategories(categoryList);
        categoryAdapter.notifyDataSetChanged();

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            btnAddListing.setVisibility(View.GONE);
        }
        else{
            btnAddListing.setVisibility(View.VISIBLE);
        }

        btnAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewListingActivity.class));
            }
        });

    }

    @Override
    protected void onRestart() {
        getNekretnine();
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            inflater.inflate(R.menu.menu_public, menu);
        }else{
            inflater.inflate(R.menu.menu_signed_in, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.accSignIn:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.accSignUp:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.accSignOut:
                FirebaseAuth.getInstance().signOut();
                recreate();
                break;
            case R.id.myListings:
                Intent i = new Intent(MainActivity.this, UserListingsActivity.class);
                startActivity(i);
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void FirebaseDataRetrieved() {

        categoryAdapter.notifyDataSetChanged();
        getNekretnine();

    }

    private void getNekretnine(){
        database.child("Nekretnine").limitToLast(10)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        recentlyListedList= new ArrayList<>();
                        ArrayList<Property> properties = new ArrayList<>();
                        snapshot.getChildren().forEach(n -> properties.add(n.getValue(Property.class).setKey(n.getKey())));
                        properties.forEach(p ->
                                recentlyListedList.add(
                                        new PropertyInfo(p.getKey(),
                                                p.getNaziv(),
                                                codeListManager.GetCategoryByKey(p.getKategorija()).getNaziv(),
                                                codeListManager.GetStatusByKey(p.getStatus()).getNaziv(),
                                                p.getCijena(),
                                                p.getLokacija(),
                                                p.getBrojSoba(),
                                                p.getSlike() != null && p.getSlike().size() > 0 ? p.getSlike().get(0) : "",
                                                p.getStatus()
                                        )
                                )
                        );
                        recentlyListedAdapter.setProperties(recentlyListedList);
                        recentlyListedAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Neuspješno", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}