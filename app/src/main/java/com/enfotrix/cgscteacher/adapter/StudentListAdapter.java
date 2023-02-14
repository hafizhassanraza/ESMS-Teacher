package com.enfotrix.cgscteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.cgscteacher.MainActivity;
import com.enfotrix.cgscteacher.R;
import com.enfotrix.cgscteacher.Students;
import com.enfotrix.cgscteacher.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private List<Student> students;
    private Context context;

    public StudentListAdapter(Context context, List<Student> students) {
        this.students = students;
        this.context = context;
    }


    @NonNull
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.students_list, null);
        StudentListAdapter.ViewHolder viewHolder = new StudentListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.ViewHolder holder, int position) {

        final Student student = students.get(position);
        holder.studentName.setText(student.getFirstName() + " " + student.getLastName());
        holder.fatherName.setText(student.getFatherName());
        holder.feeStatus.setText("Pending");
        holder.regNo.setText(student.getRegNumber());
        holder.serialNo.setText(String.valueOf(position+1));

        holder.studentCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, student.getFatherWhatsappNumber(), Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+student.getFatherPhoneNumber()));//change the number
               context.startActivity(callIntent);
            }
        });
        holder.studentWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = "+92"+student.getFatherWhatsappNumber(); // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = context.getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
 }


    @Override
    public int getItemCount() {
        return students.size() ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView studentName, fatherName, feeStatus, regNo, serialNo;
        private ImageView studentWhatsApp, studentCall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name) ;
            fatherName = itemView.findViewById(R.id.father_name);
            feeStatus = itemView.findViewById(R.id.fee_status);
            studentWhatsApp = itemView.findViewById(R.id.student_whatsapp);
            studentCall = itemView.findViewById(R.id.student_call);
            regNo = itemView.findViewById(R.id.reg_no);
            serialNo = itemView.findViewById(R.id.serial_no);


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
