package com.example.ReminderAppWDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Reminder.class}, version = 3) //
@TypeConverters({Converters.class})
public abstract class ReminderDatabase extends RoomDatabase {
    public abstract ReminderDao getReminderDao();

    private static ReminderDatabase database;

    public static ReminderDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    ReminderDatabase.class, "reminder_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}
