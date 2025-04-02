package com.example.notification;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditEventActivity extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText;
    private Button dateButton, timeButton, saveButton;
    private RadioGroup priorityRadioGroup;
    private Switch notificationSwitch;
    private Calendar calendar;
    private DatabaseHelper db;
    private Event existingEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_event);

        db = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Требуется разрешение")
                    .setMessage("Для работы уведомлений требуется разрешение на точные будильники. Хотите открыть настройки?")
                    .setPositiveButton("Открыть настройки", (dialog, which) -> {
                        Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        startActivity(intent);
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
            }
        }

        createNotificationChannel();

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        priorityRadioGroup = findViewById(R.id.priorityRadioGroup);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.GONE); // Initially hidden

        titleEditText.setHint("Название");
        descriptionEditText.setHint("Описание");
        dateButton.setText("Дата");
        timeButton.setText("Время");
        saveButton.setText("Сохранить");
        notificationSwitch.setText("Уведомление");

        ((RadioButton)findViewById(R.id.lowPriority)).setText(R.string.low_priority);
        ((RadioButton)findViewById(R.id.mediumPriority)).setText(R.string.medium_priority);
        ((RadioButton)findViewById(R.id.highPriority)).setText(R.string.high_priority);

        int eventId = getIntent().getIntExtra("event_id", -1);
        if (eventId != -1) {
            for (Event event : db.getAllEvents()) {
                if (event.getId() == eventId) {
                    existingEvent = event;
                    deleteButton.setVisibility(View.VISIBLE);
                    deleteButton.setOnClickListener(v -> deleteEvent());
                    break;
                }
            }

            if (existingEvent != null) {
                titleEditText.setText(existingEvent.getTitle());
                descriptionEditText.setText(existingEvent.getDescription());
                calendar.setTimeInMillis(existingEvent.getDateTime());
                notificationSwitch.setChecked(existingEvent.isNotified());

                switch (existingEvent.getPriority()) {
                    case 1:
                        priorityRadioGroup.check(R.id.lowPriority);
                        break;
                    case 2:
                        priorityRadioGroup.check(R.id.mediumPriority);
                        break;
                    case 3:
                        priorityRadioGroup.check(R.id.highPriority);
                        break;
                }
            }
        }

        updateDateTimeButtons();

        dateButton.setOnClickListener(v -> showDatePickerDialog());
        timeButton.setOnClickListener(v -> showTimePickerDialog());

        saveButton.setOnClickListener(v -> saveEvent());
    }

    private void deleteEvent() {
        if (existingEvent != null) {
            try {
                cancelNotification(existingEvent);

                db.deleteEvent(existingEvent.getId());
                
                Toast.makeText(this, "Событие удалено", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка удаления события", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTimeButtons();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    updateDateTimeButtons();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void updateDateTimeButtons() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        dateButton.setText(dateFormat.format(calendar.getTime()));
        timeButton.setText(timeFormat.format(calendar.getTime()));
    }

    private void saveEvent() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        long dateTime = calendar.getTimeInMillis();

        int selectedPriorityId = priorityRadioGroup.getCheckedRadioButtonId();
        int priority = 1;

        if (selectedPriorityId == R.id.mediumPriority) {
            priority = 2;
        } else if (selectedPriorityId == R.id.highPriority) {
            priority = 3;
        }

        boolean shouldNotify = notificationSwitch.isChecked();

        if (existingEvent != null) {
            existingEvent.setTitle(title);
            existingEvent.setDescription(description);
            existingEvent.setDateTime(dateTime);
            existingEvent.setPriority(priority);
            existingEvent.setNotified(shouldNotify);
            db.updateEvent(existingEvent);

            if (shouldNotify) {
                scheduleNotification(existingEvent);
            } else {
                cancelNotification(existingEvent);
            }
        } else {
            Event event = new Event(title, description, dateTime, priority, shouldNotify);
            long id = db.addEvent(event);
            event.setId((int) id);

            if (shouldNotify) {
                scheduleNotification(event);
            }
        }

        finish();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NotificationPublisher.NOTIFICATION_CHANNEL_ID,
                    NotificationPublisher.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Уведомления о событиях");
            channel.enableVibration(true);
            channel.enableLights(true);
            
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification(Event event) {
        if (event.getDateTime() <= System.currentTimeMillis()) {
            Toast.makeText(this, "Нельзя установить уведомление на прошедшее время", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Для работы уведомлений требуется разрешение", Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Требуется разрешение на точные будильники", Toast.LENGTH_LONG).show();
                return;
            }
        }

        try {
            Intent notificationIntent = new Intent(this, NotificationPublisher.class);
            notificationIntent.putExtra("event_id", event.getId());
            notificationIntent.putExtra("event_title", event.getTitle());
            notificationIntent.putExtra("event_description", event.getDescription());

            int requestCode = event.getId();

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                long notificationTime = event.getDateTime() - 30000;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            notificationTime,
                            pendingIntent
                    );
                } else {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            notificationTime,
                            pendingIntent
                    );
                }

                AlarmManager.AlarmClockInfo nextAlarm = alarmManager.getNextAlarmClock();
                if (nextAlarm != null) {
                    long nextAlarmTime = nextAlarm.getTriggerTime();
                    if (Math.abs(nextAlarmTime - notificationTime) < 60000) {
                        Toast.makeText(this, "Уведомление установлено на " + 
                            new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                                .format(new Date(event.getDateTime())), 
                            Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Уведомление установлено на другое время", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent verifyIntent = new Intent(this, NotificationPublisher.class);
                    PendingIntent verifyPendingIntent = PendingIntent.getBroadcast(
                            this,
                            event.getId(),
                            verifyIntent,
                            PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
                    );
                    
                    if (verifyPendingIntent != null) {
                        Toast.makeText(this, "Уведомление установлено", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Уведомление установлено", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Ошибка: AlarmManager недоступен", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Ошибка безопасности при установке уведомления", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка установки уведомления: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelNotification(Event event) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                event.getId(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            try {
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            } catch (Exception e) {
            }
        }
    }
}