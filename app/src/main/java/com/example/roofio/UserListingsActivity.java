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
import android.widget.TextView;
import android.widget.Toast;

import com.example.roofio.adapters.ListingsByCategoryAdapter;
import com.example.roofio.adapters.UserListingsAdapter;
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

public class UserListingsActivity extends AppCompatActivity {

    DatabaseReference database;
    CodeListManager codeListManager;

    TextView categoryName;

    RecyclerView userListingsRecycleView;

    List<PropertyInfo> userListingsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_listings);


        database = FirebaseDatabase.getInstance().getReference();
        codeListManager = CodeListManager.getInstance();

        categoryName = findViewById(R.id.categoryNameTextView);

        userListingsRecycleView = findViewById(R.id.userListingsRecycler);

        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserListingsActivity.this, LinearLayoutManager.VERTICAL, false);
        userListingsRecycleView.setLayoutManager(layoutManager);
        UserListingsAdapter adapter = new UserListingsAdapter(UserListingsActivity.this, userListingsList);
        userListingsRecycleView.setAdapter(adapter);

        database.child("Nekretnine").orderByChild("oglasivac").equalTo(userKey)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userListingsList= new ArrayList<>();
                        ArrayList<Property> properties = new ArrayList<>();
                        snapshot.getChildren().forEach(n -> properties.add(n.getValue(Property.class).setKey(n.getKey())));
                        properties.forEach(p ->
                                userListingsList.add(
                                        new PropertyInfo(p.getKey(),
                                                p.getNaziv(),
                                                codeListManager.GetCategoryByKey(p.getKategorija()).getNaziv(),
                                                codeListManager.GetStatusByKey(p.getStatus()).getNaziv(),
                                                p.getCijena(),
                                                p.getLokacija(),
                                                p.getBrojSoba(),
                                                p.getSlike() != null ? p.getSlike().get(0) : "",
                                                p.getStatus(),
                                                p.getVrijemeKreiranjaOglasa()
                                        )
                                )
                        );
                        adapter.setListings(userListingsList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserListingsActivity.this, "Neuspješno", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.accSignIn:
                startActivity(new Intent(UserListingsActivity.this, LoginActivity.class));
                break;
            case R.id.accSignUp:
                startActivity(new Intent(UserListingsActivity.this, RegisterActivity.class));
                break;
            case R.id.accSignOut:
                FirebaseAuth.getInstance().signOut();
                recreate();
                break;
            case R.id.myListings:
                Intent i = new Intent(UserListingsActivity.this, UserListingsActivity.class);
                startActivity(i);
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}