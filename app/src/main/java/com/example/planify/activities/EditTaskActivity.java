package com.example.planify.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planify.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditTaskActivity extends AppCompatActivity {

    TextInputEditText etTitle, etDescription, etDeadline;
    Button btnUpdate;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDeadline = findViewById(R.id.etDeadline);

        // Ambil data dari Intent
        id = getIntent().getStringExtra("id");

        etTitle.setText(getIntent().getStringExtra("title"));
        etDescription.setText(getIntent().getStringExtra("description"));
        etDeadline.setText(getIntent().getStringExtra("deadline"));

        btnUpdate.setOnClickListener(v -> {
            // Nanti kita isi updateTask()
        });
    }
}