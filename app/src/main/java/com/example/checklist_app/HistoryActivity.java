package com.example.checklist_app;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // ✅ ایمپورت جدید
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvHistory;
    private HistoryAdapter adapter;
    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // ✅ فعال‌سازی دکمهٔ برگشت در Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // ✅ راه‌اندازی RecyclerView و آداپتور
        rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(tasks, this::deleteTaskFromHistory);
        rvHistory.setAdapter(adapter);
        loadHistory();
    }

    private void loadHistory() {
        List<Task> historyData = StorageHelper.loadHistory(this);
        tasks.clear();
        tasks.addAll(historyData);
        adapter.notifyDataSetChanged();
    }

    private void deleteTaskFromHistory(Task task) {
        int index = tasks.indexOf(task);
        if (index != -1) {
            tasks.remove(index);
            adapter.notifyItemRemoved(index);
            StorageHelper.saveHistoryList(this, tasks);
            Toast.makeText(this, "آیتم حذف شد", Toast.LENGTH_SHORT).show();
            if (tasks.isEmpty()) Toast.makeText(this, "تاریخچه خالی شد", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    // ✅ مدیریت کلیک روی دکمهٔ برگشت
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // یا finish() بسته به نیاز ناوبری شما
        return true;
    }
}