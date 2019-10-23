package com.example.ReminderAppWDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

@Entity(tableName = "reminders")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private int rid;

    private String name;
    private String note;
    private Calendar calendar;
    private int priority;
    private boolean enabled;



    public Reminder(String name, String note, Calendar calendar, int priority, boolean enabled) {


        this.name = name;
        this.note = note;
        this.priority = priority;
        this.enabled=enabled;

        this.calendar=calendar;

    }
    public Reminder(String name, String note, String date, int priority, int enabled) {


        this.name = name;
        this.note = note;
        this.priority = priority;

        if (enabled == 0)
            this.enabled = false;
        else
            this.enabled = true;

        String[] dateSplit = date.split("/");

        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);

        calendar = new GregorianCalendar(year, month - 1, day);

    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getRid() {
        return rid;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }


    public int getPriority() {
        return priority;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Long getTime() {
        return calendar.getTimeInMillis();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setTime(Long time) {
        calendar.setTimeInMillis(time);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public static class SortByDate implements Comparator<Reminder> {

        @Override
        public int compare(Reminder o1, Reminder o2) {

            return o1.calendar.compareTo(o2.calendar);
        }
    }


    public static class SortByPriority implements Comparator<Reminder> {

        @Override
        public int compare(Reminder o1, Reminder o2) {
            Integer p1 = o1.getPriority();
            Integer p2 = o2.getPriority();

            // We reverse the comparison because we want higher priority to come first
            return p2.compareTo(p1);
        }
    }


}

class Converters {
    @TypeConverter
    public  static Long dateToLong(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    @TypeConverter
    public static Calendar longToDate(Long time) {
        Calendar temp = new GregorianCalendar();
        temp.setTimeInMillis(time);
        return temp;
    }
}
