package com.example.planify.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.planify.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditTaskActivity extends AppCompatActivity {

    TextInputEditText etTitle, etDescription, etDeadline;
    Button btnUpdate;
    Calendar calendar;

    String id;

    private static final String URL_UPDATE =
            "http://10.0.2.2/planify_api/update_task.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDeadline = findViewById(R.id.etDeadline);
        btnUpdate = findViewById(R.id.btnUpdate);

        id = getIntent().getStringExtra("id");

        etTitle.setText(getIntent().getStringExtra("title"));
        etDescription.setText(getIntent().getStringExtra("description"));
        etDeadline.setText(getIntent().getStringExtra("deadline"));

        calendar = Calendar.getInstance();

        etDeadline.setOnClickListener(v -> showDatePicker());
        etDeadline.setFocusable(false);
        etDeadline.setClickable(true);

        btnUpdate.setOnClickListener(v -> updateTask());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Prevent selecting past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etDeadline.setText(dateFormat.format(calendar.getTime()));
    }

    private void updateTask() {

        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String deadline = etDeadline.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Title wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_UPDATE,

                response -> {

                    if (response.trim().equals("success")) {

                        Toast.makeText(
                                this,
                                "Task berhasil diupdate",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    } else {

                        Toast.makeText(
                                this,
                                "Gagal mengupdate task",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                },

                error -> Toast.makeText(
                        this,
                        error.toString(),
                        Toast.LENGTH_LONG
                ).show()

        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("title", title);
                params.put("description", description);
                params.put("deadline", deadline);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}