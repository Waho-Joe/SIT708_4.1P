# SIT708_4.1P
## Overview

This project is an Android Personal Event Planner App developed for SIT708 Task 4.1P. The app allows users to create, view, update, and manage personal events.

The application uses multiple fragments with Jetpack Navigation Component. Event data is stored locally using Room Database. The app also uses RecyclerView to display event items in a clear list format.

## Features

- View personal events
- Create new events
- Update existing events
- Store event data locally
- Display event title, category, location, and time
- RecyclerView event list
- Room Database local storage
- Fragment-based screens
- Jetpack Navigation Component
- Bottom Navigation

## Project Structure

```text
app/src/main/java/com.example.sit708_41p/
├── MainActivity.java
├── Event.java
├── EventDao.java
├── EventDatabase.java
├── ListFragment.java
├── CreateFragment.java
├── UpdateFragment.java
└── RecyclerViewAdapter.java

app/src/main/res/layout/
├── activity_main.xml
├── event_list.xml
├── fragment_list.xml
├── fragment_create.xml
└── fragment_update.xml

app/src/main/
└── AndroidManifest.xml
