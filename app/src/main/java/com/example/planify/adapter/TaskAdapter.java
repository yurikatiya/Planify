package com.example.planify.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String URL_UPDATE_STATUS =
            "http://10.0.2.2/planify_api/update_status.php";

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

        // Set status checkbox
        holder.cbStatus.setOnCheckedChangeListener(null); // Clear listener before setting checked
        holder.cbStatus.setChecked("1".equals(task.getStatus()));

        holder.cbStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String newStatus = isChecked ? "1" : "0";
            updateStatus(task.getId(), newStatus);
        });

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

    private void updateStatus(String id, String status) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_UPDATE_STATUS,
                response -> {
                    if (response.trim().equals("success")) {
                        if (listener != null) {
                            listener.onTaskStatusChanged();
                        }
                    } else {
                        Toast.makeText(context, "Gagal update status", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("status", status);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
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
        CheckBox cbStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);
            cbStatus = itemView.findViewById(R.id.cbStatus);

            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}