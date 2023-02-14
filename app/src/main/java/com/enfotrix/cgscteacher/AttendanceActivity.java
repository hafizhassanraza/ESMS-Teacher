package com.enfotrix.cgscteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.enfotrix.cgscteacher.*;

import com.enfotrix.cgscteacher.adapter.StudentsAttendanceAdapter;
import com.enfotrix.cgscteacher.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    private static final String TAG = "AttendanceActivity";
    FirebaseFirestore db; // Firestore Instance

    private List<Student> students;
    private CollectionReference attendanceReference;


    // Views
    private RecyclerView recyclerView;
    private TextView presents, absents, leaves;
    private TextView tvStudentCount, tvClassName;
    private ProgressBar progressBar;
    private Button btnSave;
    private String date;
    StudentsAttendanceAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        init();
        fetchStudents();


        tvClassName.setText(SaveSharedPreferences.getClassName(getApplicationContext()));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            date = bundle.getString("date");
        }
        /*attendanceReference = db.collection("Attendance")
                .document("2022-23")
                .collection("AttendanceMonth")
                .document(date.substring(3, 5))
                .collection("AttendanceDate")
                .document(date)
                .collection("AttendanceSections")
                .document(SelectActivity.SECTION_ID)
                .collection("AttendanceStudents");*/


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {


                    if (students.size() > 0) {
                        ProgressDialog progressDialog = new ProgressDialog(AttendanceActivity.this,
                                R.style.MyAlertDialogStyle);
                        progressDialog.setCancelable(false);
                        progressDialog.setTitle("Uploading Attendance");
                        progressDialog.setMessage("Please Wait..");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();


                        Toast.makeText(AttendanceActivity.this, SaveSharedPreferences.getSectionID(AttendanceActivity.this)+"", Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < students.size(); i++) {
                            Student student = students.get(i);
                            String DocID = date + student.getStudentId();
                            Map<String, Object> docData = new HashMap<>();

                            if (student.getStatus() != null)
                                docData.put("Status", student.getStatus());
                            else docData.put("Status", "Present");


                            docData.put("DocID", DocID);
                            docData.put("StudentID", student.getStudentId());
                            docData.put("RegNumber", student.getRegNumber());
                            docData.put("StudentName", student.getFirstName() + " " + student.getLastName());
                            docData.put("FatherName", student.getFatherName());
                            docData.put("Session", "2022-23");
                            docData.put("Date", date);
                            docData.put("SectionID", SaveSharedPreferences.getSectionID(AttendanceActivity.this));
                            int finalI = i;


                            db.collection("Attendance").document(DocID).set(docData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AttendanceActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Common.sendNotification(student.getToken(), AttendanceActivity.this,
                                                        student.getFirstName() + " " + student.getLastName()
                                                                + " is " + student.getStatus() + "  today");
                                            }

                                            if (finalI == students.size() - 1) {
                                                progressDialog.dismiss();
                                                Toast.makeText(AttendanceActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        }


                    }


                } else
                    Toast.makeText(AttendanceActivity.this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchStudents() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Students")
                .whereEqualTo("CurrentSection", SaveSharedPreferences.getSectionID(getApplicationContext()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            students = task.getResult().toObjects(Student.class);
                            if (students.size() > 0) {
                                Collections.sort(students, new Comparator<Student>() {
                                    @Override
                                    public int compare(Student student, Student t1) {
                                        return student.getRegNumber().trim().compareToIgnoreCase(t1.getRegNumber().trim());
                                    }
                                });

                                adapter = new StudentsAttendanceAdapter(AttendanceActivity.this, students);
                                recyclerView.setAdapter(adapter);
                                tvStudentCount.setText(students.size() + "");
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(AttendanceActivity.this, "No students found", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }

    public void init() {


        recyclerView = findViewById(R.id.rv_students);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        db = FirebaseFirestore.getInstance();
        students = new ArrayList<>();
        tvStudentCount = findViewById(R.id.tv_student_count);
        tvClassName = findViewById(R.id.tv_class_name);
        progressBar = findViewById(R.id.progress_bar);
        btnSave = findViewById(R.id.btn_save);
 /*       presents = findViewById(R.id.presents);
        leaves = findViewById(R.id.leaves);
        absents = findViewById(R.id.absents);*/
    }

}