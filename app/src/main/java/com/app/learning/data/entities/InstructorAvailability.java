package com.app.learning.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class InstructorAvailability {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "instructor_availability_id")
    private int id;
    private int instructorId;
    private String topic;
    private long dateTime;

    private double latitude;
    private double longitude;

    public InstructorAvailability(int instructorId, String topic, long dateTime, double latitude, double longitude) {
        this.instructorId = instructorId;
        this.topic = topic;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTimeString() {
        // Create a SimpleDateFormat object with the desired date and time format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());

        // Convert the milliseconds to a Date object
        Date date = new Date(dateTime);

        // Format the Date object using the SimpleDateFormat object
        return sdf.format(date);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}