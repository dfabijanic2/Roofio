package com.example.roofio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roofio.adapters.ListingsByCategoryAdapter;
import com.example.roofio.adapters.RecentlyListedAdapter;
import com.example.roofio.models.Category;
import com.example.roofio.models.Property;
import com.example.roofio.models.PropertyInfo;
import com.example.roofio.models.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    CodeListManager codeListManager;
    DatabaseReference database;

    RecyclerView listingsRecycleView;
    ListingsByCategoryAdapter listingsAdapter;
    List<PropertyInfo> listingsList = new ArrayList<>();

    private EditText searchTextView;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = getIntent().getExtras();

        database = FirebaseDatabase.getInstance().getReference();
        codeListManager = CodeListManager.getInstance();

        listingsRecycleView = findViewById(R.id.listingsSearchRecycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
        listingsRecycleView.setLayoutManager(layoutManager);
        listingsAdapter = new ListingsByCategoryAdapter(SearchActivity.this, listingsList);
        listingsRecycleView.setAdapter(listingsAdapter);

        searchTextView = findViewById(R.id.searchText);

        if (extras != null) {
            searchText = extras.getString("searchText");
            searchTextView.setText(searchText);
            searchListings(searchText);
        }

        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND)
                {
                    searchListings(searchTextView.getText().toString());
                    return true;
                }
                return false;
            }
        });




    }

    public void searchListings(String search){
        database.child("Nekretnine").orderByChild("naziv").startAt(search).endAt(search+"\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<PropertyInfo> listings= new ArrayList<>();
                        ArrayList<Property> properties = new ArrayList<>();
                        snapshot.getChildren().forEach(n -> properties.add(n.getValue(Property.class).setKey(n.getKey())));
                        properties.forEach(p ->
                                listings.add(
                                        new PropertyInfo(p.getKey(),
                                                p.getNaziv(),
                                                codeListManager.GetCategoryByKey(p.getKategorija()).getNaziv(),
                                                codeListManager.GetStatusByKey(p.getStatus()).getNaziv(),
                                                p.getCijena(),
                                                p.getLokacija(),
                                                p.getBrojSoba(),
                                                p.getSlike().get(0),
                                                p.getStatus(),
                                                p.getVrijemeKreiranjaOglasa()
                                        )
                                )
                        );
                        listingsList.addAll(listings);
                        listingsList = listingsList.stream().distinct().collect(Collectors.toList());
                        listingsAdapter.setListings(listingsList);
                        listingsAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SearchActivity.this, "Neuspješno", Toast.LENGTH_SHORT).show();
                    }
                });

        database.child("Nekretnine").orderByChild("opisOglasa").startAt(search).endAt(search+"\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<PropertyInfo> listings= new ArrayList<>();
                        ArrayList<Property> properties = new ArrayList<>();
                        snapshot.getChildren().forEach(n -> properties.add(n.getValue(Property.class).setKey(n.getKey())));
                        properties.forEach(p ->
                                listingsList.add(
                                        new PropertyInfo(p.getKey(),
                                                p.getNaziv(),
                                                codeListManager.GetCategoryByKey(p.getKategorija()).getNaziv(),
                                                codeListManager.GetStatusByKey(p.getStatus()).getNaziv(),
                                                p.getCijena(),
                                                p.getLokacija(),
                                                p.getBrojSoba(),
                                                p.getSlike().get(0),
                                                p.getStatus(),
                                                p.getVrijemeKreiranjaOglasa()
                                        )
                                )
                        );
                        listingsList.addAll(listings);
                        listingsList = listingsList.stream().distinct().collect(Collectors.toList());
                        listingsAdapter.setListings(listingsList);
                        listingsAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SearchActivity.this, "Neuspješno", Toast.LENGTH_SHORT).show();
                    }
                });
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

}