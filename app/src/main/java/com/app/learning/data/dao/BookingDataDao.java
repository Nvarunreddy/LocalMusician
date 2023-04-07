package com.app.learning.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.app.learning.data.entities.BookingData;
import com.app.learning.data.entities.BookingDataWithInstructorAvailabilityAndUserData;

import java.util.List;

@Dao
public interface BookingDataDao {

    @Insert
    void insertBookingData(BookingData bookingData);

    @Delete
    void deleteBookingData(BookingData bookingData);

    @Query("SELECT * FROM booking_data WHERE user_id = :userId")
    List<BookingData> getAllBookingDataByUserId(int userId);

    @Query("SELECT * FROM booking_data WHERE schedule_id IN (SELECT id FROM InstructorAvailability WHERE instructorId = :userId)")
    List<BookingData> getBookingDataByUserIdAndScheduleIdInstructorAvailability(int userId);

    @Query("SELECT booking_data.*, InstructorAvailability.*, UserData.* " +
            "FROM booking_data " +
            "INNER JOIN InstructorAvailability ON booking_data.schedule_id = InstructorAvailability.instructor_availability_id " +
            "INNER JOIN UserData ON booking_data.user_id = UserData.user_data_id " +
            "INNER JOIN UserData AS UserData2 ON InstructorAvailability.instructorId = UserData2.user_data_id " +
            "WHERE UserData2.user_data_id = :userId")
    List<BookingDataWithInstructorAvailabilityAndUserData> getAllBookingDataWithInstructorAvailabilityAndUserDataByInstructorId(int userId);

    @Query("SELECT booking_data.*, InstructorAvailability.*, UserData.* " +
            "FROM booking_data " +
            "INNER JOIN InstructorAvailability ON booking_data.schedule_id = InstructorAvailability.instructor_availability_id " +
            "INNER JOIN UserData as UserData ON InstructorAvailability.instructorId = UserData.user_data_id " +
            "WHERE booking_data.user_id = :userId")
    List<BookingDataWithInstructorAvailabilityAndUserData> getAllBookingDataWithInstructorAvailabilityAndInstructorData(int userId);


}
