package com.example.authp1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginButton, registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v->loginUser());
        registerButton.setOnClickListener(v->registerUser());
    }

    private void registerUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Заполните все поля для ввода", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(LoginActivity.this, "Неверный формат почты", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6){
            Toast.makeText(LoginActivity.this, "Пароль должен содержать не менее 6 символов", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task->{
            if(task.isSuccessful()){
                Toast.makeText(LoginActivity.this, "Регистрация прошла успешно  ", Toast.LENGTH_SHORT).show();
                saveUserFirestore(email);
            } else {
                Toast.makeText(LoginActivity.this, "Такой email уже есть в системе", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task->{
            if(task.isSuccessful()){
                checkUserRole();
            } else {
                Toast.makeText(LoginActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRole() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String role = documentSnapshot.getString("role");
                        if (role != null) {
                            switch (role) {
                                case "admin":
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    break;
                                case "employee":
                                    startActivity(new Intent(LoginActivity.this, EmployeeActivity.class));
                                    break;
                                case "user":
                                    startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                    break;
                            }
                            finish();
                        }
                    });
        }
    }

    private void saveUserFirestore(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("role", "user");

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .set(user).addOnSuccessListener(a ->
                {
                    Toast.makeText(LoginActivity.this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this, "Ошибка регистрации :(" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
