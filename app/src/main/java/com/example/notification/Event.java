package com.example.notification;

import android.graphics.Color;

public class Event {
    private int id;
    private String title;
    private String description;
    private long dateTime;
    private int priority;
    private boolean isNotified;

    public Event() {}

    public Event(String title, String description, long dateTime, int priority, boolean isNotified) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.priority = priority;
        this.isNotified = isNotified;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getDateTime() { return dateTime; }
    public void setDateTime(long dateTime) { this.dateTime = dateTime; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public boolean isNotified() { return isNotified; }
    public void setNotified(boolean notified) { isNotified = notified; }

    public int getPriorityColor() {
        switch (priority) {
            case 1: return Color.GREEN;
            case 2: return Color.YELLOW;
            case 3: return Color.RED;
            default: return Color.GRAY;
        }
    }
}