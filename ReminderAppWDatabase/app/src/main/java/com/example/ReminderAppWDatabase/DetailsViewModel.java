package com.example.ReminderAppWDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.app.Application;
import android.os.Bundle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsViewModel extends AndroidViewModel {

    public final static String BUNDLE_KEY = "com.example.cs310_hw5.bundle.indexkey";


    private LiveData<Reminder> liveReminder;
    private ExecutorService executor;
    private ReminderDatabase database;
    private int reminderId;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        executor = Executors.newSingleThreadExecutor();
        database = ReminderDatabase.getInstance(application.getApplicationContext());
    }


    public void loadReminder(int rid) {
        reminderId = rid;
        liveReminder = database.getReminderDao().getReminder(reminderId);  //
    }

    public LiveData<Reminder> getLiveReminder() {
        return liveReminder;
    }

    public Reminder getReminder() {
        return liveReminder.getValue();
    }


    public void setTime(final Long time) {
        if (getReminder() == null)
            return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getReminder().setTime(time);
                database.getReminderDao().updateReminders(getReminder());
            }
        });
    }

    public void setName(final String name) {
        if (getReminder() == null)
            return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getReminder().setName(name);
                database.getReminderDao().updateReminders(getReminder());
            }
        });
    }

    public void setNote(final String note) {
        if (getReminder() == null)
            return;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getReminder().setNote(note);
                database.getReminderDao().updateReminders(getReminder());
            }
        });
    }

    public void setPriority(final int priority) {
        if (getReminder() == null)
            return;
        if (priority <= 4 && priority >= 0) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    getReminder().setPriority(priority);
                    database.getReminderDao().updateReminders(getReminder());
                }
            });
        }
    }


    public void writeToBundle(Bundle bundle) {


        bundle.putInt(BUNDLE_KEY, reminderId);
    }

    public void readFromBundle(Bundle bundle) {
        loadReminder(bundle.getInt(BUNDLE_KEY));
    }

}

