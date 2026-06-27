package com.example.checklist_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public interface OnHistoryDeleteListener {
        void onDeleteTask(Task task);
    }

    private ArrayList<Task> tasks;
    private OnHistoryDeleteListener deleteListener;

    public HistoryAdapter(ArrayList<Task> tasks, OnHistoryDeleteListener listener) {
        this.tasks = tasks;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvTaskText.setText(task.getText());

        // ✅ نمایش تاریخ و ساعت (دقیقاً همان ID که در XML اضافه کردیم)
        holder.tvTaskDate.setText(task.getTimestamp() != null ? task.getTimestamp() : "بدون زمان");

        // ✅ آپدیت وضعیت آیکون چک‌باکس بر اساس وضعیت تکسک
        holder.ivCheckIcon.setImageResource(task.isChecked()
                ? android.R.drawable.checkbox_on_background
                : android.R.drawable.checkbox_off_background);

        holder.btnDeleteHistory.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDeleteTask(task);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateList(ArrayList<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskText;
        TextView tvTaskDate; // ✅ اضافه شد
        ImageButton btnDeleteHistory;
        ImageView ivCheckIcon;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskText = itemView.findViewById(R.id.tvTaskText);
            tvTaskDate = itemView.findViewById(R.id.tvTaskDate); // ✅ اضافه شد
            btnDeleteHistory = itemView.findViewById(R.id.btnDeleteHistory);
            ivCheckIcon = itemView.findViewById(R.id.ivCheckIcon);
        }
    }
}