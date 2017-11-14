package com.example.hello.kjschedule;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.hello.kjschedule.MainActivity.appDatabase;

/**
 * Created by owner on 11/9/2017.
 */

public class Term implements Parcelable{

    public static HashMap<Integer,Term> allTermMap = new HashMap<>();

    private int termId;
    private String termName;
    private String termStart;
    private String termEnd;
    private ArrayList<Course> termCourseArray = new ArrayList<>();

    private static ArrayList<Term> allTermArray = new ArrayList<>();

    private static int highestTermId = 0;

    public Term(){
        this("","","");
    }

    public Term(String termName, String termStart, String termEnd) {
        this.getHighestId();
        this.termId = highestTermId;

        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;
        //this.termCourseArray = new ArrayList<>();

        allTermMap.put(this.termId,this);
        insertIntoDB();
    }
    public Term(int termId, String termName, String termStart, String termEnd) {
        this.termId = termId;

        this.termName = termName;
        this.termStart = termStart;
        this.termEnd = termEnd;

        allTermMap.put(this.termId,this);
    }


    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
        this.updateDB();
    }

    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
        this.updateDB();
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
        this.updateDB();
    }

    public ArrayList<Course> getTermCourseArray() {

        return termCourseArray;
    }

    public void addToTermCourseArray(Course course) {
        this.termCourseArray.add(course);
    }

    public static ArrayList<Term> getAllTermArray() {
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

    public static void addToAllTermArray(Term term) {
        Term.allTermArray.add(term);
    }

    @Override
    public String toString() {
        return this.getTermName() + "\t[" + this.getTermStart() + " - " + this.getTermEnd() + "]";
    }

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
        parcel.writeTypedList(termCourseArray);
    }
    private Term(Parcel in) {
        termId = in.readInt();
        termName = in.readString();
        termStart = in.readString();
        termEnd = in.readString();
        in.readTypedList(termCourseArray, Course.CREATOR);

    }

    /*Database Methods - add insert to constructor + add update into setters*/
    public void getHighestId(){

        String query = "SELECT COUNT(*) AS count FROM term";

        Cursor cursor = appDatabase.rawQuery(query,null);

        cursor.moveToFirst();

        highestTermId = cursor.getInt(0);
    }
    //add to constructor
    public void insertIntoDB(){
        appDatabase.execSQL(
                "INSERT INTO term(termId, termName, termStart, termEnd) " +
                        "VALUES(" +
                        this.termId + ", '" +
                        this.termName + "', '" +
                        this.termStart + "', '" +
                        this.termEnd + "')"
        );
    }
    public void updateDB(){
        appDatabase.execSQL(
                "UPDATE term " +
                        "SET " +
                        "termid = " + this.termId + ", " +
                        "termname = '" + this.termName + "', " +
                        "termStart = '" + this.termStart + "', " +
                        "termEnd = '" + (this.termEnd ) + "' " +
                        "WHERE termId = " + this.termId
        );
    }
    public void deleteFromDB(){
        appDatabase.execSQL("DELETE from term WHERE termId = " + this.termId);
    }
    public static void createFromDB() {

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM term", null);

        int termIdField = cursor.getColumnIndex("termId");
        int termNameField = cursor.getColumnIndex("termName");
        int termStartField = cursor.getColumnIndex("termStart");
        int termEndField = cursor.getColumnIndex("termEnd");

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                int termId = cursor.getInt(termIdField);
                String termName = cursor.getString(termNameField);
                String termStart = cursor.getString(termStartField);
                String termEnd = cursor.getString(termEndField);


                Term term = new Term(termId, termName, termStart, termEnd);

            } while (cursor.moveToNext());
        }
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Term> CREATOR
            = new Parcelable.Creator<Term>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Term createFromParcel(Parcel in) {
            return new Term(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Term[] newArray(int size) {
            return new Term[size];
        }
    };



}
