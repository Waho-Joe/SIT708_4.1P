package com.example.sit708_41p;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Event> eventList = new ArrayList<>();
    EventDatabase database;


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.eventRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        recyclerViewAdapter = new RecyclerViewAdapter(
                eventList,
                requireContext(),
                new RecyclerViewAdapter.OnEventClickListener() {
                    @Override
                    public void onDeleteClick(Event event) {
                        deleteEvent(event);
                    }

                    @Override
                    public void onUpdateClick(Event event) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("eventId", event.getId());

                        Navigation.findNavController(requireView())
                                .navigate(R.id.updateFragment, bundle);
                    }
                }
        );
        recyclerView.setAdapter(recyclerViewAdapter);

        database = EventDatabase.getInstance(requireContext());

        loadEvents();
        return view;
    }


    private void loadEvents() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Event> eventsFromDb = database.eventDao().getAllEvents();

            requireActivity().runOnUiThread(() -> {
                eventList.clear();
                eventList.addAll(eventsFromDb);
                recyclerViewAdapter.notifyDataSetChanged();
            });
        });
    }
    private void deleteEvent(Event event) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.eventDao().delete(event);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                loadEvents();
            });
        });
    }
    @Override
    public void onResume() {
        super.onResume();

        if (database != null && recyclerViewAdapter != null) {
            loadEvents();
        }
    }
}