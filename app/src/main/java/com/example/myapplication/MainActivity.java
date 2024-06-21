package com.example.myapplication;

import static android.view.View.*;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button one, two, three, four, five, six, seven, eight, nine, zero, plus, minus,
            division, multi, result, clear, root, square, percent;
    private TextView formula, endresult;

    private double valueFirst = Double.NaN;

    private double valueSecond;

    private char Action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupView();

        View.OnClickListener numberClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                formula.setText(formula.getText().toString() + button.getText().toString());

            }
        };

        one.setOnClickListener(numberClickListener);
        two.setOnClickListener(numberClickListener);
        three.setOnClickListener(numberClickListener);
        four.setOnClickListener(numberClickListener);
        five.setOnClickListener(numberClickListener);
        six.setOnClickListener(numberClickListener);
        seven.setOnClickListener(numberClickListener);
        eight.setOnClickListener(numberClickListener);
        nine.setOnClickListener(numberClickListener);
        zero.setOnClickListener(numberClickListener);

        View.OnClickListener actionClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Calculate();
                Action = button.getText().charAt(0);
                formula.setText(String.valueOf(valueFirst) + Action);
                endresult.setText("0");
            }
        };

        minus.setOnClickListener(actionClickListener);
        plus.setOnClickListener(actionClickListener);
        division.setOnClickListener(actionClickListener);
        multi.setOnClickListener(actionClickListener);
        square.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Calculate();
                Action = button.getText().charAt(0);
                formula.setText(String.valueOf(valueFirst) + Action + 2);
                endresult.setText("0");
            }
        });

        View.OnClickListener actionFirstClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Action = button.getText().charAt(0);
                formula.setText(String.valueOf(Action));
                endresult.setText("0");
                valueFirst = Double.NaN;
            }
        };
        percent.setOnClickListener(actionFirstClickListener);
        root.setOnClickListener(actionFirstClickListener);

        result.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calculate();
                Action = '=';
                endresult.setText(String.valueOf(valueFirst));
                formula.setText(null);
            }
        });

        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                endresult.setText("0");
                formula.setText(null);
                valueFirst = Double.NaN;
                valueSecond = Double.NaN;
            }
        });
    }

    private void setupView(){
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        zero = (Button) findViewById(R.id.zero);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        division = (Button) findViewById(R.id.devision);
        multi = (Button) findViewById(R.id.multi);
        result = (Button) findViewById(R.id.result);
        root = (Button) findViewById(R.id.root);
        square = (Button) findViewById(R.id.square);
        percent = (Button) findViewById(R.id.percent);
        clear = (Button) findViewById(R.id.clear);
        formula = (TextView) findViewById(R.id.formula);
        endresult = (TextView) findViewById(R.id.endresult);
    }


    private void Calculate(){
        String textFormula = formula.getText().toString();
        int indexAction = textFormula.indexOf(Action);
        if(!Double.isNaN(valueFirst)){
            try {
                if (indexAction != -1) {
                    String numberValue = textFormula.substring(indexAction + 1);
                    valueSecond = Double.parseDouble(numberValue);
                    switch (Action) {
                        case '+':
                            valueFirst += valueSecond;
                            break;
                        case '-':
                            valueFirst -= valueSecond;
                            break;
                        case '*':
                            valueFirst *= valueSecond;
                            break;
                        case '/':
                            if (valueSecond == 0) {
                                valueFirst = 0.0;
                            } else {
                                valueFirst /= valueSecond;
                            }
                            break;
                        case '^':
                            valueFirst = Math.pow(valueFirst, 2);
                            break;
                        case '=':
                            valueFirst = valueSecond;
                            break;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else if (Action == '%' || Action == '√') {
            try {
                if (indexAction != -1) {
                    String numberValue = textFormula.substring(indexAction + 1);
                    valueSecond = Double.parseDouble(numberValue);
                    switch (Action) {
                        case '√':
                            valueFirst = Math.sqrt(valueSecond);
                            break;
                        case '%':
                            valueFirst = valueSecond/100;
                            break;
                        case '=':
                            valueFirst = valueSecond;
                            break;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else {
            try{
                valueFirst = Double.parseDouble(formula.getText().toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        endresult.setText(String.valueOf(valueFirst));
        formula.setText("");
    }
}