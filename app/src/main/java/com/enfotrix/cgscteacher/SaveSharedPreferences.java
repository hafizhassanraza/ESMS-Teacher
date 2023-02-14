package com.enfotrix.cgscteacher;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {
    static final String PREF_USER_NAME = "username";
    static final String PREF_CLASS_NAME = "classname";
    static final String PREF_CLASS_SECTION = "classsection";
    static final String PREF_TOTAL_STUDENTS = "totalstudents";
    static final String PREF_CLASS_MEDIUM = "classmedium";

    static final String PREF_SECTION_ID = "sectionid";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setTotalStudents(Context ctx, String totalStudents){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOTAL_STUDENTS, totalStudents);
        editor.commit();
    }


    public static void setUserName(Context ctx, String userName, String className, String classSection, String classMedium) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_CLASS_NAME, className);
        editor.putString(PREF_CLASS_SECTION, classSection);
        editor.putString(PREF_CLASS_MEDIUM, classMedium);
        editor.commit();
    }


    public static void saveSectionID(Context ctx, String sectionID) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_SECTION_ID, sectionID);
        editor.commit();
    }
    public static String getTotalStudents(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_TOTAL_STUDENTS, "");
    }

    public static String getSectionID(Context ctx) {

        return getSharedPreferences(ctx).getString(PREF_SECTION_ID, "");
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getClassName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_CLASS_NAME, "");
    }

    public static String getClassSection(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_CLASS_SECTION, "");
    }

    public static String getClassMedium(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_CLASS_MEDIUM, "");
    }

    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

}


