package com.example.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAddapter extends RecyclerView.Adapter<RecyclerAddapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Person> peopleList;

    private final Context context;

    RecyclerAddapter(Context context, List<Person> peopleList){
        this.peopleList = peopleList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerAddapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAddapter.ViewHolder holder, int position) {
        Person person = peopleList.get(position);
        holder.photoView.setImageResource(person.getPhoto());
        holder.nameView.setText(person.getName());
        holder.ageView.setText(person.getAge());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("flag", person.getPhoto());
                intent.putExtra("nameView", person.getName());
                intent.putExtra("capitalView", person.getAge());
                intent.putExtra("descriptionView", person.getDescription());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView photoView;
        final TextView nameView, ageView;
        ViewHolder(View view){
            super(view);
            photoView = view.findViewById(R.id.flag);
            nameView = view.findViewById(R.id.name);
            ageView = view.findViewById(R.id.capital);
        }
    }
}
