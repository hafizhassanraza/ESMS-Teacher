package com.enfotrix.cgscteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.enfotrix.cgscteacher.adapter.StudentsMarksAdapter;
import com.enfotrix.cgscteacher.model.Exam;
import com.enfotrix.cgscteacher.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

public class MarksActivity extends AppCompatActivity {

    private static final String TAG = "MarksActivity";
    FirebaseFirestore db; // Firestore Instance
    private CollectionReference resultsRef;
    private List<Student> students;
    private List<Subject> subjects;
    private Exam exam;


    // Views
    private RecyclerView recyclerView;
    private TextView tvStudentCount, tvExamName;
    private EditText etTotalMarks;
    private ProgressBar progressBar;
    private Button btnSave;
    private Spinner spinSubjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

        init();
        fetchStudents();
        fetchSubjects();

        Bundle bundle = getIntent().getExtras();
        exam = (Exam) bundle.getSerializable("exam");




        /*btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etTotalMarks.getText().toString())) {
                    Toast.makeText(MarksActivity.this, "Please enter total marks", Toast.LENGTH_SHORT).show();
                } else if (spinSubjects.getSelectedItem().toString().equalsIgnoreCase("Select Subject")
                || subjects.size() <= 0) {
                    Toast.makeText(MarksActivity.this, "Please select a subject", Toast.LENGTH_SHORT).show();
                } else if (!validMarks()) {
                    Toast.makeText(MarksActivity.this, "Marks must range from (0-100)", Toast.LENGTH_SHORT).show();
                } else {
                    resultsRef = db.collection("Result")
                            .document("2022-23")
                            .collection("ResultExam")
                            .document(exam.getID())
                            .collection("ResultSections")
                            .document(SelectActivity.SECTION_ID)
                            .collection("ResultSubjects")
                            .document(((Subject)spinSubjects.getSelectedItem()).getID())
                            .collection("ResultStudents");


                        ProgressDialog progressDialog = new ProgressDialog(MarksActivity.this,
                                R.style.MyAlertDialogStyle);
                        progressDialog.setCancelable(false);
                        progressDialog.setTitle("Uploading Marks");
                        progressDialog.setMessage("Please Wait..");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();


                        for (int i = 0; i < students.size(); i++) {
                            Student student = students.get(i);

                                Map<String, Object> docData = new HashMap<>();
                                docData.put("FatherName", student.getFatherName());
                                docData.put("RegNumber", student.getRegNumber());
                                docData.put("ObtainMarks", student.getMarks());
                                docData.put("StudentId", student.getStudentId());
                                docData.put("StudentName", student.getFirstName() + " " + student.getLastName());
                                docData.put("SubjectName", spinSubjects.getSelectedItem().toString());
                                docData.put("TotalMarks", etTotalMarks.getText().toString().trim());

                                int finalI = i;
                                resultsRef.document(student.getStudentId())
                                        .set(docData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MarksActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                                                }

                                                if ( finalI == students.size() -1){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MarksActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(MarksActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });





                    }
                }
            }
        });*/
    }

    private boolean validMarks() {
        boolean isValid = true;
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
           if (student.getMarks() == null){
              students.get(i).setMarks("0");
           }

           if (Integer.parseInt(student.getMarks()) < 0 || Integer.parseInt(student.getMarks()) >100){
               isValid = false;
               break;
           }
        }

        return isValid;
    }

    private void fetchSubjects() {
        db.collection("Subjects")
                .whereEqualTo("SectionID", SelectActivity.SECTION_ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            subjects = task.getResult().toObjects(Subject.class);
                            //initSubjectsSpinner();

                        }
                    }
                });
    }

    /*private void initSubjectsSpinner() {
        ArrayAdapter sectionsAdapter
                = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subjects);


        subjects.add(0, new Subject("Select Subject"));
        spinSubjects.setAdapter(sectionsAdapter);
        spinSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.hint));
                ((TextView) view).setTypeface(ResourcesCompat.getFont(MarksActivity.this, R.font.poppinsregular));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }*/

    private void fetchStudents() {
        Log.d(TAG, "fetchStudents: " + SelectActivity.SECTION_ID);
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Students")
                .whereEqualTo("CurrentSection", SelectActivity.SECTION_ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            students = task.getResult().toObjects(Student.class);
                            if (students.size() > 0) {
                                recyclerView.setAdapter(new StudentsMarksAdapter(MarksActivity.this, students));
                                tvStudentCount.setText(students.size() + "");
                            } else {
                                Toast.makeText(MarksActivity.this, "No students found", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

    public void init() {
        btnSave = findViewById(R.id.btn_save);
        subjects = new ArrayList<>();
        students = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        etTotalMarks = findViewById(R.id.et_total);
        progressBar = findViewById(R.id.progress_bar);
        tvStudentCount = findViewById(R.id.tv_student_count);
        tvExamName = findViewById(R.id.tv_exam);
        recyclerView = findViewById(R.id.rv_students);
        spinSubjects = findViewById(R.id.spin_subjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }
}