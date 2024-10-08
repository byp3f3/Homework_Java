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

public class TogetherPlay extends AppCompatActivity {

    Button exit;
    TextView turn;
    TextView count;
    Button b1,b2, b3, b4, b5, b6, b7, b8, b9;
    String symbol = "0";
    int step = 0;
    Button[] buttons;
    String user;
    int player = 0;
    int player2 = 0;
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
        count = findViewById(R.id.count);
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
        clear = findViewById(R.id.clear);

        player = sharedPreferences.getInt("points1", 0);
        player2 = sharedPreferences.getInt("points2", 0);
        count.setText(player + " : " + player2);

        for (Button but:buttons
        ) {
            but.setText(null);
            but.setEnabled(true);
        }

        turn = findViewById(R.id.turn);
        userName();

        View.OnClickListener actionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                button.setText(symbol);
                button.setEnabled(false);
                step++;

                if (WinCheck() == 1){
                    if(turn.getText() == "Игрок 1"){
                        turn.setText("Игрок 2");
                        player++;
                        count.setText(player + " : " + player2);
                        editor.putInt("points1", player);
                        editor.apply();
                        for (Button but:buttons
                        ) {
                            but.setText(null);
                            but.setEnabled(true);
                        }
                        step = 0;
                    }
                    else{
                        turn.setText("Игрок 1");
                        player2++;
                        editor.putInt("points2", player2);
                        editor.apply();
                        count.setText(player + " : " + player2);
                        for (Button but:buttons
                        ) {
                            but.setText(null);
                            but.setEnabled(true);
                        }
                        step = 0;
                    }

                }if(step == 9){
                    for (Button but:buttons
                    ) {
                        but.setText(null);
                        but.setEnabled(true);
                    }
                }
                userName();
                XorO();
            }
        };

        b1.setOnClickListener(actionClickListener);
        b2.setOnClickListener(actionClickListener);
        b3.setOnClickListener(actionClickListener);
        b4.setOnClickListener(actionClickListener);
        b5.setOnClickListener(actionClickListener);
        b6.setOnClickListener(actionClickListener);
        b7.setOnClickListener(actionClickListener);
        b8.setOnClickListener(actionClickListener);
        b9.setOnClickListener(actionClickListener);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TogetherPlay.this, MainActivity.class);
                startActivity(intent);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player =0;
                editor.putInt("points1", player);
                editor.apply();
                player2 = 0;
                editor.putInt("points2", player2);
                editor.apply();
                count.setText(player + " : " +player2);
            }
        });

    }

    private void userName() {
        if (turn.getText() == "Игрок 1"){
            turn.setText("Игрок 2");
        }
        else turn.setText("Игрок 1");
    }
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

    private void XorO() {
        if (symbol == "X"){
            symbol = "0";
        }
        else
        {
            symbol = "X";
        }
    }

}
