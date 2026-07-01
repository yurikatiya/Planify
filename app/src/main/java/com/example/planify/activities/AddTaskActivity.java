package com.example.planify.activities;

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

import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    TextInputEditText etTitle, etDescription, etDeadline;
    Button btnSave;

    private static final String URL_ADD =
            "http://10.0.2.2/planify_api/add_task.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDeadline = findViewById(R.id.etDeadline);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {

        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String deadline = etDeadline.getText().toString().trim();

        if(TextUtils.isEmpty(title)){

            Toast.makeText(
                    this,
                    "Title wajib diisi",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        URL_ADD,

                        response -> {

                            if(response.trim().equals("success")){

                                Toast.makeText(
                                        this,
                                        "Task berhasil ditambahkan",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();

                            }else{

                                Toast.makeText(
                                        this,
                                        "Gagal menambah task",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        },

                        error -> Toast.makeText(
                                this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()

                ){

                    @Override
                    protected Map<String, String> getParams() {

                        Map<String,String> params =
                                new HashMap<>();

                        params.put("user_id","1");
                        params.put("title",title);
                        params.put("description",description);
                        params.put("deadline",deadline);

                        return params;
                    }
                };

        RequestQueue queue =
                Volley.newRequestQueue(this);

        queue.add(request);
    }
}