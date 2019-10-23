package com.example.ReminderAppWDatabase;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder>
{
    private List<Reminder> reminderList;
    private EventHandler eventHandler;

    public ReminderAdapter(EventHandler eventHandler, List<Reminder> reminderList) {
        this.reminderList = reminderList;
        this.eventHandler = eventHandler;
    }
    public void setReminders(List<Reminder> reminderList){
        this.reminderList=reminderList;

    }


    @NonNull
    @Override
    public ReminderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reminder_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i)
    {
        final Reminder reminder = reminderList.get(i);

        myViewHolder.nameView.setText(reminder.getName());
        myViewHolder.noteView.setText(reminder.getNote());
        myViewHolder.priorityBar.setMax(4);
        myViewHolder.priorityBar.setProgress(reminder.getPriority());
        myViewHolder.activeSwitch.setChecked(reminder.isEnabled());

        Calendar calendar = reminder.getCalendar();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        String dateStr = String.valueOf(day) + " " + new DateFormatSymbols().getMonths()[month];


        myViewHolder.dateView.setText(dateStr);

        myViewHolder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventHandler.onReminderClick(myViewHolder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View rowView;
        private TextView nameView;
        private TextView noteView;
        private TextView dateView;
        private ImageButton cancelButton;
        private SeekBar priorityBar;
        private Switch activeSwitch;
        private ImageView avatarView;

        MyViewHolder(View rowView) {
            super(rowView);
            this.rowView = rowView;
            nameView = rowView.findViewById(R.id.nameView);
            noteView = rowView.findViewById(R.id.noteView);
            dateView = rowView.findViewById(R.id.dateView);
            cancelButton = rowView.findViewById(R.id.cancelButton);
            priorityBar = rowView.findViewById(R.id.priorityBar);
            activeSwitch = rowView.findViewById(R.id.activeSwitch);
            avatarView = rowView.findViewById(R.id.avatarView);

            avatarView.setImageResource(R.drawable.ic_account_circle_black_24dp);

            activeSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventHandler.onSwitchChange(getAdapterPosition());
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventHandler.onCancelButtonClick(getAdapterPosition());
                }
            });

            priorityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if(fromUser)
                        eventHandler.onPriorityBarChange(getAdapterPosition(),seekBar.getProgress());

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {

                }
            });



        }
    }

    interface EventHandler {
        void onCancelButtonClick(int index);
        void onPriorityBarChange(int index, int value);
        void onSwitchChange(int index);
        void onReminderClick(int index);
    }


}
