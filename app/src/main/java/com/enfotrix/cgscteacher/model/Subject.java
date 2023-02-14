package com.enfotrix.cgscteacher.model;

public class Subject {
    private String ClassID;
    private String ID; // Subject Id
    private String SectionID;
    private String SubjectName;

    public Subject(String subjectName){
        this.SubjectName = subjectName;
    }

    public Subject(){

    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSectionID() {
        return SectionID;
    }

    public void setSectionID(String sectionID) {
        SectionID = sectionID;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    @Override
    public String toString() {
        return SubjectName;
    }
}
