package com.app.learning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.app.learning.adapter.InstructorAvailabilityWithUserDataAdapter;
import com.app.learning.data.AppDatabase;
import com.app.learning.data.DatabaseClient;
import com.app.learning.data.dao.BookingDataDao;
import com.app.learning.data.dao.InstructorAvailabilityDao;
import com.app.learning.data.entities.BookingData;
import com.app.learning.data.entities.InstructorAvailabilityWithUserData;
import com.app.learning.work.BookingReminderWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookScheduleActivity extends AppCompatActivity implements InstructorAvailabilityWithUserDataAdapter.OnScheduleClickListener {

    private RecyclerView recyclerView;
    private Button gotoMyBookingsBtn;
    private InstructorAvailabilityWithUserDataAdapter adapter;
    private InstructorAvailabilityDao instructorAvailabilityDao;
    private BookingDataDao bookingDataDao;
    private List<InstructorAvailabilityWithUserData> scheduleList;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_schedule);
        userId = getIntent().getIntExtra(Constants.USER_ID, -1);

        // Initialize the database and DAOs
        AppDatabase db = DatabaseClient.getInstance(this).getAppDatabase();
        instructorAvailabilityDao = db.instructorAvailabilityDao();
        bookingDataDao = db.bookingDataDao();

        // Initialize the RecyclerView and adapter
        recyclerView = findViewById(R.id.rv_schedule_list);
        gotoMyBookingsBtn = findViewById(R.id.my_bookings_btn);

        gotoMyBookingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyBookingsActivity.class);
            intent.putExtra(Constants.USER_ID, userId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        scheduleList = instructorAvailabilityDao.getAvailableInstructorAvailabilitiesWithUserData();

        adapter = new InstructorAvailabilityWithUserDataAdapter(scheduleList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBookClick(int position) {
        // Get the selected schedule
        InstructorAvailabilityWithUserData schedule = scheduleList.get(position);

        // Create a new booking with the selected schedule's instructorId, userId, and dateTime
        BookingData bookingData = new BookingData(schedule.getInstructorAvailability().getId(),
                userId,
                System.currentTimeMillis());

        // Insert the booking into the database
        bookingDataDao.insertBookingData(bookingData);

        // Show a toast message to confirm the booking
        Toast.makeText(this, "Booking successful", Toast.LENGTH_SHORT).show();
        scheduleList.remove(position);
        adapter.notifyDataSetChanged();

        scheduleWorkerForDayBeforeScheduleDay(schedule);
        scheduleWorkerForHourBeforeScheduleTime(schedule);
    }

    // Schedule a reminder notification for 1 day before the booking
    private void scheduleWorkerForDayBeforeScheduleDay(InstructorAvailabilityWithUserData schedule) {
        long delayInMillis = schedule.getInstructorAvailability().getDateTime() - System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        if (delayInMillis > 0) {
            Data inputData = new Data.Builder()
                    .putInt(Constants.SCHEDULE_ID, schedule.getInstructorAvailability().getId())
                    .build();
            OneTimeWorkRequest reminderWorkRequest = new OneTimeWorkRequest.Builder(BookingReminderWorker.class)
                    .setInputData(inputData)
                    .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                    .build();
            WorkManager.getInstance(this).enqueue(reminderWorkRequest);
        }
    }

    // Schedule a reminder notification for 1 hour before the booking
    private void scheduleWorkerForHourBeforeScheduleTime(InstructorAvailabilityWithUserData schedule) {
        long delayInMillis = schedule.getInstructorAvailability().getDateTime() - System.currentTimeMillis() - 60 * 60 * 1000;
        if (delayInMillis > 0) {
            Data inputData = new Data.Builder()
                    .putInt(Constants.SCHEDULE_ID, schedule.getInstructorAvailability().getId())
                    .build();
            OneTimeWorkRequest reminderWorkRequest = new OneTimeWorkRequest.Builder(BookingReminderWorker.class)
                    .setInputData(inputData)
                    .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                    .build();
            WorkManager.getInstance(this).enqueue(reminderWorkRequest);
        }
    }
}
