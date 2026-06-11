package com.example.sit708_41p;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM events ORDER BY substr(time, 7, 4) ASC, substr(time, 4, 2) ASC, substr(time, 1, 2) ASC, substr(time, 12, 5) ASC")
    List<Event> getAllEvents();

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    Event getEventById(int id);
}