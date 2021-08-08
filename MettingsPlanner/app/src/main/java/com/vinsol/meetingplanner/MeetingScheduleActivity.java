package com.vinsol.meetingplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vinsol.meetingplanner.data.FetchData;
import com.vinsol.meetingplanner.databinding.ActivityMeetingScheduleBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MeetingScheduleActivity extends AppCompatActivity {

    private String currentDate;
    private Calendar myCalendar = Calendar.getInstance();
    private ActivityMeetingScheduleBinding scheduleBinding;
    private DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    private DatePickerDialog.OnDateSetListener date = (datePicker, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = dayOfMonth + MeetingsHomeActivity.STRING_DASH +
                (monthOfYear+1) + MeetingsHomeActivity.STRING_DASH + year;
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date date = format.parse(selectedDate);
            selectedDate = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        scheduleBinding.buttonMeetingDate.setText(selectedDate);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduleBinding = ActivityMeetingScheduleBinding.inflate(getLayoutInflater());
        setContentView(scheduleBinding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getString("CURRENT_DATE_ID") != null) {
            currentDate = extras.getString("CURRENT_DATE_ID");
        } else {
            Date date = new Date(Calendar.getInstance(Locale.ENGLISH).getTimeInMillis());
            currentDate = format.format(date);
        }

        scheduleBinding.buttonMeetingDate.setText(currentDate);
        scheduleBinding.buttonPrev.setOnClickListener(v -> MeetingScheduleActivity.this.finish());

        scheduleBinding.buttonMeetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] dateInfo = currentDate.split(MeetingsHomeActivity.STRING_DASH);
                int day = Integer.parseInt(dateInfo[0]);
                int month = Integer.parseInt(dateInfo[1]);
                int year = Integer.parseInt(dateInfo[2]);

                DatePickerDialog datePickerDialog=  new DatePickerDialog(MeetingScheduleActivity.this,
                        R.style.DialogTheme, date, year, month-1, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        scheduleBinding.buttonMeetingStartTime.setOnClickListener(v -> {
            showTimePicker(scheduleBinding.buttonMeetingStartTime);
        });
        scheduleBinding.buttonMeetingEndTime.setOnClickListener(v -> {
            showTimePicker(scheduleBinding.buttonMeetingEndTime);
        });
        
        scheduleBinding.scheduleSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = scheduleBinding.buttonMeetingDate.getText().toString();
                String startTime = scheduleBinding.buttonMeetingStartTime.getText().toString();
                String endTime = scheduleBinding.buttonMeetingEndTime.getText().toString();

                if(startTime.isEmpty() || endTime.isEmpty()){
                   return;
                }

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                try {
                    Date startTimeDate = timeFormat.parse(startTime);
                    Date endTimeDate = timeFormat.parse(endTime);

                    if(startTimeDate.after(endTimeDate)){
                        Toast.makeText(MeetingScheduleActivity.this,
                                "Start time can't be before end time.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                FetchData fetchData = new FetchData();
                fetchData.get(MeetingScheduleActivity.this, date, null,
                        startTime, endTime);
            }
        });
    }

    private void showTimePicker(Button button) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MeetingScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String time = selectedHour + ":" + selectedMinute;
                try {
                    Date selectedTime =  timeFormat.parse(time);
                    time = new SimpleDateFormat("hh:mm aa").format(selectedTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                button.setText(time);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}