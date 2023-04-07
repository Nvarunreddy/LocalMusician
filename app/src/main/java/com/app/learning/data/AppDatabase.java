package com.app.learning.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.app.learning.data.dao.BookingDataDao;
import com.app.learning.data.dao.InstructorAvailabilityDao;
import com.app.learning.data.dao.UserDao;
import com.app.learning.data.entities.BookingData;
import com.app.learning.data.entities.InstructorAvailability;
import com.app.learning.data.entities.UserData;

@Database(entities = {UserData.class, InstructorAvailability.class, BookingData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract InstructorAvailabilityDao instructorAvailabilityDao();

    public abstract BookingDataDao bookingDataDao();
}