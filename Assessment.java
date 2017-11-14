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

public class Assessment implements Parcelable {
    public static HashMap<Integer,Assessment> allAssessmentMap = new HashMap<>();

    private int assessmentId;
    private String assessmentType;
    private String assessmentDescription;
    private String assessmentDueDate;
    private boolean assessmentReminder;

    private static int highestAssessmentId = 0;

    private static ArrayList<Assessment> assessmentArrayList = new ArrayList<>();

    public Assessment(){
        this("","","",false);
    }

    public Assessment(String assessmentType, String assessmentDescription, String assessmentDueDate, boolean assessmentReminder) {
        this.getHighestId();
        this.assessmentId = highestAssessmentId;

        this.assessmentType = assessmentType;
        this.assessmentDescription = assessmentDescription;
        this.assessmentDueDate = assessmentDueDate;
        this.assessmentReminder = assessmentReminder;

        allAssessmentMap.put(this.assessmentId,this);
        this.insertIntoDB();
    }
    public Assessment(int assessmentId, String assessmentType, String assessmentDescription, String assessmentDueDate, boolean assessmentReminder) {
        this.assessmentId = assessmentId;
        this.assessmentType = assessmentType;
        this.assessmentDescription = assessmentDescription;
        this.assessmentDueDate = assessmentDueDate;
        this.assessmentReminder = assessmentReminder;

        allAssessmentMap.put(this.assessmentId,this);
    }

    public int getAssessmentId() {
        return assessmentId;
    }
    public String getAssessmentType() {
        return assessmentType;
    }
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
        this.updateDB();
    }
    public String getAssessmentTitle() {
        return assessmentDescription;
    }
    public void setAssessmentTitle(String assessmentDescription) {
        this.assessmentDescription = assessmentDescription;
        this.updateDB();
    }
    public String getAssessmentDueDate() {
        return assessmentDueDate;
    }
    public void setAssessmentDueDate(String assessmentDueDate) {
        this.assessmentDueDate = assessmentDueDate;
        this.updateDB();
    }
    public boolean isAssessmentReminder() {
        return assessmentReminder;
    }
    public void setAssessmentReminder(boolean assessmentReminder) {
        this.assessmentReminder = assessmentReminder;
        this.updateDB();
    }


    public static ArrayList<Assessment> getAllAssessmentArray() {
        ArrayList<Assessment> allAssessmentArray = new ArrayList<>();

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM assessment", null);

        int assessmentIdField = cursor.getColumnIndex("assessmentId");

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int assessmentId = cursor.getInt(assessmentIdField);
                allAssessmentArray.add(allAssessmentMap.get(assessmentId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return allAssessmentArray;
    }


    /*Database Methods - add insert to constructor + add update into setters*/
    public void getHighestId(){

        String query = "SELECT MAX (assessmentId) FROM assessment";

        Cursor cursor = appDatabase.rawQuery(query,null);

        cursor.moveToFirst();

        highestAssessmentId = cursor.getInt(0) + 1;
    }
    public void insertIntoDB(){
        appDatabase.execSQL(
                "INSERT INTO assessment(assessmentId, assessmentType, assessmentDescription, assessmentDueDate, assessmentReminder) " +
                        "VALUES(" +
                        this.assessmentId + ", '" +
                        this.assessmentType + "', '" +
                        this.assessmentDescription + "', '" +
                        this.assessmentDueDate + "', " +
                        (this.assessmentReminder ? 1 : 0) + ")"
        );
    }
    public void updateDB(){
        appDatabase.execSQL(
                "UPDATE assessment " +
                        "SET " +
                        "assessmentId = " + this.assessmentId + ", " +
                        "assessmentType = '" + this.assessmentType + "', " +
                        "assessmentDescription = '" + this.assessmentDescription + "', " +
                        "assessmentDueDate = '" + this.assessmentDueDate + "', " +
                        "assessmentReminder = " + (this.assessmentReminder ? 1 : 0) + " " +
                        "WHERE assessmentId = " + this.assessmentId
        );
    }
    public void deleteFromDB(){
        appDatabase.execSQL("DELETE from assessment WHERE assessmentId = " + this.assessmentId);
    }
    public static void createFromDB() {

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM assessment", null);

        int assessmentIdField = cursor.getColumnIndex("assessmentId");
        int assessmentTypeField = cursor.getColumnIndex("assessmentType");
        int assessmentDescriptionField = cursor.getColumnIndex("assessmentDescription");
        int assessmentDueDateField = cursor.getColumnIndex("assessmentDueDate");
        int assessmentReminderField = cursor.getColumnIndex("assessmentReminder");

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int assessmentId = cursor.getInt(assessmentIdField);
                String assessmentType = cursor.getString(assessmentTypeField);
                String assessmentDescription = cursor.getString(assessmentDescriptionField);
                String assessmentDueDate = cursor.getString(assessmentDueDateField);
                Boolean assementReminder = cursor.getInt(assessmentReminderField) == 1;

                Assessment assessment = new Assessment(assessmentId, assessmentType, assessmentDescription, assessmentDueDate, assementReminder);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(assessmentId);
        parcel.writeString(assessmentType);
        parcel.writeString(assessmentDescription);
        parcel.writeString(assessmentDueDate);
        parcel.writeInt(assessmentReminder ? 1 : 0);
        //parcel.writeTypedList(Mentor.getAllMentorArray());
        //parcel.writeList(courseAssessment);
        //parcel.writeValue(courseNotes);
    }
    private Assessment(Parcel in) {
        assessmentId = in.readInt();
        assessmentType = in.readString();
        assessmentDescription = in.readString();
        assessmentDueDate = in.readString();
        assessmentReminder = in.readInt() != 0;

        //courseAssessment =  in.readList(courseAssessment,getClass().getClassLoader());
        //courseAssessment = in.readList(courseAssessment,getClass().getClassLoader());
        //courseNotes = in.readParcelable(getClass().getClassLoader());
    }
    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Assessment> CREATOR
            = new Parcelable.Creator<Assessment>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Assessment createFromParcel(Parcel in) {
            return new Assessment(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Assessment[] newArray(int size) {
            return new Assessment[size];
        }
    };

    @Override
    public String toString() {

        return this.getAssessmentTitle() + " [" + this.getAssessmentType() + "]";
    }
}
