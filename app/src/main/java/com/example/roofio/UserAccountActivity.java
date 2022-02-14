package com.example.roofio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roofio.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAccountActivity extends AppCompatActivity {

    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private Button update;
    private boolean isAllFieldsChecked;

    FirebaseUser currentUser;
    DatabaseReference dbUser;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        email = findViewById(R.id.profile_email);
        update = findViewById(R.id.updateUser);

        firstName = findViewById(R.id.profile_last_name);
        lastName = findViewById(R.id.profile_first_name);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        dbUser =  FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        dbUser.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(UserAccountActivity.this, "Ažuriranje podataka nije uspješno", Toast.LENGTH_SHORT).show();
                }else{
                    user = task.getResult().getValue(User.class);
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    email.setText(user.getEmail());
                }

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAllFields();
                if (isAllFieldsChecked)
                {
                    updateUser();
                }
            }
        });
    }

    private void updateUser() {


        currentUser.updateEmail(email.getText().toString());

        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setEmail(email.getText().toString());
        dbUser.setValue(user);
        Toast.makeText(UserAccountActivity.this, "Ažuriranje podataka uspješno", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(UserAccountActivity.this, MainActivity.class);
        startActivity(i);
    }

    private void CheckAllFields() {
        isAllFieldsChecked = true;
        if (email.length() == 0) {
            email.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (firstName.length() == 0) {
            firstName.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (lastName.length() == 0) {
            lastName.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }

    }
}