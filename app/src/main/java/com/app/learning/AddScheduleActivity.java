package com.app.learning;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.learning.data.DatabaseClient;
import com.app.learning.data.dao.InstructorAvailabilityDao;
import com.app.learning.data.entities.InstructorAvailability;
import com.app.learning.databinding.ActivityAddScheduleBinding;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private InstructorAvailabilityDao dao;

    private ActivityAddScheduleBinding binding;

    private int year, month, day, hour, minute;
    private static final int REQUEST_SELECT_LOCATION = 1;
    private double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize DAO
        dao = DatabaseClient.getInstance(this).getAppDatabase().instructorAvailabilityDao();

        // Set click listeners for date and time picker buttons
        binding.datePickerButton.setOnClickListener(v -> showDatePickerDialog());

        binding.timePickerButton.setOnClickListener(v -> showTimePickerDialog());

        binding.addScheduleButton.setOnClickListener(v -> saveInstructorAvailability());

        binding.scheduleListButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, InstructorScheduleListActivity.class);
            intent.putExtra(Constants.USER_ID, getIntent().getIntExtra(Constants.USER_ID, -1));
            startActivity(intent);
        });

        binding.myBookingsBtn.setOnClickListener(v->{
            Intent intent = new Intent(this, MyBookingsActivity.class);
            intent.putExtra(Constants.USER_ID, getIntent().getIntExtra(Constants.USER_ID, -1));
            intent.putExtra(Constants.ROLE, Constants.INSTRUCTOR);
            startActivity(intent);
        });

        binding.selectLocation.setOnClickListener(v->{
            Intent intent = new Intent(AddScheduleActivity.this, InstructorMapsActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_LOCATION);
        });
    }

    // Show date picker dialog when date picker button is clicked
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                year,
                month,
                dayOfMonth);
        datePickerDialog.show();
    }

    // Show time picker dialog when time picker button is clicked
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                hour,
                minute,
                false);
        timePickerDialog.show();
    }

    // Set the selected date in the date input field
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        updateDateTimeInput();
    }

    // Set the selected time in the time input field
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        updateDateTimeInput();
    }

    // Update the date and time input field with the selected date and time
    private void updateDateTimeInput() {
        String dateTimeString = String.format("%d/%d/%d %02d:%02d", month + 1, day, year, hour, minute);
        binding.dateTimeInput.setText(dateTimeString);
    }

    // Save the instructor availability record to the database
    private void saveInstructorAvailability() {
        String topic = binding.topicInput.getText().toString();
        long dateTime = getSelectedDateTimeInMillis();
        int userId = getIntent().getIntExtra(Constants.USER_ID, -1);

        InstructorAvailability availability = new InstructorAvailability(userId, topic, dateTime, latitude, longitude);
        dao.insert(availability);

        binding.topicInput.setText("");
        binding.dateTimeInput.setText("");

        Toast.makeText(this, "Schedule saved successfully", Toast.LENGTH_SHORT).show();
    }

    private long getSelectedDateTimeInMillis() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_LOCATION && resultCode == RESULT_OK) {
            latitude = data.getDoubleExtra(Constants.EXTRA_LATITUDE, 0);
            longitude = data.getDoubleExtra(Constants.EXTRA_LONGITUDE, 0);

            binding.selectLocation.setText("Latitude: "+ latitude +"\nLongitude: "+ longitude);
        }
    }

}