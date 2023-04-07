package com.app.learning.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class InstructorAvailabilityWithUserData {

    @Embedded
    private InstructorAvailability instructorAvailability;

    @Embedded
    private UserData userData;


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
