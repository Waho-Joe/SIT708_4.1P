package com.example.sit708_41p;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Event> eventList;
    private Context context;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onDeleteClick(Event event);
        void onUpdateClick(Event event);
    }



    public RecyclerViewAdapter(List<Event> eventList, Context context, OnEventClickListener listener) {
        this.eventList = eventList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.event_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.titleTextView.setText(eventList.get(position).getTitle());
        holder.categoryTextView.setText(eventList.get(position).getCategory());
        holder.locationTextView.setText(eventList.get(position).getLocation());
        holder.timeTextView.setText(eventList.get(position).getTime());

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(event);
            }
        });

        holder.updateButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUpdateClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {

        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView categoryTextView;
        TextView locationTextView;
        TextView timeTextView;
        Button updateButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
