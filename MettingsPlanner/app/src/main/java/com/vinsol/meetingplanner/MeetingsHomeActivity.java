package com.vinsol.meetingplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vinsol.meetingplanner.adapter.MeetingsRecyclerAdapter;
import com.vinsol.meetingplanner.data.FetchData;
import com.vinsol.meetingplanner.data.MeetingInfo;
import com.vinsol.meetingplanner.databinding.ActivityMeetingsHomeBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MeetingsHomeActivity extends AppCompatActivity {

    private ActivityMeetingsHomeBinding homeBinding;
    private String currentDate;

    private Calendar myCalendar = Calendar.getInstance();
    public static final String STRING_DASH = "-";
    private DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    private DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDate = dayOfMonth + STRING_DASH + (monthOfYear+1) + STRING_DASH + year;
        try {
            Date date = format.parse(currentDate);
            currentDate = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getNewDetails(0);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityMeetingsHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getString("DATE_DETAILS_ID") != null) {
            currentDate = extras.getString("DATE_DETAILS_ID");
        } else {
            Date date = new Date(Calendar.getInstance(Locale.ENGLISH).getTimeInMillis());

            currentDate = format.format(date);
        }

        homeBinding.buttonScheduleMeeting.setOnClickListener(v -> {
            boolean verifyDate = true;
            try {
                Date selectedDate = format.parse(currentDate);
                String currentDateText = format.format(new Date());
                verifyDate = (currentDateText.equals(currentDate) || selectedDate.after(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(verifyDate){
                Intent scheduleMeeting = new Intent(MeetingsHomeActivity.this, MeetingScheduleActivity.class);
                scheduleMeeting.putExtra("CURRENT_DATE_ID", currentDate);
                startActivity(scheduleMeeting);
            } else {
                Toast.makeText(this, "Can't schedule meeting for previous date.", Toast.LENGTH_SHORT).show();
            }
        });

        homeBinding.buttonDate.setText(currentDate);
        homeBinding.buttonDate.setOnClickListener(v -> {
            String[] dateInfo = currentDate.split(STRING_DASH);
            int day = Integer.parseInt(dateInfo[0]);
            int month = Integer.parseInt(dateInfo[1]);
            int year = Integer.parseInt(dateInfo[2]);

            DatePickerDialog datePickerDialog=  new DatePickerDialog(MeetingsHomeActivity.this,
                    R.style.DialogTheme, date, year, month-1, day);
            datePickerDialog.show();
        });

        homeBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MeetingsRecyclerAdapter meetingsRecyclerAdapter = new MeetingsRecyclerAdapter(
                new ArrayList<MeetingInfo>(), this);
        homeBinding.recyclerView.setAdapter(meetingsRecyclerAdapter);

        homeBinding.buttonPrev.setOnClickListener(v -> {
            getNewDetails(-1);
        });
        homeBinding.buttonNext.setOnClickListener(v -> {
            getNewDetails(1);
        });

        FetchData fetchData = new FetchData();
        fetchData.get(MeetingsHomeActivity.this, currentDate, meetingsRecyclerAdapter, null, null);
    }

    private void getNewDetails(int days) {
        try {
            Intent intent = new Intent(MeetingsHomeActivity.this, MeetingsHomeActivity.class);
            intent.putExtra("DATE_DETAILS_ID", getDate(days));
            startActivity(intent);
            MeetingsHomeActivity.this.finish();
        } catch (ParseException e) {
            Toast.makeText(this, "Unable to get details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getDate(int days) throws ParseException {
        if(days == 0)
            return currentDate;

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = format.parse(currentDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);

        return format.format(calendar.getTime());
    }
}