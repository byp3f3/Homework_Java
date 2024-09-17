package com.example.recyclerview;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ItemActivity extends AppCompatActivity {

    private ImageView photo_view;
    private TextView name_view;
    private TextView age_view;
    private TextView description_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_page);

        photo_view = findViewById(R.id.flag);
        name_view = findViewById(R.id.name);
        age_view = findViewById(R.id.age);
        description_view = findViewById(R.id.description);

        int flag = getIntent().getIntExtra("flag", 0);
        String nameView = getIntent().getStringExtra("nameView");
        String capitalView = getIntent().getStringExtra("capitalView");
        String descriptionView = getIntent().getStringExtra("descriptionView");

        photo_view.setImageResource(flag);
        name_view.setText(nameView);
        age_view.setText(capitalView);
        description_view.setText(descriptionView);
    }
}
