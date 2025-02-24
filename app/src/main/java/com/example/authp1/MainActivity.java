package com.example.authp1;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button servicesButton = findViewById(R.id.button3);
        Button employeesButton = findViewById(R.id.button4);

        servicesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EmployeeActivity.class);
            startActivity(intent);
        });

        employeesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
        });

    }
}