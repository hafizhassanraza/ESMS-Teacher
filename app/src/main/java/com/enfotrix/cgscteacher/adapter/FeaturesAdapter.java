package com.enfotrix.cgscteacher.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.enfotrix.cgscteacher.AttendanceActivity;
import com.enfotrix.cgscteacher.MarksActivity;
import com.enfotrix.cgscteacher.R;
import com.enfotrix.cgscteacher.SelectActivity;
import com.enfotrix.cgscteacher.Students;
import com.enfotrix.cgscteacher.model.Exam;
import com.enfotrix.cgscteacher.model.Feature;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.DashboardViewHolder> {
    private Context mContext;
    private List<Feature> categories;


    public FeaturesAdapter(Context mContext, List<Feature> categories) {
        this.mContext = mContext;
        this.categories = categories;


    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_feature, null));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        Feature feature = categories.get(position);
        holder.ivCategoryIcon.setImageResource(feature.getResourceId());
        holder.tvCategoryName.setText(categories.get(position).getFeatureName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (feature.getResourceId()) {
                    case R.drawable.ic_attendance2:
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                                R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        String date = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year;
                                        mContext.startActivity(new Intent(mContext, AttendanceActivity.class)
                                                .putExtra("date", date));

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                        break;


                    case R.drawable.ic_baseline_boy_24:
                      mContext.startActivity(new Intent(mContext, Students.class));


                    /*case R.drawable.ic_marks:
                        Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_select_exam);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                        Spinner spinCategories = dialog.findViewById(R.id.spin_cat);
                        Spinner spinSubCategories = dialog.findViewById(R.id.spin_sub_cat);
                        Button btnContinue = dialog.findViewById(R.id.btn_yes);

                        spinCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    FirebaseFirestore.getInstance()
                                            .collection("Exams")
                                            .whereEqualTo("ExamCtg",spinCategories.getSelectedItem().toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    List<Exam> exams = task.getResult().toObjects(Exam.class);

                                                    ArrayAdapter sectionsAdapter
                                                            = new ArrayAdapter(mContext,
                                                            android.R.layout.simple_spinner_dropdown_item,
                                                            exams);
                                                    spinSubCategories.setAdapter(sectionsAdapter);

                                                    spinSubCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                            ((TextView) view).setTextColor(ContextCompat.getColor(mContext, R.color.hint));
                                                            ((TextView) view).setTypeface(ResourcesCompat.getFont(mContext, R.font.poppinsregular));

                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                                        }
                                                    });
                                                }
                                            });




                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        btnContinue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (spinSubCategories.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                                    Toast.makeText(mContext, "Please select a category", Toast.LENGTH_SHORT).show();
                                } else {
                                    mContext.startActivity(new Intent(mContext, MarksActivity.class)
                                            .putExtra("exam",(Exam) spinSubCategories.getSelectedItem()));
                                }
                            }
                        });

                        dialog.show();

                        break;*/
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class DashboardViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private ImageView ivCategoryIcon;
        private CardView cardView;

        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_sales_manager);
            tvCategoryName = itemView.findViewById(R.id.tv_category_text);
            ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);
        }
    }
}