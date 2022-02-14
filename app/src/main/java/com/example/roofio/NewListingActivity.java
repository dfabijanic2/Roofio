package com.example.roofio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.roofio.adapters.ImageSliderAdapter;
import com.example.roofio.adapters.RecentlyListedAdapter;
import com.example.roofio.models.Category;
import com.example.roofio.models.Image;
import com.example.roofio.models.Property;
import com.example.roofio.models.PropertyInfo;
import com.example.roofio.models.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NewListingActivity extends AppCompatActivity {

    private EditText newTitle;
    private EditText newDesc;
    private EditText newSize;
    private EditText newNumRoom;
    private EditText newNumEtaza;
    private EditText newFurniture;
    private EditText newBalkonLoda;
    private EditText newYearBuild;
    private EditText newYearRenovation;
    private EditText newEnergyLevel;
    private EditText newPrice;
    private EditText newLocation;
    private Category categorySelected;
    private Status statusSelected;
    private List<String> images = new ArrayList<>();

    private Button btnAddListingFinal;
    private Button btnAddImages;

    private CodeListManager codeListManager;
    private DatabaseReference database;
    private ArrayList<String> urlStrings;
    private List<Image> ImageList = new ArrayList<>();

    Spinner spinnerStatus;
    Spinner spinnerCategories;

    private boolean isAllFieldsChecked = false;

    Property prop = new Property();

    RecyclerView imageRecyclerView;
    ImageSliderAdapter imageSliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        database = FirebaseDatabase.getInstance().getReference();
        codeListManager = CodeListManager.getInstance();
        spinnerCategories = findViewById(R.id.categorySpinner);

        imageRecyclerView = findViewById(R.id.imagesRecyclerView);
        RecyclerView.LayoutManager rlLayoutManager = new LinearLayoutManager(NewListingActivity.this, LinearLayoutManager.HORIZONTAL, false);
        imageRecyclerView.setLayoutManager(rlLayoutManager);
        imageSliderAdapter = new ImageSliderAdapter(NewListingActivity.this, ImageList);
        imageRecyclerView.setAdapter(imageSliderAdapter);

        btnAddListingFinal = (Button)findViewById(R.id.btnAddListingFinal);
        btnAddImages = (Button)findViewById(R.id.btnAddImages);
        newTitle = (EditText)findViewById(R.id.newTitle);
        newDesc = (EditText)findViewById(R.id.newDesc);
        newSize = (EditText)findViewById(R.id.newSize);
        newNumRoom = (EditText)findViewById(R.id.newNumRoom);
        newNumEtaza = (EditText)findViewById(R.id.newNumEtaza);
        newFurniture = (EditText)findViewById(R.id.newFurniture);
        newBalkonLoda = (EditText)findViewById(R.id.newBalkonLoda);
        newYearBuild = (EditText)findViewById(R.id.newYearBuild);
        newYearRenovation = (EditText)findViewById(R.id.newYearRenovation);
        newEnergyLevel = (EditText)findViewById(R.id.newEnergyLevel);
        newLocation = (EditText)findViewById(R.id.newLocation);
        newPrice = (EditText)findViewById(R.id.newPrice);


        ArrayAdapter<Category> adapter= new ArrayAdapter<Category>(this, R.layout.spinner_item, codeListManager.getCategories());

        adapter.setDropDownViewResource(R.layout.spinner_item);

        spinnerCategories.setAdapter(adapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected = (Category) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStatus = findViewById(R.id.spinnerStatus);

        ArrayAdapter<Status> adapterStatus= new ArrayAdapter<Status>(this, R.layout.spinner_item, codeListManager.getStatuses());

        adapterStatus.setDropDownViewResource(R.layout.spinner_item);

        spinnerStatus.setAdapter(adapterStatus);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSelected = (Status) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImages();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String key = extras.getString("listingKey");
            fillFields(key);
        }

        btnAddListingFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAllFields();
                String id = extras != null ? extras.getString("listingKey") :  database.child("Nekretnine").push().getKey();
                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference();
                urlStrings = new ArrayList<>();
                if (isAllFieldsChecked) {
                    if(ImageList.size() == 0){
                        saveListing(id);
                        return;
                    }
                    for (int upload_count = 0; upload_count < ImageList.size(); upload_count++) {
                        if(!ImageList.get(upload_count).isLocal()){
                            urlStrings.add(ImageList.get(upload_count).getImageUri().toString());
                            if (urlStrings.size() == ImageList.size()) {
                                saveListing(id);
                            }
                            continue;
                        }
                        Uri IndividualImage = ImageList.get(upload_count).getImageUri();
                        final StorageReference ImageName = ImageFolder.child(id + IndividualImage.getLastPathSegment());
                            ImageName.putFile(IndividualImage).addOnSuccessListener(
                                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            ImageName.getDownloadUrl().addOnSuccessListener(
                                                    new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            urlStrings.add(String.valueOf(uri));
                                                            if (urlStrings.size() == ImageList.size()) {
                                                                saveListing(id);
                                                            }
                                                        }
                                                    }
                                            );
                                        }
                                    }
                            );

                    }


                }
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

    private void saveListing(String id){
        Property newPropery = new Property(newTitle.getText().toString(), Integer.parseInt(categorySelected.getKey()), Integer.parseInt(statusSelected.getKey()), FirebaseAuth.getInstance().getCurrentUser().getUid(), newDesc.getText().toString(), urlStrings, Double.parseDouble(newPrice.getText().toString()), newLocation.getText().toString(), Double.parseDouble(newNumRoom.getText().toString()), Integer.parseInt(newSize.getText().toString()), Integer.parseInt(newNumEtaza.getText().toString()), Integer.parseInt(newYearBuild.getText().toString()), Integer.parseInt(newYearRenovation.getText().toString()), newEnergyLevel.getText().toString(), newBalkonLoda.getText().toString(), newFurniture.getText().toString());
        database.child("Nekretnine").child(id).setValue(newPropery);
        Toast.makeText(NewListingActivity.this, "Oglas uspješno kreiran", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(NewListingActivity.this, UserListingsActivity.class);
        startActivity(i);
    }

    private void CheckAllFields() {
        isAllFieldsChecked = true;
        if (newTitle.length() == 0) {
            newTitle.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newDesc.length() == 0) {
            newDesc.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newSize.length() == 0) {
            newSize.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newLocation.length() == 0) {
            newLocation.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newPrice.length() == 0) {
            newPrice.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newBalkonLoda.length() == 0) {
            newBalkonLoda.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newEnergyLevel.length() == 0) {
            newEnergyLevel.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newFurniture.length() == 0) {
            newFurniture.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;

        }
        if (newNumEtaza.length() == 0) {
            newNumEtaza.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newNumRoom.length() == 0) {
            newNumRoom.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newYearBuild.length() == 0) {
            newYearBuild.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (newYearRenovation.length() == 0) {
            newYearRenovation.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if(ImageList.size() == 0)
        {
            Toast.makeText(NewListingActivity.this, "Odaberite barem jednu sliku", Toast.LENGTH_LONG).show();
            isAllFieldsChecked = false;
        }


    }

    private void chooseImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                if (data.getClipData() != null) {

                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSlect = 0;

                    while (currentImageSlect < countClipData) {

                        Uri ImageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                        ImageList.add(new Image(ImageUri, true));
                        currentImageSlect = currentImageSlect + 1;
                    }
                } else if(data.getData() != null){
                    ImageList.add(new Image(data.getData(), true));
                }
                else {
                    Toast.makeText(this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                }
                imageSliderAdapter.setImages(ImageList);
                imageSliderAdapter.notifyDataSetChanged();

            }
        }
    }

    private void fillFields(String listingKey){

        database.child("Nekretnine").child(listingKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    return;
                }
                prop = snapshot.getValue(Property.class);
                newTitle.setText(prop.getNaziv());
                newDesc.setText(prop.getOpisOglasa());
                newSize.setText(prop.getStambenaPovrsina().toString());
                newNumRoom.setText(String.format("%.1f", prop.getBrojSoba()));
                newNumEtaza.setText(prop.getBrojEtaza().toString());
                newFurniture.setText(prop.getNamjestenost());
                newBalkonLoda.setText(prop.getBakonLodaTerasa());
                newYearBuild.setText(prop.getGodinaIzgradnje().toString());
                newYearRenovation.setText(prop.getGodinaZadnjeRenovacije().toString());
                newEnergyLevel.setText(prop.getEnergetskiRazred());
                newLocation.setText(prop.getLokacija());
                newPrice.setText(String.format("%.2f", prop.getCijena()));
                spinnerStatus.setSelection(codeListManager.getStatuses().indexOf(codeListManager.GetStatusByKey(prop.getStatus())));
                spinnerCategories.setSelection(codeListManager.getCategories().indexOf(codeListManager.GetCategoryByKey(prop.getKategorija())));

                ImageList = prop.getSlike() != null ? prop.getSlike().stream().map(s -> new Image(Uri.parse(s), false)).collect(Collectors.toList()) : new ArrayList<>();

                imageSliderAdapter.setImages(ImageList);
                imageSliderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.accSignIn:
                startActivity(new Intent(NewListingActivity.this, LoginActivity.class));
                break;
            case R.id.accSignUp:
                startActivity(new Intent(NewListingActivity.this, RegisterActivity.class));
                break;
            case R.id.accSignOut:
                FirebaseAuth.getInstance().signOut();
                recreate();
                break;
            case R.id.myListings:
                Intent i = new Intent(NewListingActivity.this, UserListingsActivity.class);
                startActivity(i);
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}