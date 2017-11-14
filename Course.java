package com.example.hello.kjschedule;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.HashMap;

import static com.example.hello.kjschedule.MainActivity.appDatabase;


public class Course implements Parcelable {

    /*All Courses*/
    public static HashMap<Integer, Course> allCourseMap = new HashMap<Integer, Course>();

    private int courseId;
    private String courseName;
    private String courseStartDate;
    private boolean courseStartAlert;
    private String courseEndDate;
    private boolean courseEndAlert;
    private String courseStatus;

    /*Arrays*/
    //private ArrayList<Mentor> courseMentor = new ArrayList<>();
    //private ArrayList<Assessment> courseAssessmentArray = new ArrayList<>();
    //private ArrayList<Note> courseNoteArray = new ArrayList<>();

    /*Statics*/
    private static int highestCourseId = 0;

    /*Constructors*/
    public Course() {
        this("", "", false, "",
                false, "");
    }

    public Course(String courseName, String courseStartDate, boolean courseStartAlert,
                  String courseEndDate, boolean courseEndAlert, String courseStatus) {
        getHighestCourseId();
        this.courseId = highestCourseId;

        this.courseName = courseName;
        this.courseStartDate = courseStartDate;
        this.courseStartAlert = courseStartAlert;
        this.courseEndDate = courseEndDate;
        this.courseEndAlert = courseEndAlert;
        this.courseStatus = courseStatus;

        allCourseMap.put(this.courseId,this);
        this.insertIntoDB();
    }

    /*Constructor for DB population*/
    public Course(int courseId, String courseName, String courseStartDate, boolean courseStartAlert,
                  String courseEndDate, boolean courseEndAlert, String courseStatus) {

        this.courseId = courseId;

        this.courseName = courseName;
        this.courseStartDate = courseStartDate;
        this.courseStartAlert = courseStartAlert;
        this.courseEndDate = courseEndDate;
        this.courseEndAlert = courseEndAlert;
        this.courseStatus = courseStatus;

        allCourseMap.put(this.courseId,this);
    }

    /*Setters and Getters*/

