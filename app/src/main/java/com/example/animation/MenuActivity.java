package com.example.animation;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeUtils;

public class MenuActivity extends AppCompatActivity {

    private Button frame;
    private Button tween;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        frame = findViewById(R.id.frame);
        tween = findViewById(R.id.tween);
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        Animation scaleAnimation2 = AnimationUtils.loadAnimation(this, R.anim.scale_animation_2);
        frame.startAnimation(scaleAnimation2);
        tween.startAnimation(scaleAnimation);


        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pages_animation_2, R.anim.pages_animation);
            }
        });

        tween.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SecondActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pages_animation_5, R.anim.pages_animation_6
                );
            }
        });
    }
}
