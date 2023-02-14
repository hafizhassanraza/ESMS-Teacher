package com.enfotrix.cgscteacher.model;

public class Timetable {
    String Starttime, Endtime, Subject;

    public Timetable(){

    }

    public String getStarttime() {
        return Starttime;
    }

    public void setStarttime(String starttime) {
        Starttime = starttime;
    }

    public String getEndtime() {
        return Endtime;
    }

    public void setEndtime(String endtime) {
        Endtime = endtime;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public Timetable(String starttime, String endtime, String subject) {
        Starttime = starttime;
        Endtime = endtime;
        Subject = subject;
    }
}
