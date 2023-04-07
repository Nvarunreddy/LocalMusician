package com.app.learning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.learning.adapter.BookingDataAdapter;
import com.app.learning.data.AppDatabase;
import com.app.learning.data.DatabaseClient;
import com.app.learning.data.dao.BookingDataDao;
import com.app.learning.data.entities.BookingDataWithInstructorAvailabilityAndUserData;

import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingDataAdapter adapter;
    private List<BookingDataWithInstructorAvailabilityAndUserData> bookingDataList;

    private BookingDataDao bookingDataDao;
    private int userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        userId = getIntent().getIntExtra(Constants.USER_ID, -1);
        role = getIntent().getStringExtra(Constants.ROLE);

        bookingDataDao = DatabaseClient.getInstance(this).getAppDatabase().bookingDataDao();

        recyclerView = findViewById(R.id.rv_my_bookings_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Swipe to delete functionality
        ItemTouchHelper.SimpleCallback swipeToDeleteCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                BookingDataWithInstructorAvailabilityAndUserData bookingData = bookingDataList.get(position);

                // Delete booking from database
                bookingDataDao.deleteBookingData(bookingData.getBookingData());

                // Remove booking from list
                bookingDataList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        bookingDataList = (role != null && role.equals(Constants.INSTRUCTOR)) ? bookingDataDao.getAllBookingDataWithInstructorAvailabilityAndUserDataByInstructorId(userId) : bookingDataDao.getAllBookingDataWithInstructorAvailabilityAndInstructorData(userId);
        adapter = new BookingDataAdapter(this, bookingDataList);
        recyclerView.setAdapter(adapter);
    }
}
