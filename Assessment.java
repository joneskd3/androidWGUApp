package com.example.hello.kjschedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by owner on 11/9/2017.
 */

public class Assessment implements Parcelable {
    private int assessmentId;
    private String assessmentType;
    private String assessmentDescription;
    private String assessmentDueDate;
    private boolean assessmentDueReminder;

    private static int highestAssessmentId = 0;

    private static ArrayList<Assessment> assessmentArrayList = new ArrayList<>();

    public Assessment(String assessmentType, String assessmentDescription, String assessmentDueDate, boolean assessmentDueReminder) {
        this.assessmentId = highestAssessmentId + 1;
        highestAssessmentId++;
        this.assessmentType = assessmentType;
        this.assessmentDescription = assessmentDescription;
        this.assessmentDueDate = assessmentDueDate;
        this.assessmentDueReminder = assessmentDueReminder;

        addToAssessmentArray(this);
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentDescription() {
        return assessmentDescription;
    }

    public void setAssessmentDescription(String assessmentDescription) {
        this.assessmentDescription = assessmentDescription;
    }

    public String getAssessmentDueDate() {
        return assessmentDueDate;
    }

    public void setAssessmentDueDate(String assessmentDueDate) {
        this.assessmentDueDate = assessmentDueDate;
    }

    public boolean isAssessmentDueReminder() {
        return assessmentDueReminder;
    }

    public void setAssessmentDueReminder(boolean assessmentDueReminder) {
        this.assessmentDueReminder = assessmentDueReminder;
    }

    public static ArrayList<Assessment> getAllAssessmentArray() {
        return assessmentArrayList;
    }

    public static void addToAssessmentArray(Assessment assessment) {
        assessmentArrayList.add(assessment);
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
        parcel.writeInt(assessmentDueReminder ? 1 : 0);
        //parcel.writeTypedList(Mentor.getAllMentorArray());
        //parcel.writeList(courseAssessment);
        //parcel.writeValue(courseNotes);
    }
    private Assessment(Parcel in) {
        assessmentId = in.readInt();
        assessmentType = in.readString();
        assessmentDescription = in.readString();
        assessmentDueDate = in.readString();
        assessmentDueReminder = in.readInt() != 0;

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
}
