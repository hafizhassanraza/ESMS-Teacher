package com.enfotrix.cgscteacher.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enfotrix.cgscteacher.AttendanceActivity;
import com.enfotrix.cgscteacher.Constants;
import com.enfotrix.cgscteacher.DatesheetActivity;
import com.enfotrix.cgscteacher.HomeActivity;
import com.enfotrix.cgscteacher.MarksActivity;
import com.enfotrix.cgscteacher.R;
import com.enfotrix.cgscteacher.SaveSharedPreferences;
import com.enfotrix.cgscteacher.SelectActivity;
import com.enfotrix.cgscteacher.adapter.AnnouncementAdapter;
import com.enfotrix.cgscteacher.adapter.FeaturesAdapter;
import com.enfotrix.cgscteacher.adapter.StudentsAttendanceAdapter;
import com.enfotrix.cgscteacher.adapter.TimetableAdapter;
import com.enfotrix.cgscteacher.adapter.sliderAdapter;
import com.enfotrix.cgscteacher.model.AnnouncementModel;
import com.enfotrix.cgscteacher.model.Feature;
import com.enfotrix.cgscteacher.model.SliderItem;
import com.enfotrix.cgscteacher.model.Student;
import com.enfotrix.cgscteacher.model.Timetable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private RecyclerView rvFeatures;
    private List<Feature> featuresList;

    private CardView cvDatesheet, cvTimetable;
    private FirebaseFirestore db;
    private TextView classname, classsection, totalstudents, classmedium, tvPresent, tvAbsent, tvLeave;

    private List<AnnouncementModel> announcementModelList;
    private RecyclerView rvAnnouncement;
    AnnouncementAdapter adapter;
    final static String status = "Approved";


    private String txtsession, txtexamtype, txtType, txtExam;
    private ArrayList<String> examtype;
    private ArrayList<String> examctg;
    private ArrayList<String> sessions;
    private List<Timetable> timetables = new ArrayList<>();


    private ImageView logoutimage;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);


        getSessions();
        sliderSet();
        getAnnouncements();


        classname.setText(SaveSharedPreferences.getClassName(getContext()));
        classsection.setText(SaveSharedPreferences.getClassSection(getContext()));
        totalstudents.setText(SaveSharedPreferences.getTotalStudents(getContext()));
        classmedium.setText(SaveSharedPreferences.getClassMedium(getContext()));

        logoutimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveSharedPreferences.clearUserName(getContext());
                startActivity(new Intent(getContext(), SelectActivity.class));
            }
        });


        getTodayAttendance();

        getAttendanceInfo();

        return view;
    }

    private void getTodayAttendance() {

    }

    private void init(View view) {

        db = FirebaseFirestore.getInstance();
        rvFeatures = view.findViewById(R.id.rv_features);
        featuresList = new ArrayList<>();
        featuresList.add(new Feature("Attendance", R.drawable.ic_attendance2));
        featuresList.add(new Feature("Students", R.drawable.ic_baseline_boy_24));
        //featuresList.add(new Feature("Marks", R.drawable.ic_marks));
        rvFeatures.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvFeatures.setAdapter(new FeaturesAdapter(getActivity(), featuresList));

        sessions = new ArrayList<>();
        examtype = new ArrayList<>();
        examctg = new ArrayList<>();

        viewPager2 = view.findViewById(R.id.viewpager_imageslider);
        logoutimage = view.findViewById(R.id.iv_logout);

        cvDatesheet = view.findViewById(R.id.cardDatesheet);
        cvTimetable = view.findViewById(R.id.cardTimetable);


        classname = view.findViewById(R.id.className);
        classsection = view.findViewById(R.id.classSection);
        totalstudents = view.findViewById(R.id.txtTotalStudents);
        classmedium = view.findViewById(R.id.textclassmedium);
        tvLeave = view.findViewById(R.id.tvLeave);
        tvAbsent = view.findViewById(R.id.tvAbsent);
        tvPresent = view.findViewById(R.id.tvPresent);


        rvAnnouncement = view.findViewById(R.id.rvAnnouncements);
        announcementModelList = new ArrayList<>();
        rvAnnouncement.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));

        cvDatesheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }


    public ArrayList<String> getSessions() {

        db.collection("Session").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    String sessionName = documentSnapshot.getString("SessionName");
                    sessions.add(sessionName);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });


        return sessions;
    }

    public void showDialog() {
        txtType = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = getLayoutInflater().inflate(R.layout.result_dialog, null);

        AppCompatSpinner spinner = view.findViewById(R.id.autoCompletetxt);
        AppCompatSpinner spnType = view.findViewById(R.id.text_type);
        AppCompatSpinner spnExam = view.findViewById(R.id.text_exam);
        AppCompatButton btn_getResult = view.findViewById(R.id.btn_getResult);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_session, sessions);
