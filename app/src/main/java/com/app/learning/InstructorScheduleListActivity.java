package com.app.learning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.learning.data.DatabaseClient;
import com.app.learning.data.dao.InstructorAvailabilityDao;
import com.app.learning.data.entities.InstructorAvailability;

import java.util.List;

public class InstructorScheduleListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScheduleListAdapter adapter;
    private InstructorAvailabilityDao dao;
    private List<InstructorAvailability> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_schedule_list);

        // Initialize DAO and RecyclerView
        dao = DatabaseClient.getInstance(this).getAppDatabase().instructorAvailabilityDao();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up adapter and populate RecyclerView with schedule data
        adapter = new ScheduleListAdapter();
        recyclerView.setAdapter(adapter);
        scheduleList = dao.getAllByInstructorId(getIntent().getIntExtra(Constants.USER_ID, -1));
        adapter.setScheduleList(scheduleList);

        // Set up swipe-to-dismiss gesture for deleting schedules
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                InstructorAvailability schedule = scheduleList.get(position);
                dao.delete(schedule);
                scheduleList.remove(schedule);
                adapter.notifyItemRemoved(position);
                Toast.makeText(InstructorScheduleListActivity.this, "Schedule deleted", Toast.LENGTH_SHORT).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Adapter for displaying the list of schedules in the RecyclerView
    private class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleViewHolder> {
        private List<InstructorAvailability> scheduleList;

        public void setScheduleList(List<InstructorAvailability> scheduleList) {
            this.scheduleList = scheduleList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_layout, parent, false);
            return new ScheduleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
            InstructorAvailability schedule = scheduleList.get(position);
            holder.topicTextView.setText(schedule.getTopic());
            holder.dateTimeTextView.setText(schedule.getDateTimeString());
        }

        @Override
        public int getItemCount() {
            return scheduleList.size();
        }
    }

    // ViewHolder for each item in the RecyclerView
    private static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        public TextView topicTextView;
        public TextView dateTimeTextView;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            topicTextView = itemView.findViewById(R.id.topic_text_view);
            dateTimeTextView = itemView.findViewById(R.id.date_time_text_view);
        }
    }
}
