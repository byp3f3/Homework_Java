package com.example.animation;

import static com.example.animation.R.anim.pages_animation_8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private ImageView img;
    private Button startBtn;
    private Button pauseBtn;

    private  Button pageChng;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        img=findViewById(R.id.imgBlink);
        startBtn = findViewById(R.id.start_tween);
        pauseBtn=findViewById(R.id.pause_tween);
        pageChng = findViewById(R.id.pageChange);

        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation);
        Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_animation);
        Animation translateAnimation2 = AnimationUtils.loadAnimation(this, R.anim.translate_animation_2);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);

        startBtn.startAnimation(translateAnimation);
        pauseBtn.startAnimation(rotateAnimation);
        pageChng.startAnimation(translateAnimation2);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.startAnimation(blinkAnimation);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.clearAnimation();
            }
        });

        pageChng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pages_animation_8, R.anim.pages_animation_7);
            }
        });

    }
}
