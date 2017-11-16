package com.example.hello.kjschedule;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.hello.kjschedule.MainActivity.appDatabase;

@SuppressWarnings("unused")
public class Term implements Parcelable{

    //mapping
    @SuppressLint("UseSparseArrays")
    static HashMap<Integer,Term> allTermMap = new HashMap<>();

    //instance variables
    private int termId;
    private String termName;
    private String termStart;
    private String termEnd;

    private static int highestTermId = 0;

    //constructors
    Term(){
        this("","","");
    }
    Term(String termName, String termStart, String termEnd) {
        this.getHighestId();
        this.termId = highestTermId;

        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;
        //this.termCourseArray = new ArrayList<>();

        allTermMap.put(this.termId,this);
        insertIntoDB();
    }
    private Term(int termId, String termName, String termStart, String termEnd) {
        this.termId = termId;

        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;

        allTermMap.put(this.termId,this);
    }

    //setters and getters
    int getTermId() {
        return termId;
    }
    String getTermName() {
        return termName;
    }
    void setTermName(String termName) {
        this.termName = termName;
        this.updateDB();
    }
    String getTermStart() {
        return termStart;
    }
    void setTermStart(String termStart) {
        this.termStart = termStart;
        this.updateDB();
    }
    String getTermEnd() {
        return termEnd;
    }
    void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
        this.updateDB();
    }

    //arrays
    ArrayList<Course> getTermCourseArray() {

        ArrayList<Course> termCourseArray = new ArrayList<>();

        String query = "SELECT * FROM termCourse WHERE termId = " + this.getTermId();
        Cursor cursor = appDatabase.rawQuery(query, null);

        int courseIdField = cursor.getColumnIndex("courseId");

        if ( cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int courseId = cursor.getInt(courseIdField);
                termCourseArray.add(Course.allCourseMap.get(courseId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return termCourseArray;
    }
    static ArrayList<Term> getAllTermArray() {
        ArrayList<Term> allTermArray = new ArrayList<>();

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM term", null);

        int termIdField = cursor.getColumnIndex("termId");

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int termId = cursor.getInt(termIdField);
                allTermArray.add( Term.allTermMap.get(termId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return allTermArray;
    }

    /*Database Methods*/
    private void getHighestId(){

        String query = "SELECT MAX (termId) FROM term";
        Cursor cursor = appDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        highestTermId = cursor.getInt(0) + 1;
        cursor.close();
    }
    private void insertIntoDB(){
        appDatabase.execSQL(
                "INSERT INTO term(termId, termName, termStart, termEnd) " +
                        "VALUES(" +
                        this.termId + ", '" +
                        this.termName + "', '" +
                        this.termStart + "', '" +
                        this.termEnd + "')"
        );
    }
    void insertIntoDB(Course course){
        appDatabase.execSQL(
                "INSERT INTO termCourse(termId, courseId) " +
                        "VALUES(" +
                        this.termId + ", " +
                        course.getCourseId() + ")"
        );
    }
    private void updateDB(){
        appDatabase.execSQL(
                "UPDATE term " +
                        "SET " +
                        "termId = " + this.termId + ", " +
                        "termName = '" + this.termName + "', " +
                        "termStart = '" + this.termStart + "', " +
                        "termEnd = '" + (this.termEnd ) + "' " +
                        "WHERE termId = " + this.termId
        );
    }
    void clearTermCourseDB(){
        appDatabase.execSQL("DELETE FROM termCourse WHERE termId = " + this.getTermId());
    }
    public void deleteFromDB(){
        appDatabase.execSQL("DELETE from term WHERE termId = " + this.termId);
        appDatabase.execSQL("DELETE from termCourse WHERE termId = " + this.termId);
    }
    static void createFromDB() {

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM term", null);

        int termIdField = cursor.getColumnIndex("termId");
        int termNameField = cursor.getColumnIndex("termName");
        int termStartField = cursor.getColumnIndex("termStart");
        int termEndField = cursor.getColumnIndex("termEnd");

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int termId = cursor.getInt(termIdField);
                String termName = cursor.getString(termNameField);
                String termStart = cursor.getString(termStartField);
                String termEnd = cursor.getString(termEndField);

                new Term(termId, termName, termStart, termEnd);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    //parcelable methods
    public static final Parcelable.Creator<Term> CREATOR
            = new Parcelable.Creator<Term>() {

        @Override
        public Term createFromParcel(Parcel in) {
            return new Term(in);
        }

        @Override
        public Term[] newArray(int size) {
            return new Term[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(termId);
        parcel.writeString(termName);
        parcel.writeString(termStart);
        parcel.writeString(termEnd);
    }
    private Term(Parcel in) {
        termId = in.readInt();
        termName = in.readString();
        termStart = in.readString();
        termEnd = in.readString();
    }

    @Override
    public String toString() {
        return this.getTermName() + "\t[" + this.getTermStart() + " - " + this.getTermEnd() + "]";
    }
}
