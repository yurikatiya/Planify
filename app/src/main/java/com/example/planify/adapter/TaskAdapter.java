package com.example.planify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.planify.R;
import com.example.planify.activities.EditTaskActivity;
import com.example.planify.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;
    private Context context;

    private static final String URL_DELETE =
            "http://10.0.2.2/planify_api/delete_task.php";

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = taskList.get(position);

        holder.txtTitle.setText(task.getTitle());
        holder.txtDescription.setText(task.getDescription());
        holder.txtDeadline.setText("Deadline : " + task.getDeadline());

        // Klik card -> Edit
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, EditTaskActivity.class);

            intent.putExtra("id", task.getId());
            intent.putExtra("title", task.getTitle());
            intent.putExtra("description", task.getDescription());
            intent.putExtra("deadline", task.getDeadline());

            context.startActivity(intent);

        });

        // Klik icon sampah
        holder.imgDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Hapus Task")
                    .setMessage("Yakin ingin menghapus task ini?")
                    .setPositiveButton("Hapus", (dialog, which) -> {

                        StringRequest request = new StringRequest(
                                Request.Method.POST,
                                URL_DELETE,

                                response -> {

                                    if (response.trim().equals("success")) {

                                        int adapterPosition = holder.getAdapterPosition();

                                        if (adapterPosition != RecyclerView.NO_POSITION) {
                                            taskList.remove(adapterPosition);
                                            notifyItemRemoved(adapterPosition);
                                        }

                                    }

                                },

                                error -> {

                                }

                        ) {

                            @Override
                            protected Map<String, String> getParams() {

                                Map<String, String> params = new HashMap<>();
                                params.put("id", task.getId());

                                return params;
                            }

                        };

                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(request);

                    })

                    .setNegativeButton("Batal", null)
                    .show();

        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDescription, txtDeadline;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}