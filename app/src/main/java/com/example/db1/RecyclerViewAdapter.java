package com.example.db1;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<Book> bookArrayList;
    public RecyclerViewAdapter(Context context, ArrayList<Book> bookArrayList){
        this.context = context;
        this.bookArrayList=bookArrayList;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Book book  =bookArrayList.get(position);
        holder.bookName.setText(book.getBook_Name());
        holder.bookAuthor.setText(book.getBook_Author());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, BookActivity.class);
                intent.putExtra("id", book.getID_Book());
                intent.putExtra("name", book.getBook_Name());
                intent.putExtra("author", book.getBook_Author());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  bookName, bookAuthor;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            bookName = itemView.findViewById(R.id.b_name);
            bookAuthor = itemView.findViewById(R.id.b_author);
        }
    }
}
