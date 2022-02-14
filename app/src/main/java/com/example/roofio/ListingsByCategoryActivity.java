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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roofio.adapters.ListingsByCategoryAdapter;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    List<PropertyInfo> allListings = new ArrayList<>();

    Integer selectedOrder = 0;

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
                                                    p.getStatus(),
                                                    p.getVrijemeKreiranjaOglasa()
                                            )
                                    )
                                );
                        allListings = listingsByCategoryList;
                        if(statusSelected != null){
                            if(!statusSelected.getKey().equals("-1"))
                                listingsByCategoryList = listingsByCategoryList.stream().filter(l -> l.getStatusId().toString().equals(statusSelected.getKey())).collect(Collectors.toList());
                                orderListings();
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

        ArrayAdapter<Status> adapterStatus = new ArrayAdapter<Status>(this, R.layout.spinner_item, statuses);

        adapterStatus.setDropDownViewResource(R.layout.spinner_item);

        spinnerStatus.setAdapter(adapterStatus);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = (Status) parent.getItemAtPosition(position);
                if(!statusSelected.getKey().equals("-1")){
                    filterListings(statusSelected.getKey());
                    orderListings();
                    listingsByCategoryAdapter.setListings(listingsByCategoryList);
                    listingsByCategoryAdapter.notifyDataSetChanged();
                }

                else{
                    listingsByCategoryList = allListings;
                    orderListings();
                    listingsByCategoryAdapter.setListings(listingsByCategoryList);
                    listingsByCategoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner orderSpinner = findViewById(R.id.orderSpinner);

        List<String> orderBy = new ArrayList<>();
        orderBy.add( "Najnovije");
        orderBy.add( "Najstarije");
        orderBy.add( "Cijena - uzlazno");
        orderBy.add( "Cijena - silazno");

        ArrayAdapter<String> adapterOrder = new ArrayAdapter<String>(this, R.layout.spinner_item, orderBy);

        adapterOrder.setDropDownViewResource(R.layout.spinner_item);

        orderSpinner.setAdapter(adapterOrder);


        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrder = position;
                orderListings();
                listingsByCategoryAdapter.setListings(listingsByCategoryList);
                listingsByCategoryAdapter.notifyDataSetChanged();
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
        listingsByCategoryList = allListings.stream().filter(l -> l.getStatusId().toString().equals(statusKey)).collect(Collectors.toList());
        listingsByCategoryAdapter.setListings(listingsByCategoryList);
        listingsByCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.accSignIn:
                startActivity(new Intent(ListingsByCategoryActivity.this, LoginActivity.class));
                break;
            case R.id.accSignUp:
                startActivity(new Intent(ListingsByCategoryActivity.this, RegisterActivity.class));
                break;
            case R.id.accSignOut:
                FirebaseAuth.getInstance().signOut();
                recreate();
                break;
            case R.id.myListings:
                Intent i = new Intent(ListingsByCategoryActivity.this, UserListingsActivity.class);
                startActivity(i);
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void orderListings(){
        Comparator<PropertyInfo> comparator = (l1, l2) -> {
            return LocalDateTime.parse(l1.getVrijemeKreiranjaOglasa()).compareTo(LocalDateTime.parse(l2.getVrijemeKreiranjaOglasa()));
        };

        Comparator<PropertyInfo> comparatorDesc = (l1, l2) -> {
            return LocalDateTime.parse(l2.getVrijemeKreiranjaOglasa()).compareTo(LocalDateTime.parse(l1.getVrijemeKreiranjaOglasa()));
        };
        switch (selectedOrder){
            case 0:
                Collections.sort(listingsByCategoryList, comparatorDesc);
                break;
            case 1:
                Collections.sort(listingsByCategoryList, comparator);
                break;
            case 2:
                listingsByCategoryList.sort(Comparator.comparing(PropertyInfo::getCijena));
                break;
            case 3:
                listingsByCategoryList.sort((l1, l2) -> l2.getCijena().compareTo(l1.getCijena()));
                break;

        }

    }
}