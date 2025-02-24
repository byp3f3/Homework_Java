package com.example.authp1;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private ListView usersListView;
    private EditText emailField, passwordField;
    private Spinner roleSpinner;
    private Button saveUserButton, deleteUserButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<User> usersList;
    private List<String> userIds;
    private UserAdapter adapter;
    private int selectedPosition = ListView.INVALID_POSITION; // Keep track of the selected position
    private View activityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        usersListView = findViewById(R.id.usersListView);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        roleSpinner = findViewById(R.id.roleSpinner);
        saveUserButton = findViewById(R.id.saveUserButton);
        deleteUserButton = findViewById(R.id.deleteUserButton);
        activityRootView = findViewById(R.id.activity_admin_root);

        usersList = new ArrayList<>();
        userIds = new ArrayList<>();
        adapter = new UserAdapter(this, usersList);
        usersListView.setAdapter(adapter);

        usersListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Настройка Spinner для выбора роли
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.roles_array, // Массив строк в res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        loadUsers();

        saveUserButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String role = roleSpinner.getSelectedItem().toString(); // Получаем выбранную роль

            if (email.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка валидации email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Неверный формат почты", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedPosition != ListView.INVALID_POSITION) {
                // Обновление существующего пользователя
                String userId = userIds.get(selectedPosition);
                updateUser(userId, email, role);
            } else {
                if (password.isEmpty()) {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Проверка длины пароля
                if (password.length() < 6) {
                    Toast.makeText(this, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Добавление нового пользователя
                createUserWithEmailAndPassword(email, password, role);
            }
            hideKeyboard(); // Скрыть клавиатуру после сохранения
        });

        deleteUserButton.setOnClickListener(v -> {
            if (selectedPosition != ListView.INVALID_POSITION) {
                String userId = userIds.get(selectedPosition);
                deleteUser(userId);
            } else {
                Toast.makeText(this, "Выберите пользователя для удаления", Toast.LENGTH_SHORT).show();
            }
            hideKeyboard(); // Скрыть клавиатуру после удаления
        });

        usersListView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == selectedPosition) {
                passwordField.setEnabled(true);
                usersListView.setItemChecked(position, false);
                selectedPosition = ListView.INVALID_POSITION;
                emailField.setText("");
                passwordField.setText("");
                roleSpinner.setSelection(0); // Сбросить выбор роли
                hideKeyboard();
            } else {
                // Выбор нового элемента
                passwordField.setEnabled(false);
                usersListView.setItemChecked(position, true);
                selectedPosition = position;
                User user = usersList.get(position);
                emailField.setText(user.getEmail());
                passwordField.setText(""); // Пароль не отображается для безопасности
                // Установка выбранной роли в Spinner
                String role = user.getRole();
                int rolePosition = roleAdapter.getPosition(role);
                roleSpinner.setSelection(rolePosition);

                showKeyboard(emailField);
            }
        });

        // Слушатель видимости клавиатуры
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
            if (heightDiff > 100) { // Если более 100 пикселей, вероятно, клавиатура открыта
                adjustLayoutForKeyboard(true);
            } else {
                adjustLayoutForKeyboard(false);
            }
        });
    }

    private void loadUsers() {
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                usersList.clear();
                userIds.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    usersList.add(new User(
                            document.getId(),
                            document.getString("email"),
                            document.getString("role")
                    ));
                    userIds.add(document.getId());
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Ошибка загрузки пользователей", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserWithEmailAndPassword(String email, String password, String role) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Пользователь успешно создан в Firebase Authentication
                        String userId = mAuth.getCurrentUser().getUid();
                        saveUserToFirestore(userId, email, role);
                        logAdminAction("createUser", email, role); // Логирование создания пользователя
                    } else {
                        Toast.makeText(this, "Ошибка создания пользователя: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String userId, String email, String role) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("role", role);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Пользователь добавлен", Toast.LENGTH_SHORT).show();
                    loadUsers();
                    emailField.setText("");
                    passwordField.setText("");
                    roleSpinner.setSelection(0); // Сбросить выбор роли
                    clearSelection(); // Сбросить выделение после добавления
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка сохранения пользователя: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUser(String userId, String email, String role) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("role", role);

        db.collection("users").document(userId).update(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Пользователь обновлен", Toast.LENGTH_SHORT).show();
                    loadUsers();
                    emailField.setText("");
                    passwordField.setText("");
                    roleSpinner.setSelection(0); // Сбросить выбор роли
                    clearSelection(); // Сбросить выделение после обновления
                    logAdminAction("updateUser", email, role); // Логирование обновления пользователя
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка обновления пользователя", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteUser(String userId) {
        db.collection("users").document(userId).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Пользователь удален", Toast.LENGTH_SHORT).show();
                    loadUsers();
                    emailField.setText("");
                    passwordField.setText("");
                    roleSpinner.setSelection(0); // Сбросить выбор роли
                    clearSelection(); // Сбросить выделение после удаления
                    logAdminAction("deleteUser", emailField.getText().toString(), roleSpinner.getSelectedItem().toString()); // Логирование удаления пользователя
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка удаления пользователя", Toast.LENGTH_SHORT).show();
                });
    }

    private void logAdminAction(String action, String email, String role) {
        AdminLog log = new AdminLog(action, email, role, System.currentTimeMillis());
        db.collection("adminLogs").add(log)
                .addOnSuccessListener(documentReference -> {
                    // Лог успешно записан
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Ошибка записи лога: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearSelection() {
        selectedPosition = ListView.INVALID_POSITION;
        usersListView.clearChoices();
        adapter.notifyDataSetChanged();
    }

    private void adjustLayoutForKeyboard(boolean isKeyboardVisible) {
        if (isKeyboardVisible) {
            // Переместить поля и кнопки выше клавиатуры
            saveUserButton.setTranslationY(-200); // Пример: переместить на 200 пикселей вверх
            deleteUserButton.setTranslationY(-200);
            emailField.setTranslationY(-200);
            passwordField.setTranslationY(-200);
        } else {
            // Вернуть поля и кнопки на место
            saveUserButton.setTranslationY(0);
            deleteUserButton.setTranslationY(0);
            emailField.setTranslationY(0);
            passwordField.setTranslationY(0);
        }
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailField.getWindowToken(), 0);
    }
}