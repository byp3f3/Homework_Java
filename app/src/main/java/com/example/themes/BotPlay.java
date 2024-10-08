package com.example.themes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BotPlay extends AppCompatActivity {

    Button exit;
    Button b1,b2, b3, b4, b5, b6, b7, b8, b9;

    boolean isWinner = true;
    int step;
    int bot;
    int player;
    TextView count;
    TextView turn;
    Button[] buttons;
    String user = "X";
    String robot = "O";

    Button clear;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = this.getSharedPreferences("krestiki", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setContentView(R.layout.play);
        exit = findViewById(R.id.exit);
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        b8 = findViewById(R.id.button8);
        b9 = findViewById(R.id.button9);
        buttons = new Button[]{b1, b2, b3, b4, b5, b6, b7, b8, b9};
        count = findViewById(R.id.count);

        player = sharedPreferences.getInt("pointsH", 0);
        bot = sharedPreferences.getInt("pointsB", 0);
        count.setText(player + " : " +bot);

        turn = findViewById(R.id.turn);
        clear = findViewById(R.id.clear);
        turn.setText("Ваш ход");

        b1.setOnClickListener(actionClickListener);
        b2.setOnClickListener(actionClickListener);
        b3.setOnClickListener(actionClickListener);
        b4.setOnClickListener(actionClickListener);
        b5.setOnClickListener(actionClickListener);
        b6.setOnClickListener(actionClickListener);
        b7.setOnClickListener(actionClickListener);
        b8.setOnClickListener(actionClickListener);
        b9.setOnClickListener(actionClickListener);


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player =0;
                editor.putInt("pointsH", player);
                editor.apply();
                bot = 0;
                editor.putInt("pointsB", bot);
                editor.apply();
                count.setText(player + " : " +bot);
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BotPlay.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener actionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            button.setText(user);
            button.setEnabled(false);
            step++;
            if (WinCheck() == 1){
                player++;
                count.setText(player + " : " + bot);
                editor.putInt("pointsH", player);
                editor.apply();
                for (Button but:buttons
                ) {
                    but.setText(null);
                    but.setEnabled(true);
                }
                step = 0;
                return;
            }
            if (step < 9){
                turn.setText(null);
                Random random = new Random();
                int buttonNum = random.nextInt(9);
                while (!buttons[buttonNum].isEnabled())
                    buttonNum = random.nextInt(9);
                buttons[buttonNum].setText(robot);
                buttons[buttonNum].setEnabled(false);
                step++;
                if (WinCheck() == 1){
                    bot++;
                    editor.putInt("pointsB", bot);
                    editor.apply();
                    count.setText(player + " : " + bot);
                    for (Button but:buttons
                    ) {
                        but.setText(null);
                        but.setEnabled(true);
                    }
                    buttonNum = random.nextInt(9);
                    while (!buttons[buttonNum].isEnabled())
                        buttonNum = random.nextInt(9);
                    buttons[buttonNum].setText(robot);
                    buttons[buttonNum].setEnabled(false);
                    step = 1;
                }
                if (step == 9){
                    for (Button but:buttons
                    ) {
                        but.setText(null);
                        but.setEnabled(true);
                    }
                    step = 0;
                }
                turn.setText("Ваш ход");
            }
            if (step == 9){
                for (Button but:buttons
                ) {
                    but.setText(null);
                    but.setEnabled(true);
                }
                step = 0;
            }

        }

    };

    private int WinCheck() {
        Button[][] winningCombinations = {
                {buttons[0], buttons[1], buttons[2]},
                {buttons[3], buttons[4], buttons[5]},
                {buttons[6], buttons[7], buttons[8]},
                {buttons[0], buttons[3], buttons[6]},
                {buttons[1], buttons[4], buttons[7]},
                {buttons[2], buttons[5], buttons[8]},
                {buttons[0], buttons[4], buttons[8]},
                {buttons[2], buttons[4], buttons[6]}
        };

        for (Button[] combination : winningCombinations) {
            if (combination[0].getText().equals(combination[1].getText()) &&
                    combination[1].getText().equals(combination[2].getText()) &&
                    !combination[0].getText().toString().isEmpty()) {
                return 1;
            }
        }

        return 0;
    }
}


