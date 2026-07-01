package com.example.planify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.planify.R;
import com.example.planify.adapter.TaskAdapter;
import com.example.planify.model.Task;
import com.example.planify.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerTask;
    FloatingActionButton fabAdd;
    BottomNavigationView bottomNavigation;

    TextView txtUsername;

    SessionManager sessionManager;

    ArrayList<Task> taskList;
    TaskAdapter adapter;

    String URL_GET_TASKS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        txtUsername = findViewById(R.id.txtUsername);
        recyclerTask = findViewById(R.id.recyclerTask);
        fabAdd = findViewById(R.id.fabAdd);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        txtUsername.setText(
                "Hello, " +
                        sessionManager.getUserName()
        );

        URL_GET_TASKS =
                "http://10.0.2.2/planify_api/get_tasks.php?user_id="
                        + sessionManager.getUserId();

        recyclerTask.setLayoutManager(
                new LinearLayoutManager(this));

        taskList = new ArrayList<>();

        adapter = new TaskAdapter(this,taskList);

        recyclerTask.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddTaskActivity.class
                    );

            startActivity(intent);
        });

        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.nav_profile){

                startActivity(
                        new Intent(
                                MainActivity.this,
                                ProfileActivity.class
                        )
                );

                return true;
            }

            return true;
        });

        loadTasks();
    }

    private void loadTasks() {

        JsonArrayRequest request =
                new JsonArrayRequest(
                        URL_GET_TASKS,

                        response -> {

                            taskList.clear();

                            try {

                                for (int i = 0;
                                     i < response.length();
                                     i++) {

                                    JSONObject obj =
                                            response.getJSONObject(i);

                                    Task task =
                                            new Task(
                                                    obj.getString("id"),
                                                    obj.getString("title"),
                                                    obj.getString("description"),
                                                    obj.getString("deadline")
                                            );

                                    taskList.add(task);
                                }

                                adapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        },

                        error -> error.printStackTrace()
                );

        RequestQueue queue =
                Volley.newRequestQueue(this);

        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}