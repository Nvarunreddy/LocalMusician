
package com.app.learning.work;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.learning.Constants;
import com.app.learning.R;
import com.app.learning.data.AppDatabase;
import com.app.learning.data.DatabaseClient;
import com.app.learning.data.entities.InstructorAvailability;
import com.app.learning.data.entities.UserData;

import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;


public class BookingReminderWorker extends Worker {

    private static final String CHANNEL_ID = "BookingReminderChannel";
    private static final String CHANNEL_NAME = "Booking Reminder Channel";

    public BookingReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get the schedule ID from the input data
        int scheduleId = getInputData().getInt(Constants.SCHEDULE_ID, -1);

        // If the schedule ID is not valid, return failure
        if (scheduleId == -1) {
            return Result.failure();
        }

        // Get the InstructorAvailability data for the given schedule ID
        AppDatabase database = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        InstructorAvailability instructorAvailability = database.instructorAvailabilityDao().getInstructorAvailabilityById(scheduleId);

        // If there is no data for the given schedule ID, return failure
        if (instructorAvailability == null) {
            return Result.failure();
        }

        // Get the user data for the instructor
        UserData instructorUserData = database.userDao().getUserDataById(instructorAvailability.getInstructorId());

        // If there is no user data for the instructor, return failure
        if (instructorUserData == null) {
            return Result.failure();
        }

        // Create a notification channel
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Booking Reminder")
                .setContentText(String.format("You have a booking with %s for %s at %s", instructorUserData.getEmail(), instructorAvailability.getTopic(), instructorAvailability.getDateTimeString()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        notificationManager.notify(scheduleId, builder.build());

        return Result.success();
    }
}

