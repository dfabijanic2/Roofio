package com.example.roofio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roofio.adapters.ListingDetailsAdapter;
import com.example.roofio.adapters.ListingsByCategoryAdapter;
import com.example.roofio.models.Property;
import com.example.roofio.models.PropertyDetails;
import com.example.roofio.models.PropertyInfo;
import com.example.roofio.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListingDetailsActivity extends AppCompatActivity {

    DatabaseReference database;
    String listingKey;
    CodeListManager codeListManager;
    PropertyDetails property;


    ViewPager2 mViewPager;
    TextView desc;
    TextView price;
    TextView title;
    TextView category;
    TextView size;
    TextView numRoom;
    TextView numEtaza;
    TextView furniture;
    TextView balkonlodaterasa;
    TextView yearBuild;
    TextView yearRenovation;
    TextView energyLevel;
    TextView location;
    TextView dateCreated;
    Button btnContact;

    String to;
    String subject;

    // images array
    List<String> images;
    // Creating Object of ViewPagerAdapter
    ListingDetailsAdapter mViewPagerAdapter;

    ListingsByCategoryAdapter listingsByCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        Bundle extras = getIntent().getExtras();

        title = (TextView)findViewById(R.id.titleDetail);
        price = (TextView)findViewById(R.id.priceDetail);
        category = (TextView)findViewById(R.id.categoryDetail);
        size = (TextView)findViewById(R.id.sizeDetail);
        numRoom = (TextView)findViewById(R.id.numDetail);
        numEtaza = (TextView)findViewById(R.id.numEtazaDewtail);
        furniture = (TextView)findViewById(R.id.furnitureDetail);
        balkonlodaterasa = (TextView)findViewById(R.id.balkonloda);
        yearBuild = (TextView)findViewById(R.id.yearBuildDetail);
        yearRenovation = (TextView)findViewById(R.id.yearRenovationDetail);
        energyLevel = (TextView)findViewById(R.id.energLevel);
        desc = (TextView)findViewById(R.id.descDetail);
        location = (TextView)findViewById(R.id.locationDetail);
        dateCreated = (TextView)findViewById(R.id.dateCreated);
        btnContact = (Button)findViewById(R.id.btnContact);

        database = FirebaseDatabase.getInstance().getReference();
        codeListManager = CodeListManager.getInstance();

        if (extras != null) {
            listingKey = extras.getString("listingKey");
        }

        mViewPager = (ViewPager2)findViewById(R.id.viewPager);

        images = new ArrayList<>();
        // Initializing the ViewPagerAdapter
        mViewPagerAdapter = new ListingDetailsAdapter(ListingDetailsActivity.this, images);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(mViewPagerAdapter);

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(ListingDetailsActivity.this, ContactActivity.class);
                //i.putExtra("to", to);
                //i.putExtra("subject", subject);
                //startActivity(i);

                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:"));
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{ to });
                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ListingDetailsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        database.child("Nekretnine").child(listingKey.toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists())
                            return;
                        Property property = snapshot.getValue(Property.class).setKey(snapshot.getKey());
                        images = property.getSlike();
                        title.setText(property.getNaziv());
                        price.setText(String.format("%.2f", property.getCijena()));
                        desc.setText(property.getOpisOglasa());
                        location.setText(property.getLokacija());
                        numRoom.setText(property.getBrojSoba().toString());
                        numEtaza.setText(property.getBrojEtaza().toString());
                        furniture.setText(property.getNamjestenost());
                        balkonlodaterasa.setText(property.getBakonLodaTerasa());
                        yearBuild.setText(property.getGodinaIzgradnje().toString());
                        yearRenovation.setText(property.getGodinaZadnjeRenovacije().toString());
                        energyLevel.setText(property.getEnergetskiRazred());
                        mViewPagerAdapter.setImages(images);
                        mViewPagerAdapter.notifyDataSetChanged();
                        category.setText(codeListManager.GetCategoryByKey(property.getKategorija()).getNaziv());
                        size.setText(property.getStambenaPovrsina().toString());
                        String dt = LocalDateTime.parse(property.getVrijemeKreiranjaOglasa()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss"));
                        dateCreated.setText(dt);


                        database.child("Users").child(property.getOglasivac()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                to = snapshot.getValue(User.class).getEmail();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        subject = property.getNaziv();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ListingDetailsActivity.this, "Neuspješno", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(ListingDetailsActivity.this, LoginActivity.class));
                break;
            case R.id.accSignUp:
                startActivity(new Intent(ListingDetailsActivity.this, RegisterActivity.class));
                break;
            case R.id.accSignOut:
                FirebaseAuth.getInstance().signOut();
                recreate();
                break;
            case R.id.myListings:
                Intent i = new Intent(ListingDetailsActivity.this, UserListingsActivity.class);
                startActivity(i);
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
