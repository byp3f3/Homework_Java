package com.example.notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.AddEditEventActivity;
import com.example.notification.R;
import com.example.notification.Event;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events;
    private Context context;

    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        holder.titleTextView.setText(event.getTitle());
        holder.descriptionTextView.setText(event.getDescription());

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        holder.dateTextView.setText(format.format(new Date(event.getDateTime())));

        holder.priorityIndicator.setBackgroundColor(event.getPriorityColor());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void sortByDate() {
        Collections.sort(events, (e1, e2) -> Long.compare(e1.getDateTime(), e2.getDateTime()));
        notifyDataSetChanged();
    }

    public void sortByPriority() {
        Collections.sort(events, (e1, e2) -> Integer.compare(e2.getPriority(), e1.getPriority()));
        notifyDataSetChanged();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, dateTextView;
        View priorityIndicator;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            priorityIndicator = itemView.findViewById(R.id.priorityIndicator);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Event event = events.get(position);
                    Intent intent = new Intent(context, AddEditEventActivity.class);
                    intent.putExtra("event_id", event.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}