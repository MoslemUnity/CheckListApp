package com.example.checklist_app;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private OnTaskCheckListener listener;

    // ✅ تغییر حیاتی: استفاده از int position به جای Task (جلوگیری از کرش)
    public interface OnTaskCheckListener {
        void onCheckChanged(int position, boolean isChecked);
        void onDeleteTask(int position);
    }

    public TaskAdapter(List<Task> tasks, OnTaskCheckListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvTitle.setText(task.getText());

        // ✅ جلوگیری از اجرای خودکار لیسنر هنگام bind یا اسکرول
        holder.cb.setOnCheckedChangeListener(null);
        holder.cb.setChecked(task.isChecked());

        // ✅ ثبت رویداد تغییر تیک با ارسال ایندکس (نه آبجکت)
        holder.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onCheckChanged(pos, isChecked);
            }
        });

        // ✅ ثبت رویداد حذف با ارسال ایندکس
        holder.ivDelete.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && listener != null) {
                listener.onDeleteTask(pos);
            }
        });

        // ✅ اصلاح رنگ متن
        int textColor = task.isChecked() ? Color.parseColor("#999999") : Color.parseColor("#212121");
        holder.tvTitle.setTextColor(textColor);

        // ✅ اصلاح پرچم خط‌خورده
        int paintFlags = task.isChecked()
                ? holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                : holder.tvTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG;
        holder.tvTitle.setPaintFlags(paintFlags);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb;
        TextView tvTitle;
        ImageView ivDelete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cbTask);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}