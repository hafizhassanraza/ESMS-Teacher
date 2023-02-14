package com.enfotrix.cgscteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.cgscteacher.R;
import com.enfotrix.cgscteacher.model.Timetable;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {
    private Context context;
    private List<Timetable> timetable;

    public TimetableAdapter(Context context, List<Timetable> timetable) {
        this.context = context;
        this.timetable = timetable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_timetable, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Timetable timeTable = timetable.get(position);
        holder.tvSubject.setText(timeTable.getSubject());
        holder.tvStarttime.setText(timeTable.getStarttime());
        holder.tvEndtime.setText(timeTable.getEndtime());

    }

    @Override
    public int getItemCount() {
        return timetable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStarttime, tvEndtime, tvSubject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStarttime = itemView.findViewById(R.id.txt_lecture_time);
            tvEndtime = itemView.findViewById(R.id.txt_lecture_time);
            tvSubject = itemView.findViewById(R.id.txt_lecture_subject);

        }
    }
}
