package com.example.authp1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private ListView servicesListView;
    private Button bookServiceButton;
    private FirebaseFirestore db;
    private List<String> servicesList;
    private List<String> serviceIds;
    private ArrayAdapter<String> adapter;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        db = FirebaseFirestore.getInstance();
        servicesListView = findViewById(R.id.servicesListView);
        bookServiceButton = findViewById(R.id.bookServiceButton);
        servicesList = new ArrayList<>();
        serviceIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, servicesList);
        servicesListView.setAdapter(adapter);

        servicesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        calendar = Calendar.getInstance();

        bookServiceButton.setOnClickListener(v -> {
            int selectPosition = servicesListView.getCheckedItemPosition();
            if (selectPosition != ListView.INVALID_POSITION) {
                String serviceId = serviceIds.get(selectPosition);
                showDateTimePickerDialog(serviceId);
            } else {
                Toast.makeText(this, "Выберите услугу", Toast.LENGTH_SHORT).show();
            }
        });

        // Загрузка услуг из Firestore
        loadServices();
    }

    private void loadServices() {
        db.collection("services").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        servicesList.add(document.getString("serviceName"));
                        serviceIds.add(document.getId());
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка загрузки услуг", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDateTimePickerDialog(String serviceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите дату и время для записи");
        View view = getLayoutInflater().inflate(R.layout.dialog_date_time_picker, null);
        builder.setView(view);
        TextView dateField = view.findViewById(R.id.dateField);
        TextView timeField = view.findViewById(R.id.timeField);

        dateField.setOnClickListener(v -> showDatePickerDialog(dateField));
        timeField.setOnClickListener(v -> showTimePickerDialog(timeField));

        builder.setPositiveButton("Записаться", ((dialog, which) -> {
            String date = dateField.getText().toString().trim();
            String time = timeField.getText().toString().trim();
            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(UserActivity.this, "Выберите дату и время", Toast.LENGTH_SHORT).show();
                return;
            }
            bookService(serviceId, date, time);
        }));
        builder.setNegativeButton("Отмена", ((dialog, which) -> dialog.dismiss()));
        builder.create().show();
    }

    private void showTimePickerDialog(TextView timeField) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            updateTimeField(timeField);
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void showDatePickerDialog(TextView dateField) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateField(dateField);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateField(TextView dateField) {
        String myFormat = "dd/MM/yyyy"; // Формат даты
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dateField.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeField(TextView timeField) {
        String myFormat = "HH:mm"; // Формат времени
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        timeField.setText(sdf.format(calendar.getTime()));
    }

    private void bookService(String serviceId, String date, String time) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String clientName = user.getEmail();
            db.collection("services").document(serviceId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String serviceName = documentSnapshot.getString("serviceName");
                            Map<String, Object> appointment = new HashMap<>();

                            appointment.put("clientId", user.getUid());
                            appointment.put("clientName", clientName);
                            appointment.put("serviceId", serviceId);
                            appointment.put("serviceName", serviceName);
                            appointment.put("date", date);
                            appointment.put("time", time);

                            db.collection("appointments").add(appointment)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Запись создана", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(UserActivity.this, "Услуга не найдена", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(UserActivity.this, "Ошибка загрузки услуги", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(UserActivity.this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
        }
    }
}