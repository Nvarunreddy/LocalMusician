package com.app.learning.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "booking_data")
public class BookingData {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "schedule_id")
    private int scheduleId;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "booking_date")
    private long bookingDate;

    public BookingData(int scheduleId, int userId, long bookingDate) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.bookingDate = bookingDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(long bookingDate) {
        this.bookingDate = bookingDate;
    }
}
