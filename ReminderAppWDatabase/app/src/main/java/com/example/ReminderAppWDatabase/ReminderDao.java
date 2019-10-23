package com.example.ReminderAppWDatabase;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {

    @Query("SELECT * FROM reminders")
    LiveData<List<Reminder>> getAllReminders();
    @Query("Select * FROM reminders WHERE rid= :rid")
    LiveData<Reminder> getReminder(long rid);
    @Query("DELETE FROM reminders")
    void deleteAll();
    @Query("DELETE FROM reminders WHERE rid= :rid")
    void deleteItem(long rid);


    @Update
    void updateReminders(Reminder... reminders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminders(Reminder... reminders);



}
