package com.app.learning.data.entities;

import androidx.room.Embedded;

public class BookingDataWithInstructorAvailabilityAndUserData {
    @Embedded
    private BookingData bookingData;

    @Embedded
    private InstructorAvailability instructorAvailability;

    @Embedded
    private UserData userData;

    public BookingData getBookingData() {
        return bookingData;
    }

    public void setBookingData(BookingData bookingData) {
        this.bookingData = bookingData;
    }

    public InstructorAvailability getInstructorAvailability() {
        return instructorAvailability;
    }

    public void setInstructorAvailability(InstructorAvailability instructorAvailability) {
        this.instructorAvailability = instructorAvailability;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
