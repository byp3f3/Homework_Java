package com.example.notification;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "planner.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EVENTS = "events";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE_TIME = "date_time";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_IS_NOTIFIED = "is_notified";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE_TIME + " INTEGER,"
                + COLUMN_PRIORITY + " INTEGER,"
                + COLUMN_IS_NOTIFIED + " INTEGER)";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public long addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, event.getTitle());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_DATE_TIME, event.getDateTime());
        values.put(COLUMN_PRIORITY, event.getPriority());
        values.put(COLUMN_IS_NOTIFIED, event.isNotified() ? 1 : 0);

        long id = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return id;
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " ORDER BY " + COLUMN_DATE_TIME + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(0));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setDateTime(cursor.getLong(3));
                event.setPriority(cursor.getInt(4));
                event.setNotified(cursor.getInt(5) == 1);

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }

    public List<Event> getEventsByDate(long date) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startOfDay = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        long endOfDay = calendar.getTimeInMillis();

        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE "
                + COLUMN_DATE_TIME + " >= " + startOfDay + " AND "
                + COLUMN_DATE_TIME + " < " + endOfDay
                + " ORDER BY " + COLUMN_DATE_TIME + " ASC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(0));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setDateTime(cursor.getLong(3));
                event.setPriority(cursor.getInt(4));
                event.setNotified(cursor.getInt(5) == 1);

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }

    public List<Event> getEventsForWeek(long date) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startOfWeek = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endOfWeek = calendar.getTimeInMillis();

        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE "
                + COLUMN_DATE_TIME + " >= " + startOfWeek + " AND "
                + COLUMN_DATE_TIME + " <= " + endOfWeek
                + " ORDER BY " + COLUMN_DATE_TIME + " ASC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(0));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setDateTime(cursor.getLong(3));
                event.setPriority(cursor.getInt(4));
                event.setNotified(cursor.getInt(5) == 1);

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }

    public List<Event> getEventsForMonth(int year, int month) {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        long startOfMonth = calendar.getTimeInMillis();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        long endOfMonth = calendar.getTimeInMillis();

        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE "
                + COLUMN_DATE_TIME + " >= " + startOfMonth + " AND "
                + COLUMN_DATE_TIME + " <= " + endOfMonth
                + " ORDER BY " + COLUMN_DATE_TIME + " ASC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(cursor.getInt(0));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setDateTime(cursor.getLong(3));
                event.setPriority(cursor.getInt(4));
                event.setNotified(cursor.getInt(5) == 1);

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventList;
    }

    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, event.getTitle());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_DATE_TIME, event.getDateTime());
        values.put(COLUMN_PRIORITY, event.getPriority());
        values.put(COLUMN_IS_NOTIFIED, event.isNotified() ? 1 : 0);

        db.update(TABLE_EVENTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public boolean deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return false;
    }

    public void markEventAsNotified(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_NOTIFIED, 1);

        db.update(TABLE_EVENTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}