    public int getCourseId() {
        return courseId;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
        this.updateDB();
    }
    public String getCourseStartDate() {
        return courseStartDate;
    }
    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
        this.updateDB();
    }
    public boolean isCourseStartAlert() {
        return courseStartAlert;
    }
    public void setCourseStartAlert(boolean courseStartAlert) {
        this.courseStartAlert = courseStartAlert;
        this.updateDB();
    }
    public String getCourseEndDate() {
        return courseEndDate;
    }
    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
        this.updateDB();
    }
    public boolean isCourseEndAlert() {
        return courseEndAlert;
    }
    public void setCourseEndAlert(boolean courseEndAlert) {
        this.courseEndAlert = courseEndAlert;
        this.updateDB();
    }
    public String getCourseStatus() {
        return courseStatus;
    }
    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
        this.updateDB();
    }

    /*ArrayList Methods*/
    public ArrayList<Mentor> getCourseMentorArray() {

        ArrayList<Mentor> courseMentorArray = new ArrayList<>();

        String query = "SELECT * FROM courseMentor WHERE courseId = " + this.getCourseId();
        Cursor cursor = appDatabase.rawQuery(query, null);

        int mentorIdField = cursor.getColumnIndex("mentorId");

        if ( cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int mentorId = cursor.getInt(mentorIdField);
                courseMentorArray.add(Mentor.allMentorMap.get(mentorId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return courseMentorArray;
    }
    public ArrayList<Assessment> getCourseAssessmentArray() {

        ArrayList<Assessment> courseAssessmentArray = new ArrayList<>();

        String query = "SELECT * FROM courseAssessment WHERE courseId = " + this.getCourseId();
        Cursor cursor = appDatabase.rawQuery(query, null);

        int assessmentIdField = cursor.getColumnIndex("assessmentId");

        if ( cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int assessmentId = cursor.getInt(assessmentIdField);
                courseAssessmentArray.add(Assessment.allAssessmentMap.get(assessmentId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return courseAssessmentArray;
    }
    public ArrayList<Note> getCourseNoteArray() {

        ArrayList<Note> courseNoteArray = new ArrayList<>();

        String query = "SELECT * FROM note WHERE courseId = " + this.getCourseId();
        Cursor cursor = appDatabase.rawQuery(query, null);

        int noteIdField = cursor.getColumnIndex("noteId");

        if ( cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int noteId = cursor.getInt(noteIdField);
                courseNoteArray.add(Note.allNoteMap.get(noteId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return courseNoteArray;
    }
    /*
    public ArrayList<Assessment> getCourseAssessmentArray() {
        return true;
    }
    */
    /*
    public ArrayList<Note> getCourseNotesArray() {
        return courseNoteArray;
    }
    */
    public static ArrayList<Course> getAllCourseArray() {
        ArrayList<Course> allCourseArray = new ArrayList<>();

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM course", null);

        int courseIdField = cursor.getColumnIndex("courseId");

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int courseId = cursor.getInt(courseIdField);
                allCourseArray.add(allCourseMap.get(courseId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return allCourseArray;
    }

    /*Database Methods - add insert to constructor + add update into setters*/
    public void getHighestCourseId(){

        String query = "SELECT MAX(courseId) AS max FROM course";

        Cursor cursor = appDatabase.rawQuery(query,null);

        cursor.moveToFirst();

        highestCourseId = cursor.getInt(0) + 1;
    }
    public void insertIntoDB(){
        appDatabase.execSQL(
                "INSERT INTO course(courseId, courseName, courseStartDate, courseStartAlert, " +
                        "courseEndDate, courseEndAlert, courseStatus) " +
                        "VALUES(" +
                        this.courseId + ", '" +
                        this.courseName + "', '" +
                        this.courseStartDate + "', " +
                        (this.courseStartAlert ? 1 : 0) + ", '" +
                        this.courseEndDate + "', " +
                        (this.courseEndAlert ? 1 : 0) + ", '" +
                        this.courseStatus + "')"
        );
    }
    public void insertIntoDB(Mentor mentor){
        appDatabase.execSQL(
                "INSERT INTO courseMentor(courseId, mentorId) " +
                        "VALUES(" +
                        this.courseId + ", " +
                        mentor.getMentorId() + ")"
        );
    }
    public void insertIntoDB(Assessment assessment){
        appDatabase.execSQL(
                "INSERT INTO courseAssessment(courseId, assessmentId) " +
                        "VALUES(" +
                        this.courseId + ", " +
                        assessment.getAssessmentId() + ")"
        );
    }
    public void clearCourseMentorDB(){
        appDatabase.execSQL("DELETE FROM courseMentor WHERE courseId =" + this.getCourseId());
    }
    public void clearCourseAssessmentDB(){
        appDatabase.execSQL("DELETE FROM courseAssessment WHERE courseId =" + this.getCourseId());
    }
    public void updateDB(){
        appDatabase.execSQL(
                "UPDATE course " +
                "SET " +
                    "courseId = " + this.courseId + ", " +
                    "courseName = '" + this.courseName + "', " +
                    "courseStartDate = '" + this.courseStartDate + "', " +
                    "courseStartAlert = " + (this.courseStartAlert ? 1 : 0)+ ", " +
                    "courseEndDate = '" + this.courseEndDate + "', " +
                    "courseEndAlert = " + (this.courseEndAlert ? 1 : 0) + ", " +
                    "courseStatus = '" + this.courseStatus + "' " +
                "WHERE courseID = " + this.getCourseId()
        );
    }
    public void updateDB(Mentor mentor){
        appDatabase.execSQL(
                "UPDATE courseMentor " +
                "SET " +
                    "courseId = " + this.courseId + ", " +
                    "mentorId = " + mentor.getMentorId() + " " +
                "WHERE courseID = " + this.getCourseId()
        );
    }
    public void deleteFromDB(){
        appDatabase.execSQL("DELETE from course WHERE courseId = " + this.courseId);
        appDatabase.execSQL("DELETE from courseMentor WHERE courseId = " + this.courseId);
        appDatabase.execSQL("DELETE from courseAssessment WHERE courseId = " + this.courseId);
        appDatabase.execSQL("DELETE from termCourse WHERE courseId = " + this.courseId);
        appDatabase.execSQL("DELETE from note WHERE courseId = " + this.courseId);
    }
    public static void createCourseFromDB() {

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM course", null);

        int courseIdField = cursor.getColumnIndex("courseId");
        int courseNameField = cursor.getColumnIndex("courseName");
        int courseStartDateField = cursor.getColumnIndex("courseStartDate");
        int courseStartAlertField = cursor.getColumnIndex("courseStartAlert");
        int courseEndDateField = cursor.getColumnIndex("courseEndDate");
        int courseEndAlertField = cursor.getColumnIndex("courseEndAlert");
        int courseStatusField = cursor.getColumnIndex("courseStatus");
        int activeField = cursor.getColumnIndex("active");

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                int courseId = Integer.parseInt(cursor.getString(courseIdField));
                String courseName = cursor.getString(courseNameField);
                String courseStartDate = cursor.getString(courseStartDateField);
                Boolean courseStartAlert = cursor.getInt(courseStartAlertField) == 1;
                String courseEndDate = cursor.getString(courseEndDateField);
                Boolean courseEndAlert = cursor.getInt(courseEndAlertField) == 1;
                String courseStatus = cursor.getString(courseStatusField);

                Course course = new Course(courseId, courseName, courseStartDate, courseStartAlert,
                        courseEndDate, courseEndAlert, courseStatus);

            } while (cursor.moveToNext());
        }
    }


    /*Parcelable Methods*/
    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {

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
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(courseId);
        parcel.writeString(courseName);
        parcel.writeString(courseStartDate);
        parcel.writeInt(courseStartAlert ? 1 : 0);
        parcel.writeString(courseEndDate);
        parcel.writeInt(courseEndAlert ? 1 : 0);
        parcel.writeString(courseStatus);
        //parcel.writeTypedList(courseMentor);
        //parcel.writeTypedList(courseAssessmentArray);
        //parcel.writeTypedList(courseNoteArray);
    }
    private Course(Parcel in) {
        courseId = in.readInt();
        courseName = in.readString();
        courseStartDate = in.readString();
        courseStartAlert = in.readInt() != 0;
        courseEndDate = in.readString();
        courseEndAlert = in.readInt() != 0;
        courseStatus = in.readString();
        //in.readTypedList(courseMentor, Mentor.CREATOR);
        //in.readTypedList(courseAssessmentArray, Assessment.CREATOR);
        //in.readTypedList(courseNoteArray, Note.CREATOR);
    }

    /*String Methods*/
    @Override
    public String toString () {
        return this.getCourseName();
    }
}