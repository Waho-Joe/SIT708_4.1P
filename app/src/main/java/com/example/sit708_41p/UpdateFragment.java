package com.example.sit708_41p;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executors;

public class UpdateFragment extends Fragment {

    private EditText titleEditText,categoryEditText,locationEditText,timeEditText;
    private Button updateButton;
    private EventDatabase database;
    private Event currentEvent;
    private int eventId;

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update, container, false);

        titleEditText = view.findViewById(R.id.updateTitleEditText);
        categoryEditText = view.findViewById(R.id.updateCategoryEditText);
        locationEditText = view.findViewById(R.id.updateLocationEditText);
        timeEditText = view.findViewById(R.id.updateTimeEditText);
        updateButton = view.findViewById(R.id.updateSaveButton);

        database = EventDatabase.getInstance(requireContext());

        if (getArguments() != null) {
            eventId = getArguments().getInt("eventId", -1);
        }

        if (eventId == -1) {
            Toast.makeText(requireContext(), "Event not found", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.listFragment);
            return view;
        }

        loadEvent();

        updateButton.setOnClickListener(v -> updateEvent(view));

        return view;
    }

    private void loadEvent() {
        Executors.newSingleThreadExecutor().execute(() -> {
            currentEvent = database.eventDao().getEventById(eventId);

            requireActivity().runOnUiThread(() -> {
                if (currentEvent != null) {
                    titleEditText.setText(currentEvent.getTitle());
                    categoryEditText.setText(currentEvent.getCategory());
                    locationEditText.setText(currentEvent.getLocation());
                    timeEditText.setText(currentEvent.getTime());
                } else {
                    Toast.makeText(requireContext(), "Event not found", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateEvent(View view) {
        String title = titleEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time.isEmpty()) {
            Toast.makeText(requireContext(), "Time cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentEvent == null) {
            Toast.makeText(requireContext(), "Event not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        currentEvent.setTitle(title);
        currentEvent.setCategory(category);
        currentEvent.setLocation(location);
        currentEvent.setTime(time);

        Executors.newSingleThreadExecutor().execute(() -> {
            database.eventDao().update(currentEvent);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Event updated", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.listFragment);
            });
        });
    }
}