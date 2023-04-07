package com.app.learning.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.learning.BookScheduleActivity;
import com.app.learning.Constants;
import com.app.learning.MapsLocationActivity;
import com.app.learning.R;
import com.app.learning.data.entities.InstructorAvailabilityWithUserData;

import java.util.List;

public class InstructorAvailabilityWithUserDataAdapter extends RecyclerView.Adapter<InstructorAvailabilityWithUserDataAdapter.ViewHolder> {
    private List<InstructorAvailabilityWithUserData> mSchedules;
    private OnScheduleClickListener mListener;

    public interface OnScheduleClickListener {
        void onBookClick(int position);
    }

    public InstructorAvailabilityWithUserDataAdapter(List<InstructorAvailabilityWithUserData> schedules, OnScheduleClickListener listener) {
        mSchedules = schedules;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.learner_schedule_item_layout, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InstructorAvailabilityWithUserData schedule = mSchedules.get(position);

        holder.mEmailTextView.setText(schedule.getUserData().getEmail());
        holder.mTopicTextView.setText(schedule.getInstructorAvailability().getTopic());
        holder.mDateTimeTextView.setText(schedule.getInstructorAvailability().getDateTimeString());

        holder.mBookButton.setOnClickListener(view -> mListener.onBookClick(position));

        holder.mViewLocationButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MapsLocationActivity.class);
            intent.putExtra(Constants.EXTRA_LATITUDE, schedule.getInstructorAvailability().getLatitude());
            intent.putExtra(Constants.EXTRA_LONGITUDE, schedule.getInstructorAvailability().getLongitude());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mEmailTextView;
        public final TextView mTopicTextView;
        public final TextView mDateTimeTextView;
        public final Button mBookButton;
        public final Button mViewLocationButton;

        public ViewHolder(View view, final OnScheduleClickListener listener) {
            super(view);
            mEmailTextView = view.findViewById(R.id.tv_email);
            mTopicTextView = view.findViewById(R.id.tv_topic);
            mDateTimeTextView = view.findViewById(R.id.tv_datetime);
            mBookButton = view.findViewById(R.id.btn_book);
            mViewLocationButton = view.findViewById(R.id.view_location);

            mBookButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onBookClick(position);
                    }
                }
            });


        }
    }
}
