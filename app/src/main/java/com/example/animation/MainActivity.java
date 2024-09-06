package com.example.animation;

import static com.example.animation.R.id.*;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ImageView animationIV;
    private Button startBtn;
    private Button pauseBtn;
    private AnimationDrawable frameAnimation;
    private  Button pageChng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        animationIV = findViewById(R.id.animImageView);
        startBtn = findViewById(R.id.startButton);
        pauseBtn = findViewById(R.id.pauseButton);
        pageChng = findViewById(R.id.pageChange);

        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation_2);
        Animation common = AnimationUtils.loadAnimation(this, R.anim.idk_animation);
        startBtn.startAnimation(blinkAnimation);
        pageChng.startAnimation(common);
        frameAnimation = (AnimationDrawable) animationIV.getDrawable();

        startBtn.setOnClickListener(v -> {
            if (!frameAnimation.isRunning()){
                startBtn.clearAnimation();
                frameAnimation.start();
                pauseBtn.startAnimation(blinkAnimation);
            }
        });

        pauseBtn.setOnClickListener(v -> {
            if (frameAnimation.isRunning()){
                pauseBtn.clearAnimation();
                frameAnimation.stop();
                startBtn.startAnimation(blinkAnimation);
            }
        });

        pageChng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pages_animation_4, R.anim.pages_animation_3);
            }
        });




    }
}