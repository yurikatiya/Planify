package com.example.planify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
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

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerTask;
    private BottomNavigationView bottomNavigation;

    private TextView txtUsername, txtTotalTask, txtCompletedTask;
    private TextView txtProgressPercent, txtProgressInfo;
    private ProgressBar progressTask;

    private SessionManager sessionManager;

    private ArrayList<Task> taskList;
    private TaskAdapter adapter;

    private String URL_GET_TASKS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        // Inisialisasi View
        txtUsername = findViewById(R.id.txtUsername);
        txtTotalTask = findViewById(R.id.txtTotalTask);
        txtCompletedTask = findViewById(R.id.txtCompletedTask);
        txtProgressPercent = findViewById(R.id.txtProgressPercent);
        txtProgressInfo = findViewById(R.id.txtProgressInfo);
        progressTask = findViewById(R.id.progressTask);

        recyclerTask = findViewById(R.id.recyclerTask);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        txtUsername.setText("Hello, " + sessionManager.getUserName());

        URL_GET_TASKS = "http://10.0.2.2/planify_api/get_tasks.php?user_id=" + sessionManager.getUserId();

        recyclerTask.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();

        adapter = new TaskAdapter(
                this,
                taskList,
                new TaskAdapter.OnTaskStatusChangedListener() {
                    @Override
                    public void onTaskStatusChanged() {
                        loadTasks();
                    }
                }
        );

        recyclerTask.setAdapter(adapter);

        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        loadTasks();
    }

    private void loadTasks() {
        JsonArrayRequest request = new JsonArrayRequest(
                URL_GET_TASKS,
                response -> {
                    taskList.clear();
                    int completed = 0;

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            Task task = new Task(
                                    obj.getString("id"),
                                    obj.getString("title"),
                                    obj.getString("description"),
                                    obj.getString("deadline"),
                                    obj.getString("status")
                            );
                            taskList.add(task);
                            if ("1".equals(task.getStatus())) {
                                completed++;
                            }
                        }

                        adapter.notifyDataSetChanged();

                        // Update Statistik
                        int total = taskList.size();
                        txtTotalTask.setText(String.valueOf(total));
                        txtCompletedTask.setText(String.valueOf(completed));

                        // Update Progress
                        if (total > 0) {
                            int percent = (completed * 100) / total;
                            txtProgressPercent.setText(percent + "%");
                            progressTask.setProgress(percent);
                            txtProgressInfo.setText(completed + " of " + total + " tasks completed");
                        } else {
                            txtProgressPercent.setText("0%");
                            progressTask.setProgress(0);
                            txtProgressInfo.setText("No tasks available");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}