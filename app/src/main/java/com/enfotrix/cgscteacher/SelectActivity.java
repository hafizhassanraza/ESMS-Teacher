package com.enfotrix.cgscteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.enfotrix.cgscteacher.adapter.StudentsAttendanceAdapter;
import com.enfotrix.cgscteacher.model.Class;
import com.enfotrix.cgscteacher.model.Section;
import com.enfotrix.cgscteacher.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
    private static final String TAG = "SelectActivity";
    FirebaseFirestore db; // Firestore Instance
    private List<Class> classes;
    private List<Section> sections;
    public static String CLASS_NAME;
    public static String SECTION_ID;
    private EditText etPassword;
    private List<Student> students;


    private SaveSharedPreferences saveSharedPreference;


    // Views
    private Spinner spinClasses, spinSections;
    private Button button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        init();
        fetchClasses();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinClasses.getSelectedItemId() != 0 && spinSections.getSelectedItemId() != 0
                        && classes.size() > 0) {

                    CLASS_NAME = classes.get(spinClasses.getSelectedItemPosition()).getClassName();
                    if (sections.size() > 0) {
                        SECTION_ID = sections.get(spinSections.getSelectedItemPosition()).getDocID();

                        fetchStudents();

                        db.collection("Class").document(CLASS_NAME)
                                .collection("Sections").document(SECTION_ID).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {

                                            String Section = task.getResult().getString("SectionName");
                                            String Medium = task.getResult().getString("Medium");


                                            String Pass = task.getResult().getString("Password");
                                            if (Pass != null) {

                                                if (task.getResult().getString("Password").equals(etPassword.getText().toString())) {
                                                    SaveSharedPreferences.setUserName(getApplicationContext(), etPassword.getText().toString(), CLASS_NAME, Section, Medium);
                                                    SaveSharedPreferences.saveSectionID(getApplicationContext(), SECTION_ID);


                                                    Intent intent = new Intent(SelectActivity.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                } else
                                                    Toast.makeText(SelectActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                                            } else
                                                Toast.makeText(SelectActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }




                    /*

                     */

                } else {
                    Toast.makeText(SelectActivity.this, "Please Select Class/Section", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void fetchClasses() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Class")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            classes.clear();
                            classes = task.getResult().toObjects(Class.class);
                            classes.add(0, new Class("Select Class"));
                            initClassesSpinner();

                        } else {
                            Toast.makeText(SelectActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SelectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchSections(String className) {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("Class").document(className)
                .collection("Sections")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            sections.clear();

                            if (task.getResult().size() > 0) {
                                sections = task.getResult().toObjects(Section.class);
                                sections.add(0, new Section("Select Section"));

                            } else {
                                Toast.makeText(SelectActivity.this, "No Sections for this class", Toast.LENGTH_SHORT).show();
                            }

                            initSectionsSpinner();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SelectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initClassesSpinner() {
        ArrayAdapter classesAdapter
                = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, classes);

        spinClasses.setAdapter(classesAdapter);
        spinClasses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.hint));
                ((TextView) view).setTypeface(ResourcesCompat.getFont(SelectActivity.this, R.font.poppinsregular));

                if (spinClasses.getSelectedItemId() != 0) {
                    fetchSections(spinClasses.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void initSectionsSpinner() {
        ArrayAdapter sectionsAdapter
                = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sections);


        spinSections.setAdapter(sectionsAdapter);
        spinSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.hint));
                ((TextView) view).setTypeface(ResourcesCompat.getFont(SelectActivity.this, R.font.poppinsregular));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void fetchStudents() {
        db.collection("Students")
                .whereEqualTo("CurrentSection", SECTION_ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            students = task.getResult().toObjects(Student.class);
                            if (students.size() > 0) {
                                SaveSharedPreferences.setTotalStudents(SelectActivity.this, students.size() + "");
                            } else {
                                Toast.makeText(SelectActivity.this, "No students found", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }


    // Initialize all the views and variables
    public void init() {
        db = FirebaseFirestore.getInstance();
        spinClasses = findViewById(R.id.spin_class);
        spinSections = findViewById(R.id.spin_sections);
        classes = new ArrayList<>();
        sections = new ArrayList<>();
        button = findViewById(R.id.btn_continue);
        progressBar = findViewById(R.id.progress_bar);
        etPassword = findViewById(R.id.etPassword);
    }
}