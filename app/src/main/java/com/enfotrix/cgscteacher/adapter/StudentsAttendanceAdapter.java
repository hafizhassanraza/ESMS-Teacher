package com.enfotrix.cgscteacher.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.cgscteacher.AttendanceActivity;
import com.enfotrix.cgscteacher.R;
import com.enfotrix.cgscteacher.model.Student;

import org.w3c.dom.Text;

import java.util.List;

public class StudentsAttendanceAdapter extends RecyclerView.Adapter<StudentsAttendanceAdapter.ViewHolder> {
    private List<Student> students;
    private Context context;
    int countpresents = 0 , countabsents = 0 , countleaves = 0 ;

    public StudentsAttendanceAdapter(Context context, List<Student> students) {
        this.students = students;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_student, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Student student = students.get(position);
        holder.tvStudentName.setText(student.getFirstName() + " " + student.getLastName());
        holder.tvRegNo.setText(student.getRegNumber());
        holder.tvFatherName.setText(student.getFatherName());
        holder.tvSerialNo.setText(String.valueOf(position+1));

//        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                String status = "";
//                switch (i){
//                    case R.id.rd_present:
//                        status = "Present";
//                        break;
//                    case R.id.rd_leave:
//                        status = "Leave";
//                        break;
//                    case R.id.rd_absent:
//                        status = "Absent";
//                        break;
//
//                }
//               student.setStatus(status);
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentName, tvRegNo, tvSerialNo, tvFatherName, countpresent, countabsent, countleave;
        private RadioGroup radioGroup;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.rg_attendance);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvRegNo = itemView.findViewById(R.id.tv_reg_no);
            tvSerialNo = itemView.findViewById(R.id.tv_serial_no);
            tvFatherName = itemView.findViewById(R.id.tv_student_father_name);




            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    String status = "";
                    switch (i){
                        case R.id.rd_present:
                            status = "Present";
                            countpresents++;

                            break;
                        case R.id.rd_leave:
                            status = "Leave";
                            countleaves++;
                            break;
                        case R.id.rd_absent:
                            status = "Absent";
                            countabsents++;
                            break;

                    }
                    students.get(getAdapterPosition()).setStatus(status);

                }
            });

        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
