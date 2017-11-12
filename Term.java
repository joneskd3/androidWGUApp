package com.example.hello.kjschedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by owner on 11/9/2017.
 */

public class Term implements Parcelable{

    private int termId;
    private String termTitle;
    private String termStart;
    private String termEnd;
    private ArrayList<Course> termCourseArray = new ArrayList<>();

    private static ArrayList<Term> allTermArray = new ArrayList<>();

    private static int highestTermId = 0;

    public Term(){
        this("","","",null);
    }

    public Term(String termTitle, String termStart, String termEnd, ArrayList<Course> termCourseArray) {
        this.termId = highestTermId;
        highestTermId++;

        this.termTitle = termTitle;
        this.termStart = termStart;
        this.termEnd = termEnd;
        //this.termCourseArray = new ArrayList<>();

        addToAllTermArray(this);
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public ArrayList<Course> getTermCourseArray() {
        return termCourseArray;
    }

    public void addToTermCourseArray(Course course) {
        this.termCourseArray.add(course);
    }

    public static ArrayList<Term> getAllTermArray() {
        return allTermArray;
    }

    public static void addToAllTermArray(Term term) {
        Term.allTermArray.add(term);
    }

    @Override
    public String toString() {
        return this.getTermTitle() + "\t[" + this.getTermStart() + " - " + this.getTermEnd() + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(termId);
        parcel.writeString(termTitle);
        parcel.writeString(termStart);
        parcel.writeString(termEnd);
        parcel.writeTypedList(termCourseArray);
    }
    private Term(Parcel in) {
        termId = in.readInt();
        termTitle = in.readString();
        termStart = in.readString();
        termEnd = in.readString();
        in.readTypedList(termCourseArray, Course.CREATOR);

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
