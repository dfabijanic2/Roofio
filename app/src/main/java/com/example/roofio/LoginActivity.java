package com.example.roofio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private boolean isAllFieldsChecked = false;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                CheckAllFields();
                if (isAllFieldsChecked) {
                    loginUser(txt_email, txt_password);
                }
            }
        });
    }

    private void CheckAllFields() {
        isAllFieldsChecked = true;
        if (email.length() == 0) {
            email.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        if (password.length() == 0) {
            password.setError("Ovo polje je obavezano");
            isAllFieldsChecked = false;
        }
        else if (password.length() < 6) {
            password.setError("Lozinka mora sadržavati barem 6 znakova");
            isAllFieldsChecked = false;
        }

    }
    private void loginUser(String txt_email, String txt_password) {

        auth.signInWithEmailAndPassword(txt_email, txt_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Prijava uspješna", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Prijava neuspješna", Toast.LENGTH_SHORT).show();
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