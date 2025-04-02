package com.example.notification;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.EventAdapter;
import com.example.notification.DatabaseHelper;
import com.example.notification.Event;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView eventsRecyclerView;
    private EventAdapter adapter;
    private DatabaseHelper db;
    private Button dayView, weekView, monthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dayView = findViewById(R.id.dayView);
        weekView = findViewById(R.id.weekView);
        monthView = findViewById(R.id.monthView);

        dayView.setText("День");
        weekView.setText("Неделя");
        monthView.setText("Месяц");

        dayView.setSelected(true);

        dayView.setOnClickListener(v -> {
            dayView.setSelected(true);
            weekView.setSelected(false);
            monthView.setSelected(false);
            loadEventsForDay(System.currentTimeMillis());
        });

        weekView.setOnClickListener(v -> {
            dayView.setSelected(false);
            weekView.setSelected(true);
            monthView.setSelected(false);
            loadEventsForWeek(System.currentTimeMillis());
        });

        monthView.setOnClickListener(v -> {
            dayView.setSelected(false);
            weekView.setSelected(false);
            monthView.setSelected(true);
            Calendar calendar = Calendar.getInstance();
            loadEventsForMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        });

        loadEventsForDay(System.currentTimeMillis());
    }

    private void loadEventsForDay(long date) {
        List<Event> events = db.getEventsByDate(date);
        adapter = new EventAdapter(events, this);
        eventsRecyclerView.setAdapter(adapter);
    }

    private void loadEventsForWeek(long date) {
        List<Event> events = db.getEventsForWeek(date);
        adapter = new EventAdapter(events, this);
        eventsRecyclerView.setAdapter(adapter);
    }

    private void loadEventsForMonth(int year, int month) {
        List<Event> events = db.getEventsForMonth(year, month);
        adapter = new EventAdapter(events, this);
        eventsRecyclerView.setAdapter(adapter);
    }

    public void onAddEventClick(View view) {
        Intent intent = new Intent(this, AddEditEventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_by_date) {
            if (adapter != null) {
                adapter.sortByDate();
            }
            return true;
        } else if (id == R.id.sort_by_priority) {
            if (adapter != null) {
                adapter.sortByPriority();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dayView.isSelected()) {
            loadEventsForDay(System.currentTimeMillis());
        } else if (weekView.isSelected()) {
            loadEventsForWeek(System.currentTimeMillis());
        } else {
            Calendar calendar = Calendar.getInstance();
            loadEventsForMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        }
    }
}