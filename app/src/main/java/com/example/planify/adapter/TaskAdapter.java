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

    private Context context;
    private List<Task> taskList;
    private OnTaskStatusChangedListener listener;

    private static final String URL_DELETE =
            "http://10.0.2.2/planify_api/delete_task.php";

    public interface OnTaskStatusChangedListener {
        void onTaskStatusChanged();
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskStatusChangedListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_task, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = taskList.get(position);

        holder.txtTitle.setText(task.getTitle());
        holder.txtDescription.setText(task.getDescription());
        holder.txtDeadline.setText("Deadline : " + task.getDeadline());

        // Klik card = Edit
        holder.itemView.setOnClickListener(v -> openEdit(task));

        // Klik icon pensil = Edit
        holder.imgEdit.setOnClickListener(v -> openEdit(task));

        // Klik icon sampah = Delete
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

                                        int pos = holder.getAdapterPosition();

                                        if (pos != RecyclerView.NO_POSITION) {
                                            taskList.remove(pos);
                                            notifyItemRemoved(pos);
                                            if (listener != null) {
                                                listener.onTaskStatusChanged();
                                            }
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

    private void openEdit(Task task) {

        Intent intent = new Intent(context, EditTaskActivity.class);

        intent.putExtra("id", task.getId());
        intent.putExtra("title", task.getTitle());
        intent.putExtra("description", task.getDescription());
        intent.putExtra("deadline", task.getDeadline());

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDescription, txtDeadline;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);

            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}