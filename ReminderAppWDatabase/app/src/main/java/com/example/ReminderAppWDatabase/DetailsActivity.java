package com.example.ReminderAppWDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class DetailsActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button saveButton;
    private EditText titleEdit, descriptionEdit;
    private SeekBar priorityBar;

    private int index;

    private DetailsViewModel viewModel;
    private boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        index = getIntent().getIntExtra(RemindersActivity.INDEX_KEY, 0);

        if (savedInstanceState != null) {
            viewModel.readFromBundle(savedInstanceState);
        } else {
            viewModel.loadReminder(index);
        }

        calendarView = findViewById(R.id.calendarView);
        saveButton = findViewById(R.id.saveButton);
        titleEdit = findViewById(R.id.titleEdit);
        descriptionEdit = findViewById(R.id.descriptionEdit);
        priorityBar = findViewById(R.id.detailPriorityBar);


        first=true;
        viewModel.getLiveReminder().observe(this, new Observer<Reminder>() {
            @Override
            public void onChanged(Reminder reminder) {
                if (first && reminder!=null) {
                    titleEdit.setText(viewModel.getReminder().getName());
                    descriptionEdit.setText(viewModel.getReminder().getNote());
                    calendarView.setDate(viewModel.getReminder().getTime(), false, true);
                    priorityBar.setProgress(viewModel.getReminder().getPriority());
                    first=false;
                }
            }
        });



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent(DetailsActivity.this, RemindersActivity.class);


                resultIntent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
                finish();
                startActivity(resultIntent);


            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                viewModel.setTime(calendar.getTimeInMillis());
            }
        });

        priorityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    viewModel.setPriority(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                viewModel.setName(s.toString());
            }
        });


        descriptionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                viewModel.setNote(s.toString());
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.writeToBundle(outState);
    }

}
