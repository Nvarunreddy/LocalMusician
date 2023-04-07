package com.app.learning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.learning.R;
import com.app.learning.data.entities.BookingDataWithInstructorAvailabilityAndUserData;

import java.util.List;

public class BookingDataAdapter extends RecyclerView.Adapter<BookingDataAdapter.ViewHolder> {

    private List<BookingDataWithInstructorAvailabilityAndUserData> bookingDataList;
    private Context context;

    public BookingDataAdapter(Context context, List<BookingDataWithInstructorAvailabilityAndUserData> bookingDataList) {
        this.context = context;
        this.bookingDataList = bookingDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_data_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingDataWithInstructorAvailabilityAndUserData bookingData = bookingDataList.get(position);

        String topicName = bookingData.getInstructorAvailability().getTopic();
        String instructorName = bookingData.getUserData().getEmail();
        String scheduleDateInfo = bookingData.getInstructorAvailability().getDateTimeString();

        holder.tvTopicName.setText(topicName);
        holder.tvInstructorName.setText(instructorName);
        holder.tvScheduleDateInfo.setText(scheduleDateInfo);
    }

    @Override
    public int getItemCount() {
        return bookingDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTopicName;
        private TextView tvInstructorName;
        private TextView tvScheduleDateInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTopicName = itemView.findViewById(R.id.topic_name_tv);
            tvInstructorName = itemView.findViewById(R.id.instructor_name_tv);
            tvScheduleDateInfo = itemView.findViewById(R.id.schedule_date_tv);

        }
    }
}
