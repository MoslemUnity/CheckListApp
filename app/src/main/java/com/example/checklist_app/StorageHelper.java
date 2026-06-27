package com.example.checklist_app;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class StorageHelper {
    private static final String PREFS = "checklist_prefs";
    private static final String KEY_CURRENT = "current_tasks";
    private static final String KEY_HISTORY = "history_tasks";

    public static List<Task> loadCurrent(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_CURRENT, null);
        if (json == null || json.isEmpty()) return new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(json);
            List<Task> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                list.add(Task.fromJson(arr.getJSONObject(i)));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveCurrent(Context ctx, List<Task> tasks) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray();
            for (Task t : tasks) {
                arr.put(t.toJson());
            }
            sp.edit().putString(KEY_CURRENT, arr.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveToHistory(Context ctx, List<Task> tasks) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String existingJson = sp.getString(KEY_HISTORY, null);
        JSONArray history = new JSONArray();
        if (existingJson != null && !existingJson.isEmpty()) {
            try {
                history = new JSONArray(existingJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (Task t : tasks) {
            try {
                history.put(t.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        List<Task> all = new ArrayList<>();
        for (int i = 0; i < history.length(); i++) {
            try {
                all.add(Task.fromJson(history.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        all.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        try {
            JSONArray sorted = new JSONArray();
            for (Task t : all) sorted.put(t.toJson());
            sp.edit().putString(KEY_HISTORY, sorted.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Task> loadHistory(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_HISTORY, null);
        if (json == null || json.isEmpty()) return new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(json);
            List<Task> list = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                list.add(Task.fromJson(arr.getJSONObject(i)));
            }
            list.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ این متد را دقیقاً به انتهای کلاس اضافه کنید
    // این متد کل آرایهٔ تاریخچه را پاک و لیست جدید را جایگزین می‌کند (برای حذف تکی)
    public static void saveHistoryList(Context ctx, List<Task> tasks) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        try {
            JSONArray history = new JSONArray();
            for (Task t : tasks) {
                history.put(t.toJson());
            }
            sp.edit().putString(KEY_HISTORY, history.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}