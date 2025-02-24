package com.example.authp1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeActivity extends AppCompatActivity {


    private ListView servicesListView;
    private EditText serviceNameText;
    private EditText serviceCategoryText;
    private EditText searchServiceText;
    private Spinner categoryFilterSpinner;
    private Button saveServiceButton;
    private Button deleteServiceButton;
    private FirebaseFirestore db;
    private List<Service> servicesList;
    private List<Service> filteredServicesList; // Список для фильтрации
    private List<String> serviceIds;
    private ServiceAdapter adapter;
    private int selectedPosition = ListView.INVALID_POSITION;
    private View activityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);

        db = FirebaseFirestore.getInstance();
        servicesListView = findViewById(R.id.servicesListView);
        saveServiceButton = findViewById(R.id.save);
        deleteServiceButton = findViewById(R.id.delete);
        serviceNameText = findViewById(R.id.serviceName);
        serviceCategoryText = findViewById(R.id.serviceCategory);
        searchServiceText = findViewById(R.id.searchService);
        categoryFilterSpinner = findViewById(R.id.categoryFilter);
        servicesList = new ArrayList<>();
        filteredServicesList = new ArrayList<>();
        serviceIds = new ArrayList<>();
        adapter = new ServiceAdapter(this, filteredServicesList);
        servicesListView.setAdapter(adapter);
        activityRootView = findViewById(R.id.activity_employee_root);

        servicesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        loadServices();

        // Поиск по названию
        searchServiceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterServicesByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Фильтр по категории
        categoryFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                filterServicesByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        saveServiceButton.setOnClickListener(v -> {
            String serviceName = serviceNameText.getText().toString().trim();
            String serviceCategory = serviceCategoryText.getText().toString().trim();
            if (serviceName.isEmpty() || serviceCategory.isEmpty()) {
                Toast.makeText(this, "Введите название и категорию услуги", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedPosition != ListView.INVALID_POSITION) {
                String serviceId = serviceIds.get(selectedPosition);
                updateService(serviceId, serviceName, serviceCategory);
            } else {
                addService(serviceName, serviceCategory);
            }
            hideKeyboard();
        });

        deleteServiceButton.setOnClickListener(v -> {
            if (selectedPosition != ListView.INVALID_POSITION) {
                String serviceId = serviceIds.get(selectedPosition);
                deleteService(serviceId);
            } else {
                Toast.makeText(this, "Выберите услугу для удаления", Toast.LENGTH_SHORT).show();
            }
            hideKeyboard();
        });

        servicesListView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == selectedPosition) {
                servicesListView.setItemChecked(position, false);
                selectedPosition = ListView.INVALID_POSITION;
                serviceNameText.setText("");
                serviceCategoryText.setText("");
                hideKeyboard();
            } else {
                servicesListView.setItemChecked(position, true);
                selectedPosition = position;
                Service selectedService = filteredServicesList.get(position);
                serviceNameText.setText(selectedService.getServiceName());
                serviceCategoryText.setText(selectedService.getCategory());
                showKeyboard(serviceNameText);
            }
        });

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
            if (heightDiff > 100) {
                adjustLayoutForKeyboard(true);
            } else {
                adjustLayoutForKeyboard(false);
            }
        });
    }

    private void loadServices() {
        db.collection("services").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                servicesList.clear();
                serviceIds.clear();
                List<String> categories = new ArrayList<>();
                categories.add("Все категории"); // Добавляем опцию "Все категории"

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String serviceName = document.getString("serviceName");
                    String category = document.getString("category");
                    Service service = new Service(servicesList.size(), serviceName, category);
                    servicesList.add(service);
                    serviceIds.add(document.getId());

                    if (!categories.contains(category)) {
                        categories.add(category);
                    }
                }

                // Обновляем Spinner с категориями
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryFilterSpinner.setAdapter(spinnerAdapter);

                // Обновляем список услуг
                filterServicesByName("");
                filterServicesByCategory("Все категории");
            } else {
                Toast.makeText(this, "Ошибка загрузки услуг", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterServicesByName(String query) {
        filteredServicesList.clear();
        for (Service service : servicesList) {
            if (service.getServiceName().toLowerCase().contains(query.toLowerCase())) {
                filteredServicesList.add(service);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void filterServicesByCategory(String category) {
        if (category.equals("Все категории")) {
            filteredServicesList.clear();
            filteredServicesList.addAll(servicesList);
        } else {
            filteredServicesList.clear();
            for (Service service : servicesList) {
                if (service.getCategory().equals(category)) {
                    filteredServicesList.add(service);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void addService(String serviceName, String category) {
        Map<String, Object> service = new HashMap<>();
        service.put("serviceName", serviceName);
        service.put("category", category);

        db.collection("services").add(service)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Услуга добавлена", Toast.LENGTH_SHORT).show();
                    loadServices();
                    serviceNameText.setText("");
                    serviceCategoryText.setText("");
                    clearSelection(); // Clear selection after adding
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка добавления услуги", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateService(String serviceId, String serviceName, String category) {
        Map<String, Object> service = new HashMap<>();
        service.put("serviceName", serviceName);
        service.put("category", category);

        db.collection("services").document(serviceId).update(service)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Услуга обновлена", Toast.LENGTH_SHORT).show();
                    loadServices();
                    serviceNameText.setText("");
                    serviceCategoryText.setText("");
                    clearSelection(); // Clear selection after updating
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка обновления услуги", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteService(String serviceId) {
        db.collection("services").document(serviceId).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Услуга удалена", Toast.LENGTH_SHORT).show();
                    loadServices();
                    serviceNameText.setText("");
                    serviceCategoryText.setText("");
                    clearSelection(); // Clear selection after deleting
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка удаления услуги", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearSelection() {
        selectedPosition = ListView.INVALID_POSITION;
        servicesListView.clearChoices();
        adapter.notifyDataSetChanged();
    }

    private void adjustLayoutForKeyboard(boolean isKeyboardVisible) {
        if (isKeyboardVisible) {
            saveServiceButton.setTranslationY(-200);
            deleteServiceButton.setTranslationY(-200);
            serviceNameText.setTranslationY(-200);
            serviceCategoryText.setTranslationY(-200);
        } else {
            saveServiceButton.setTranslationY(0);
            deleteServiceButton.setTranslationY(0);
            serviceNameText.setTranslationY(0);
            serviceCategoryText.setTranslationY(0);
        }
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(serviceNameText.getWindowToken(), 0);
    }
}