package com.enfotrix.cgscteacher.model;


import java.io.Serializable;

public class Exam implements Serializable {
    private String ExamCtg;
    private String ExamName;
    private String ID; // Exam ID

    public Exam(){

    }

    public String getExamCtg() {
        return ExamCtg;
    }

    public void setExamCtg(String examCtg) {
        ExamCtg = examCtg;
    }

    public String getExamName() {
        return ExamName;
    }

    public void setExamName(String examName) {
        ExamName = examName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return ExamName;
    }
}
