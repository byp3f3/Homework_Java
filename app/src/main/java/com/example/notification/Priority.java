package com.example.notification;

import android.graphics.Color;

public enum Priority {
    LOW(1, Color.GREEN),
    MEDIUM(2, Color.YELLOW),
    HIGH(3, Color.RED);

    private int value;
    private int color;

    Priority(int value, int color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public int getColor() {
        return color;
    }

    public static Priority fromValue(int value) {
        for (Priority priority : Priority.values()) {
            if (priority.value == value) {
                return priority;
            }
        }
        return LOW;
    }
}