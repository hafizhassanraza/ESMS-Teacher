package com.enfotrix.cgscteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.enfotrix.cgscteacher.adapter.StudentListAdapter;
import com.enfotrix.cgscteacher.adapter.StudentsAttendanceAdapter;
import com.enfotrix.cgscteacher.adapter.StudentsMarksAdapter;
import com.enfotrix.cgscteacher.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Students extends AppCompatActivity {

    RecyclerView recyclerView;
    StudentListAdapter adapter;
    FirebaseFirestore db;
    private List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);


        init();
        fetchStudents();

    }


    private void fetchStudents() {
        db.collection("Students")
                .whereEqualTo("CurrentSection", SaveSharedPreferences.getSectionID(getApplicationContext()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            students = task.getResult().toObjects(Student.class);
                            if (students.size() > 0) {

                                Collections.sort(students, new Comparator<Student>() {
                                    @Override
                                    public int compare(Student student, Student t1) {
                                        return student.getRegNumber().trim().compareToIgnoreCase(t1.getRegNumber().trim());
                                    }
                                });


                                recyclerView.setAdapter(new StudentListAdapter(Students.this, students));
                            } else {
                                Toast.makeText(Students.this, "No students found", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }


    public void init() {

        students = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        adapter = new StudentListAdapter(Students.this, students);
        recyclerView = findViewById(R.id.rv_students_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }

}