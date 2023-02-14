package com.enfotrix.cgscteacher.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.cgscteacher.R;
import com.enfotrix.cgscteacher.model.Student;

import java.util.List;

public class StudentsMarksAdapter extends RecyclerView.Adapter<StudentsMarksAdapter.ViewHolder> {
    private List<Student> students;
    private Context context;

    public StudentsMarksAdapter(Context context, List<Student> students) {
        this.students = students;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_student_marks, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Student student = students.get(position);
        holder.tvStudentName.setText(student.getFirstName() + " " + student.getLastName());
        holder.tvRegNo.setText(student.getRegNumber());

        if (holder.etMarks.getTag() instanceof TextWatcher) {
            holder.etMarks.removeTextChangedListener((TextWatcher) holder.etMarks.getTag());
        }


        TextWatcher primaryWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {

                    students.get(holder.getAdapterPosition()).setMarks(editable.toString());

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        holder.etMarks.addTextChangedListener(primaryWatcher);
        holder.etMarks.setTag(primaryWatcher);


    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentName, tvRegNo;
        private EditText etMarks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvRegNo = itemView.findViewById(R.id.tv_reg_no);
            etMarks = itemView.findViewById(R.id.et_marks);
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
