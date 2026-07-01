package com.example.planify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.planify.R;
import com.example.planify.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword;
    Button btnLogin;
    TextView txtRegister;

    SessionManager sessionManager;

    private static final String URL_LOGIN =
            "http://10.0.2.2/planify_api/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        // Auto Login
        if(sessionManager.isLoggedIn()){

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            MainActivity.class
                    )
            );

            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        txtRegister.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            RegisterActivity.class
                    );

            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {

        String email =
                etEmail.getText().toString().trim();

        String password =
                etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password)) {

            Toast.makeText(
                    this,
                    "Isi semua field",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        URL_LOGIN,

                        response -> {

                            try {

                                JSONObject obj =
                                        new JSONObject(response);

                                boolean success =
                                        obj.getBoolean("success");

                                if(success){

                                    String id =
                                            obj.getString("id");

                                    String name =
                                            obj.getString("name");

                                    String userEmail =
                                            obj.getString("email");

                                    sessionManager.saveUser(
                                            id,
                                            name,
                                            userEmail
                                    );

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Login Berhasil",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    startActivity(
                                            new Intent(
                                                    LoginActivity.this,
                                                    MainActivity.class
                                            )
                                    );

                                    finish();

                                }else{

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Email atau Password salah",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                            }catch (Exception e){

                                e.printStackTrace();

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Error parsing data",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        },

                        error -> Toast.makeText(
                                LoginActivity.this,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()

                ){

                    @Override
                    protected Map<String, String> getParams() {

                        Map<String,String> params =
                                new HashMap<>();

                        params.put("email", email);
                        params.put("password", password);

                        return params;
                    }
                };

        RequestQueue queue =
                Volley.newRequestQueue(this);

        queue.add(request);
    }
}