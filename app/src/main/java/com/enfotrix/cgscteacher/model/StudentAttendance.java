package com.enfotrix.cgscteacher.model;

public class StudentAttendance {
    private String FatherName;
    private String RegNumber;
    private String Status;
    private String StudentID;
    private String StudentName;

    public StudentAttendance(String fatherName, String regNumber, String status, String studentID, String studentName) {
        FatherName = fatherName;
        RegNumber = regNumber;
        this.Status = status;
        StudentID = studentID;
        StudentName = studentName;
    }

    public StudentAttendance() {
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getRegNumber() {
        return RegNumber;
    }

    public void setRegNumber(String regNumber) {
        RegNumber = regNumber;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }
}
