package com.example.roofio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roofio.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private Button register;

    String txt_email;
    String txt_password;

    private boolean isAllFieldsChecked = false;

    private FirebaseAuth auth;
    private DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);

        auth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference().child("Users");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_email = email.getText().toString();
                txt_password = password.getText().toString();
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked)
                {
                    registerUser(txt_email, txt_password);
                }
            }
        });

    }

    private boolean CheckAllFields() {
        if (email.length() == 0) {
            email.setError("Ovo polje je obavezano");
        }
        if (password.length() == 0) {
            password.setError("Ovo polje je obavezano");
        }
        else if (password.length() < 6) {
            password.setError("Lozinka mora sadržavati barem 6 znakova");
        }
        if (firstName.length() == 0) {
            firstName.setError("Ovo polje je obavezano");
        }
        if (lastName.length() == 0) {
            lastName.setError("Ovo polje je obavezano");
        }
        return true;

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

    private void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registracija uspješna", Toast.LENGTH_SHORT).show();
                    String uid = task.getResult().getUser().getUid();
                    users.child(uid).setValue(new User(firstName.getText().toString(), lastName.getText().toString(), email));
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registracija nije uspješna", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}