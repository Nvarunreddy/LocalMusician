package com.app.learning.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.app.learning.data.entities.InstructorAvailability;
import com.app.learning.data.entities.InstructorAvailabilityWithUserData;

import java.util.List;

@Dao
public interface InstructorAvailabilityDao {
    @Insert
    void insert(InstructorAvailability availability);

    @Delete
    void delete(InstructorAvailability availability);

    @Query("DELETE FROM InstructorAvailability WHERE instructor_availability_id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM InstructorAvailability")
    List<InstructorAvailability> getAll();

    @Query("SELECT * FROM InstructorAvailability WHERE instructorId = :instructorId")
    List<InstructorAvailability> getAllByInstructorId(int instructorId);

    @Query("SELECT InstructorAvailability.*, UserData.* " +
            "FROM InstructorAvailability " +
            "INNER JOIN UserData ON InstructorAvailability.instructorId = UserData.user_data_id")
    List<InstructorAvailabilityWithUserData> getAllInstructorAvailabilitiesWithUserData();

    @Query("SELECT InstructorAvailability.*, UserData.* " +
            "FROM InstructorAvailability " +
            "INNER JOIN UserData ON InstructorAvailability.instructorId = UserData.user_data_id " +
            "WHERE InstructorAvailability.instructor_availability_id NOT IN (SELECT booking_data.schedule_id FROM booking_data)")
    List<InstructorAvailabilityWithUserData> getAvailableInstructorAvailabilitiesWithUserData();

    @Query("SELECT * from InstructorAvailability WHERE instructor_availability_id = :scheduleId")
    InstructorAvailability getInstructorAvailabilityById(int scheduleId);
}
