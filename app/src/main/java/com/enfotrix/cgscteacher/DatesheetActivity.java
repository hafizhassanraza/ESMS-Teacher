package com.enfotrix.cgscteacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.enfotrix.cgscteacher.adapter.Adapter_DateSheet;
import com.enfotrix.cgscteacher.model.Model_DateSheet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DatesheetActivity extends AppCompatActivity {

    List<Model_DateSheet> dateSheetArrayList;


    private RecyclerView rv_dateSheet;
    private FirebaseFirestore db;
    private SaveSharedPreferences sharedPrefManager;
    private String sessionname, exam, type;
    private String sectioID, classgrade;
    private List<String> sub_list;
    private String classsection;
    private String substringdate, substringmonth, monthalphabetic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datesheet);


        init();
        getDatesheet();


    }

    public void init() {

        db = FirebaseFirestore.getInstance();
        sharedPrefManager = new SaveSharedPreferences();

        sub_list = new ArrayList<>();
        dateSheetArrayList = new ArrayList<>();

        rv_dateSheet = findViewById(R.id.rv_dateSheet);
        rv_dateSheet.setHasFixedSize(true);
        rv_dateSheet.setHasFixedSize(true);
        rv_dateSheet.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        sessionname = getIntent().getStringExtra("session");
        type = getIntent().getStringExtra("type");
        exam = getIntent().getStringExtra("exam");


    }

    private void getDatesheet() {

        db.collection("DateSheet").whereEqualTo("Exam", exam).whereEqualTo("Session", sessionname)
                .whereEqualTo("SectionName", sharedPrefManager.getClassSection(getApplicationContext()))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dateSheetArrayList = task.getResult().toObjects(Model_DateSheet.class);
                        }

                        Collections.sort(dateSheetArrayList, new Comparator<Model_DateSheet>() {
                            @Override
                            public int compare(Model_DateSheet feedback, Model_DateSheet t1) {
                                return feedback.getDate().trim().compareToIgnoreCase(t1.getDate().trim());
                            }
                        });

                        Adapter_DateSheet adapterDateSheet = new Adapter_DateSheet(getApplicationContext(), dateSheetArrayList);
                        rv_dateSheet.setAdapter(adapterDateSheet);
                        adapterDateSheet.notifyDataSetChanged();

                    }

                });


    }

}
