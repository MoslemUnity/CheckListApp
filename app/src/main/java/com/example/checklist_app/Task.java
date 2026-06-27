package com.example.checklist_app;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
    private String id;
    private String text;
    private boolean isChecked;
    private String timestamp;

    public Task(String id, String text, boolean isChecked, String timestamp) {
        this.id = id;
        this.text = text;
        this.isChecked = isChecked;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    // فقط این متد JSONException پرتاب می‌کند، پس throws دارد
    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id != null ? id : "");
        obj.put("text", text != null ? text : "");
        obj.put("checked", isChecked);
        obj.put("timestamp", timestamp != null ? timestamp : "");
        return obj;
    }

    // از متدهای opt استفاده شده که هرگز JSONException پرتاب نمی‌کنند
    public static Task fromJson(JSONObject obj) {
        return new Task(
                obj.optString("id", ""),
                obj.optString("text", ""),
                obj.optBoolean("checked", false),
                obj.optString("timestamp", "")
        );
    }
}