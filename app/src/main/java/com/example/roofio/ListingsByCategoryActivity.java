package com.example.roofio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ListingsByCategoryActivity extends AppCompatActivity {

    CodeListManager codeListManager;
    TextView categoryName;
    DatabaseReference database;
    Integer categoryKey;
    private Status statusSelected;

    RecyclerView listingsByCategoryRecycleView;
    ListingsByCategoryAdapter listingsByCategoryAdapter;
    List<PropertyInfo> listingsByCategoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings_by_category);

        Bundle extras = getIntent().getExtras();

        database = FirebaseDatabase.getInstance().getReference();
        codeListManager = CodeListManager.getInstance();

        categoryName = findViewById(R.id.categoryNameTextView);

        listingsByCategoryRecycleView = findViewById(R.id.listingsRecycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListingsByCategoryActivity.this, LinearLayoutManager.VERTICAL, false);
        listingsByCategoryRecycleView.setLayoutManager(layoutManager);
        listingsByCategoryAdapter = new ListingsByCategoryAdapter(ListingsByCategoryActivity.this, listingsByCategoryList);
        listingsByCategoryRecycleView.setAdapter(listingsByCategoryAdapter);

        if (extras != null) {
            categoryName.setText(extras.getString("categoryName"));
            categoryKey = Integer.parseInt(extras.getString("categoryKey"));
        }

        database.child("Nekretnine").orderByChild("kategorija").equalTo(categoryKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listingsByCategoryList= new ArrayList<>();
                        ArrayList<Property> properties = new ArrayList<>();
                        snapshot.getChildren().forEach(n -> properties.add(n.getValue(Property.class).setKey(n.getKey())));
                        properties.forEach(p ->
                                    listingsByCategoryList.add(
                                            new PropertyInfo(p.getKey(),
                                                p.getNaziv(),
                                                codeListManager.GetCategoryByKey(p.getKategorija()).getNaziv(),
                                                codeListManager.GetStatusByKey(p.getStatus()).getNaziv(),
                                                p.getCijena(),
                                                p.getLokacija(),
                                                p.getBrojSoba(),
                                                p.getSlike().get(0),
                                                    p.getStatus()
                                            )
                                    )
                                );
                        if(statusSelected != null){
                            if(!statusSelected.getKey().equals("-1"))
                                listingsByCategoryList = listingsByCategoryList.stream().filter(l -> l.getStatusId().toString().equals(statusSelected.getKey())).collect(Collectors.toList());
                        }
                        listingsByCategoryAdapter.setListings(listingsByCategoryList);
                        listingsByCategoryAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ListingsByCategoryActivity.this, "Neuspješno", Toast.LENGTH_SHORT).show();
                    }
                });

        Spinner spinnerStatus = findViewById(R.id.statusSpinner);

        List<Status> statuses = new ArrayList<>();
        statuses.add(new Status("-1", "Sve"));
        statuses.addAll(codeListManager.getStatuses());

        ArrayAdapter<Status> adapterStatus= new ArrayAdapter<Status>(this, R.layout.spinner_item, statuses);

        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerStatus.setAdapter(adapterStatus);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = (Status) parent.getItemAtPosition(position);
                if(!statusSelected.getKey().equals("-1"))
                    filterListings(statusSelected.getKey());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void filterListings(String statusKey){
        List<PropertyInfo> listings = listingsByCategoryList.stream().filter(l -> l.getStatusId().toString().equals(statusKey)).collect(Collectors.toList());
        listingsByCategoryAdapter.setListings(listings);
        listingsByCategoryAdapter.notifyDataSetChanged();
    }
}