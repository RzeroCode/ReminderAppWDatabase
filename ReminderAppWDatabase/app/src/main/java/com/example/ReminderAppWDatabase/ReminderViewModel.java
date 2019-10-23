package com.example.ReminderAppWDatabase;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderViewModel extends AndroidViewModel {
    private ReminderDatabase database;
    private ExecutorService executor;
    private LiveData<List<Reminder>> liveReminderList;

    public ReminderViewModel(Application application) {
        super(application);

        database = ReminderDatabase.getInstance(getApplication().getApplicationContext());
        liveReminderList = database.getReminderDao().getAllReminders();
        executor = Executors.newSingleThreadExecutor();

        // sortByPriority();

    }

    public void PopulateDB() {
        if (getReminderList().size() != 0)
            return;

        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        final Reminder[] reminders =
                                {
                                        new Reminder("CS310 Midterm1", "Midterm at LO65 from 9am", "06/04/2019", 4, 1),
                                        new Reminder("Room meeting", "Meet Mr. Mbape before things get worse", "07/04/2019", 4, 1),
                                        new Reminder("CS310 Midterm1 Objection", "Room LO65 from 10am ", "06/05/2019", 3, 0),
                                        new Reminder("CS308 Project presentation", "4th sprint user-inteface design", "09/04/2019", 4, 1),
                                        new Reminder("Judiy's Funeral", "buy flowers", "31/03/2019", 3, 0),
                                        new Reminder("Avengers release", "Invite roommates to watch it", "02/04/2019", 2, 1),
                                        new Reminder("Job application", "Ware the black suit", "05/05/2019", 4, 0),
                                        new Reminder("Google's interview", "Focus on software engineering nothing else matters", "06/04/2019", 1, 1),
                                        new Reminder("Summer school starts", "Nothing special", "06/04/2019", 4, 1),
                                        new Reminder("MS application deadline", "now or never", "08/05/2019", 4, 1),
                                        new Reminder("Make plane reservation", "Most preferably Turkish Airlines", "29/03/2019", 3, 1),
                                        new Reminder("Trip to Africa", "Dont miss your plane", "06/04/2019", 4, 0),
                                        new Reminder("See the dentist", "Midterm at LO65 from 9am", "06/05/2019", 2, 1),
                                        new Reminder("CS310 Midterm2", "Midterm at LO65 from 9am", "04/04/2019", 4, 1),
                                        new Reminder("Call Dad", "Remind him of fees", "06/04/2019", 3, 1),
                                        new Reminder("Shopping", "With loved ones", "06/05/2019", 1, 0),
                                        new Reminder("fundraiser", "Give whatever you have", "01/04/2019", 4, 1),
                                        new Reminder("CS310 Final", "All topics includes", "28/05/2019", 1, 1),
                                        new Reminder("Date with Juliet", "Hilton hotel, Mecidiyekoy", "06/04/2019", 4, 0),

                                };
                        database.getReminderDao().insertReminders(reminders);
                    }
                });
            }
        });
    }

    public void deleteDB() {


        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.getReminderDao().deleteAll();
            }
        });
    }

    public LiveData<List<Reminder>> getLiveReminderList() {
        return liveReminderList;
    }

    public List<Reminder> getReminderList() {
        return liveReminderList.getValue();
    }

    private Reminder getReminder(int index) {
        return liveReminderList.getValue().get(index);
    }

    public void sortByDate() {

        if (liveReminderList.getValue() == null)
            return;
        executor.execute(new Runnable() {
            @Override
            public void run() {

                List<Reminder> list = getReminderList();
                Collections.sort(list, new Reminder.SortByDate());
                int i = 1; //
                for (Reminder reminder : list)  //
                {
                    reminder.setRid(i);
                    i++;
                }
                Reminder[] listArray = list.toArray(new Reminder[list.size()]);


                database.getReminderDao().deleteAll();
                // database.getReminderDao().updateReminders(listArray);
                database.getReminderDao().insertReminders(listArray);
            }
        });
    }


    public void sortByPriority() {
        if (liveReminderList.getValue() == null)
            return;
        executor.execute(new Runnable() {
            @Override
            public void run() {

                List<Reminder> list = getReminderList();
                Collections.sort(list, new Reminder.SortByPriority());
                int i = 1; //
                for (Reminder reminder : list)  //
                {
                    reminder.setRid(i);
                    i++;
                }
                Reminder[] listArray = list.toArray(new Reminder[list.size()]);


                database.getReminderDao().deleteAll();

                // database.getReminderDao().updateReminders(listArray);
                database.getReminderDao().insertReminders(listArray);
            }
        });


    }

    public void remove(int index) {
        if (index < 0)
            return;
        final int j = index;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Reminder> list = getReminderList();
                database.getReminderDao().deleteItem(list.get(j).getRid());
            }
        });

    }

    public void setPriority(int priority, int index) {
        if (index < 0)
            return;
        final int j = index;
        final int pri = priority;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Reminder> list = getReminderList();
                Reminder reminder = list.get(j);
                reminder.setPriority(pri);
                database.getReminderDao().updateReminders(reminder);
            }
        });


    }


    public void switchEnabled(int index) {
        if (index < 0)
            return;
        final int j = index;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Reminder> list = getReminderList();
                Reminder reminder = list.get(j);
                reminder.setEnabled(!reminder.isEnabled());
                database.getReminderDao().updateReminders(reminder);
            }
        });
    }


}