//        autoCompletetxt.setText(arrayAdapter.getItem(0).toString(), false);
        spinner.setAdapter(arrayAdapter);


       /* ArrayAdapter Adapter = new ArrayAdapter(getContext(), R.layout.dropdown_examtype, examtype);
        spinner1.setAdapter(Adapter);*/
//        text_examtype.setText(Adapter.getItem(0).toString(), false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                txtsession = adapterView.getItemAtPosition(i).toString();

                if (txtsession != null && txtType != null)
                    fetchresult(spnExam, txtsession, txtType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                txtType = adapterView.getItemAtPosition(i).toString();

                if (txtsession != null && txtType != null) {

                    fetchresult(spnExam, txtsession, txtType);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        spnExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                txtExam = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_getResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if (!txtExam.isEmpty() || txtExam != null) {
                        Intent resultIntent = new Intent(getActivity().getApplicationContext(), DatesheetActivity.class);


                        resultIntent.putExtra("session", txtsession);
                        resultIntent.putExtra("type", txtType);
                        resultIntent.putExtra("exam", txtExam);
                        startActivity(resultIntent);
                    } else
                        Toast.makeText(getContext(), "Please Select Exam!", Toast.LENGTH_SHORT).show();
                }


//                Toast.makeText(getContext(), classid + "\n" + classgrade, Toast.LENGTH_SHORT).show();

            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();


    }


    private void fetchresult(AppCompatSpinner spinner, String sessionstxt, String type) {

        examtype.clear();
        spinner.setAdapter(null);
        //Toast.makeText(getContext(), sessionstxt, Toast.LENGTH_SHORT).show();
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        db.collection("Exams").whereEqualTo("ExamCtg", type).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            examtype.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getString("ExamCtg").equals(type)) {
                                    examtype.add(document.getString("ExamName"));
                                    ArrayAdapter Adapter = new ArrayAdapter(getContext(), R.layout.dropdown_examtype, examtype);
                                    Adapter.notifyDataSetChanged();
                                    spinner.setAdapter(Adapter);
                                }
                            }
                            progressDialog.dismiss();

                        }
                    }
                });

    }

    private Runnable sliderRunnabler = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);


        }
    };


    private void getAttendanceInfo() {

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        Toast.makeText(getContext(), "" + date, Toast.LENGTH_SHORT).show();
        db.collection(Constants.KEY_COLLECTION_ATTENDANCE)
                .whereEqualTo(Constants.KEY_DATE, date)
                .whereEqualTo(Constants.KEY_SECTION_ID, SaveSharedPreferences.getSectionID(getContext()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int present = 0, absent = 0, leave = 0, totalStudents = 0;

                        boolean isAvailable = false;
                        if (task.isSuccessful() && task.getResult() != null
                                && task.getResult().getDocuments().size() > 0) {
                            totalStudents = task.getResult().getDocuments().size();

                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                String status = documentSnapshot.getString(Constants.KEY_STATUS);
                                assert status != null;
                                if (status.equals(Constants.KEY_PRESENT)) {
                                    present++;
                                } else if (status.equals(Constants.KEY_ABSENT)) {
                                    absent++;
                                } else if (status.equals(Constants.KEY_LEAVE)) {
                                    leave++;
                                }
                            }
                            isAvailable = true;
                            setAttendance(present, absent, leave, isAvailable);

                        } else {

                            Toast.makeText(getContext(), "Today's Attendance N/A", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getContext().this, "Today's Attendance N/A", Toast.LENGTH_SHORT).sh;
                            //binding.btnDetailedAttendance.setEnabled(false);
                        }

                        if (task.isComplete()) {
                            setAttendance(present, absent, leave, isAvailable);

                        }
                    }
                });
    }

    private void setAttendance(int present, int absent, int leave, boolean isAvailable) {

        if (isAvailable) {
            tvAbsent.setText(absent + "");
            tvPresent.setText(present + "");
            tvLeave.setText(leave + "");

        }

    }

    public void sliderSet() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        sliderItemList.add(new SliderItem(R.drawable.ad_one));
        sliderItemList.add(new SliderItem(R.drawable.ad_two));

        viewPager2.setAdapter(new sliderAdapter(sliderItemList, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnabler);
                sliderHandler.postDelayed(sliderRunnabler, 3000);
            }
        });

    }


    private void getAnnouncements() {

        db.collection("Announcement").whereEqualTo("Status", status).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    announcementModelList = task.getResult().toObjects(AnnouncementModel.class);
                    adapter = new AnnouncementAdapter(announcementModelList, getContext());
                    rvAnnouncement.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else
                    Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}