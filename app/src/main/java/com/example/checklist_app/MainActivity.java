package com.example.checklist_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskCheckListener {
    private EditText etTask;
    private RecyclerView rvTasks;
    private Button btnSave, btnHistory;
    private TaskAdapter adapter;
    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadTasks();
    }

    private void initViews() {
        etTask = findViewById(R.id.etTask);
        rvTasks = findViewById(R.id.rvTasks);
        btnSave = findViewById(R.id.btnSave);
        btnHistory = findViewById(R.id.btnHistory);

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(tasks, this);
        rvTasks.setAdapter(adapter);

        findViewById(R.id.btnAdd).setOnClickListener(v -> addTask());
        btnSave.setOnClickListener(v -> saveToHistory());
        btnHistory.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
    }

    private void loadTasks() {
        tasks.clear();
        tasks.addAll(StorageHelper.loadCurrent(this));
        adapter.notifyDataSetChanged();
    }

    private void addTask() {
        String text = etTask.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "لطفا عنوان کار را وارد کنید", Toast.LENGTH_SHORT).show();
            return;
        }
        String newId = UUID.randomUUID().toString();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        tasks.add(new Task(newId, text, false, timestamp));
        adapter.notifyItemInserted(tasks.size() - 1);
        etTask.setText("");
        StorageHelper.saveCurrent(this, tasks);
    }

    private void saveToHistory() {
        if (tasks.isEmpty()) {
            Toast.makeText(this, "لیست خالی است", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Task t : tasks) t.setChecked(true);
        StorageHelper.saveToHistory(this, tasks);
        tasks.clear();
        adapter.notifyDataSetChanged();
        StorageHelper.saveCurrent(this, tasks);
        Toast.makeText(this, "با موفقیت به تاریخچه منتقل شد", Toast.LENGTH_SHORT).show();
    }

    // ✅ ✅ ✅ نسخهٔ اصلاح‌شده و هماهنگ با TaskAdapter ✅ ✅ ✅
    @Override
    public void onCheckChanged(int position, boolean isChecked) {
        if (position >= 0 && position < tasks.size()) {
            tasks.get(position).setChecked(isChecked);
            StorageHelper.saveCurrent(this, tasks);
            // ⛔️ notifyItemChanged حذف شد: آداپتور خودش UI را مدیریت می‌کند
        }
    }

    @Override
    public void onDeleteTask(int position) {
        if (position >= 0 && position < tasks.size()) {
            tasks.remove(position);
            adapter.notifyItemRemoved(position);
            StorageHelper.saveCurrent(this, tasks);
            Toast.makeText(this, "کار حذف شد", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StorageHelper.saveCurrent(this, tasks);
    }
}