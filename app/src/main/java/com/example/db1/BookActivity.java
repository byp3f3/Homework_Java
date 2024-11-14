package com.example.db1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BookActivity extends AppCompatActivity {


    private TextView author, name;
    private Button delete, edit;
    private DataBaseHelper db;
    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);
        context = this;
        name = findViewById(R.id.editTextName);
        author = findViewById(R.id.editTextAuthor);
        delete= findViewById(R.id.del);
        edit = findViewById(R.id.edit);
        db = new DataBaseHelper(this);

        int id = getIntent().getIntExtra("id", 0);
        String Bookname = getIntent().getStringExtra("name");
        String BookAuthor = getIntent().getStringExtra("author");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result = db.deleteBook(id);
                if(result>0){
                    Toast.makeText(context,"Книга удалена",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(context,"Что-то не так",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(context, RecyclerViewAdapter.class);
                context.startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty() || author.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }
                long result = db.editBook(id, name.getText().toString(), author.getText().toString());
                if(result>0){
                    Toast.makeText(context,"Книга изменена",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(context,"Что-то не так",Toast.LENGTH_SHORT).show();
                }
            }
        });

        name.setText(Bookname);
        author.setText(BookAuthor);
    }
}
