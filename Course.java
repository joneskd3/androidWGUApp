package com.example.hello.kjschedule;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.hello.kjschedule.Mentor;


import java.util.ArrayList;

/**
 * Created by owner on 11/9/2017.
 */

public class Course implements Parcelable {
    private int courseId;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    private String courseName;
    private String courseStartDate;
    private boolean courseStartAlert;
    private String courseEndDate;
    private boolean courseEndAlert;
    private String courseStatus;
    private ArrayList<Mentor> courseMentor = new ArrayList<>();
    private ArrayList<Assessment> courseAssessment = new ArrayList<>();
    private ArrayList<String> courseNotes;

    private static int highestCourseId = 0;

    private static ArrayList<Course> courseArrayList = new ArrayList<>();

    public Course(){
        this("","",false,"",false,"",null,null,null);
    }

    public Course(String courseName, String courseStartDate, boolean courseStartAlert,
                  String courseEndDate, boolean courseEndAlert, String courseStatus,
                  ArrayList<Mentor> courseMentor, ArrayList<Assessment> courseAssessment,
                  ArrayList<String> courseNotes) {
        this.courseId = highestCourseId + 1;
        highestCourseId++;

        this.courseName = courseName;
        this.courseStartDate = courseStartDate;
        this.courseStartAlert = courseStartAlert;
        this.courseEndDate = courseEndDate;
        this.courseEndAlert = courseEndAlert;
        this.courseStatus = courseStatus;
        //this.courseMentor = new ArrayList<>();
        //this.courseAssessment = new ArrayList<>();
        this.courseNotes = courseNotes;

        addToCourseArrayList(this);
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public boolean isCourseStartAlert() {
        return courseStartAlert;
    }

    public void setCourseStartAlert(boolean courseStartAlert) {
        this.courseStartAlert = courseStartAlert;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public boolean isCourseEndAlert() {
        return courseEndAlert;
    }

    public void setCourseEndAlert(boolean courseEndAlert) {
        this.courseEndAlert = courseEndAlert;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public ArrayList<Mentor> getCourseMentor() {
        return courseMentor;
    }

    public void setCourseMentor(ArrayList<Mentor> courseMentor) {
        this.courseMentor = courseMentor;
    }

    public ArrayList<Assessment> getCourseAssessment() {
        return courseAssessment;
    }

    public void setCourseAssessment(ArrayList<Assessment> courseAssessment) {
        this.courseAssessment = courseAssessment;
    }

    public ArrayList<String> getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(ArrayList<String> courseNotes) {
        this.courseNotes = courseNotes;
    }

    public static ArrayList<Course> getAllCourseArray() {
        return courseArrayList;
    }

    public void addToCourseArrayList(Course course) {
        courseArrayList.add(course);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(courseName);
        parcel.writeString(courseStartDate);
        parcel.writeInt(courseStartAlert ? 1 : 0);
        parcel.writeString(courseEndDate);
        parcel.writeInt(courseEndAlert ? 1 : 0);
        parcel.writeString(courseStatus);
        parcel.writeTypedList(courseMentor);
        parcel.writeTypedList(courseAssessment);
        //parcel.writeValue(courseNotes);
    }
    private Course(Parcel in) {
        courseName = in.readString();
        courseStartDate = in.readString();
        courseStartAlert = in.readInt() != 0;
        courseEndDate = in.readString();
        courseEndAlert = in.readInt() != 0;
        courseStatus = in.readString();
        in.readTypedList(courseMentor, Mentor.CREATOR);
        in.readTypedList(courseAssessment,Assessment.CREATOR);
        //courseNotes = in.readParcelable(getClass().getClassLoader());
    }
    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Course> CREATOR
            = new Parcelable.Creator<Course>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public ArrayList<Mentor> getCourseMentorArray(){
        return courseMentor;
    }
    public void addToCourseMentor(Mentor mentor){
        courseMentor.add(mentor);
    }
    public ArrayList<Assessment> getCourseAssessmentArray(){
        return courseAssessment;
    }
    public void addToCourseAssessment(Assessment assessment){
        courseAssessment.add(assessment);
    }


    @Override
    public String toString() {
        return this.getCourseName();
    }

}
