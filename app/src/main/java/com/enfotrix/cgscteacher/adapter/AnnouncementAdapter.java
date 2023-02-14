package com.enfotrix.cgscteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.cgscteacher.R;
import com.enfotrix.cgscteacher.model.AnnouncementModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private List<AnnouncementModel> announcementModels;
    private Context context;
    final static String approveAnn = "Approved";
    FirebaseFirestore firebaseFirestor = FirebaseFirestore.getInstance();

    public AnnouncementAdapter(List<AnnouncementModel> announcementModels, Context context) {
        this.context = context;
        this.announcementModels = announcementModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AnnouncementModel announcementModel = announcementModels.get(position);

        holder.announcementTitle.setText(announcementModel.getAnnouncement());
        holder.announcementDate.setText(announcementModel.getDate());

    }

    @Override
    public int getItemCount() {
        return announcementModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView announcementTitle, announcementDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            announcementTitle = itemView.findViewById(R.id.tvAnnouncement);
            announcementDate = itemView.findViewById(R.id.tvDate);


        }
    }

}
