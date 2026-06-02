package com.example.sit708_41p;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CreateFragment extends Fragment {

    private EditText titleEditText, categoryEditText, locationEditText, timeEditText;

    private Button saveButton;

    private EventDatabase database;

    private long selectedTimeMillis = 0;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        categoryEditText = view.findViewById(R.id.categoryEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        timeEditText = view.findViewById(R.id.timeEditText);
        saveButton = view.findViewById(R.id.saveButton);

        database = EventDatabase.getInstance(requireContext());

        timeEditText.setOnClickListener(v -> showDateTimePicker());

        saveButton.setOnClickListener(v -> saveEvent(view));

        return view;
    }

    private void showDateTimePicker() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (datePicker, year, month, dayOfMonth) -> {

                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireContext(),
                            (timePicker, hourOfDay, minute) -> {
                                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedCalendar.set(Calendar.MINUTE, minute);
                                selectedCalendar.set(Calendar.SECOND, 0);
                                selectedCalendar.set(Calendar.MILLISECOND, 0);

                                selectedTimeMillis = selectedCalendar.getTimeInMillis();

                                SimpleDateFormat sdf = new SimpleDateFormat(
                                        "dd/MM/yyyy HH:mm",
                                        Locale.getDefault()
                                );

                                timeEditText.setText(sdf.format(selectedCalendar.getTime()));
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    );

                    timePickerDialog.show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void saveEvent(View view) {
        String title = titleEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time.isEmpty() || selectedTimeMillis == 0) {
            Toast.makeText(requireContext(), "Please select a date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedTimeMillis < System.currentTimeMillis()) {
            Toast.makeText(requireContext(), "You cannot select a past date/time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.isEmpty()) {
            category = "Other";
        }

        if (location.isEmpty()) {
            location = "No location";
        }

        Event event = new Event(title, category, location, time);

        Executors.newSingleThreadExecutor().execute(() -> {
            database.eventDao().insert(event);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.listFragment);
            });
        });
    }
}