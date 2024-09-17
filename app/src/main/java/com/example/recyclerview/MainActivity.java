package com.example.recyclerview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Person> people = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerAddapter recyclerAddapter = new RecyclerAddapter(this, people);
        recyclerView.setAdapter(recyclerAddapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setInitialData() {
        people.add(new Person("Валентина","70 лет", R.drawable.p1, "Садовод любитель"));
        people.add(new Person("Михаил","51 год", R.drawable.p2, "Столяр"));
        people.add(new Person("Вячеслав","72 года", R.drawable.p3, "Пенсионер с плохим зрением"));
        people.add(new Person("Дмитрий","20 лет", R.drawable.p4, "Фанат комиксов и яблок"));
        people.add(new Person("Татьяна","31 год", R.drawable.p5, "Мамочка в декрете\uD83D\uDE07"));
    }
}