package com.example.roofio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    private Button btnSend;
    private EditText emailFrom;
    private EditText emailTo;
    private EditText emailSubject;
    private EditText emailMessage;
    private String from;
    private String to;
    private String subject;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        emailFrom = (EditText)findViewById(R.id.email_from);
        emailTo = (EditText)findViewById(R.id.email_to);
        emailSubject = (EditText)findViewById(R.id.email_subject);
        emailMessage = (EditText)findViewById(R.id.email_message);
        btnSend = (Button)findViewById(R.id.btnSend);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            emailTo.setText(extras.getString("to"));
            emailSubject.setText(extras.getString("subject"));
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = emailFrom.getText().toString();
                to = emailTo.getText().toString();
                subject = emailSubject.getText().toString();
                message = emailMessage.getText().toString();

                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:"));
                i.putExtra(Intent.EXTRA_EMAIL  , to);
                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                i.putExtra(Intent.EXTRA_TEXT   , message);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}