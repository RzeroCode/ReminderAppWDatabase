package com.example.ReminderAppWDatabase;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class RemindersActivity extends AppCompatActivity implements ReminderAdapter.EventHandler {

    public static final String INDEX_KEY = "com.example.cs310_hw4.index";


    //private static ArrayList<Reminder> reminderlist;

    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private Button popButton;
    private Button deleteButton;

    private ReminderViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        radioGroup = findViewById(R.id.radioGroup);
        deleteButton = findViewById(R.id.buttonDel);
        popButton = findViewById(R.id.buttonPop);

        viewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        final ReminderAdapter reminderAdapter = new ReminderAdapter(this, new ArrayList<Reminder>());
        recyclerView.setAdapter(reminderAdapter);

        popButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.PopulateDB();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteDB();
            }
        });

        viewModel.getLiveReminderList().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                if (reminders != null) {
                    reminderAdapter.setReminders(reminders);
                    reminderAdapter.notifyDataSetChanged();

                }

            }
        });




        // Sorting selection handling
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.dateButton) {
                    viewModel.sortByDate();
                } else if (checkedId == R.id.priorityButton) {
                    viewModel.sortByPriority();
                }
            }
        });
    }


    // Rest of the methods are to handle ReminderAdapter events

    @Override
    public void onCancelButtonClick(int index) {
        viewModel.remove(index);
    }

    @Override
    public void onPriorityBarChange(int index, int newValue) {
        viewModel.setPriority(newValue, index);
    }

    @Override
    public void onSwitchChange(int index) {
        viewModel.switchEnabled(index);
    }

    public void onReminderClick(int index) {
        Reminder reminder = viewModel.getReminderList().get(index);

        Intent intent = new Intent(RemindersActivity.this, DetailsActivity.class);
        intent.putExtra(INDEX_KEY, reminder.getRid());
        startActivity(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);



    }

}
