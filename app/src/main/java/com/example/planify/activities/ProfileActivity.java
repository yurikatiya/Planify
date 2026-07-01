package com.example.planify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planify.R;
import com.example.planify.utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    TextView txtName, txtEmail;
    Button btnLogout;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        btnLogout = findViewById(R.id.btnLogout);

        sessionManager = new SessionManager(this);

        txtName.setText(
                sessionManager.getUserName()
        );

        txtEmail.setText(
                sessionManager.getEmail()
        );

        btnLogout.setOnClickListener(v -> {

            sessionManager.logout();

            Intent intent =
                    new Intent(
                            ProfileActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finishAffinity();
        });
    }
}