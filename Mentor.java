package com.example.hello.kjschedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by owner on 11/9/2017.
 */


public class Mentor implements Parcelable{
    private int mentorId;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;

    private static int highestMentorId = 0;

    private static ArrayList<Mentor> mentorArrayList = new ArrayList<>();

    public Mentor (){
        this("","","");
    }

    public Mentor (String mentorName, String mentorPhone, String mentorEmail){
        this.mentorId = highestMentorId + 1;
        highestMentorId++;

        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;

        addToMentorArrayList(this);
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getMentorPhone() {
        return mentorPhone;
    }

    public void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }

    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    }

    public static ArrayList<Mentor> getAllMentorArray() {
        return mentorArrayList;
    }

    public void addToMentorArrayList(Mentor mentor) {
        mentorArrayList.add(mentor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mentorId);
        parcel.writeString(mentorName);
        parcel.writeString(mentorPhone);
        parcel.writeString(mentorEmail);
        //parcel.writeTypedList(Mentor.getAllMentorArray());
        //parcel.writeList(courseAssessment);
        //parcel.writeValue(courseNotes);
    }
    private Mentor(Parcel in) {
        mentorId = in.readInt();
        mentorName = in.readString();
        mentorPhone = in.readString();
        mentorEmail = in.readString();
        //courseMentor =  in.readList(courseMentor,getClass().getClassLoader());
        //courseAssessment = in.readList(courseAssessment,getClass().getClassLoader());
        //courseNotes = in.readParcelable(getClass().getClassLoader());
    }
    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Mentor> CREATOR
            = new Parcelable.Creator<Mentor>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Mentor createFromParcel(Parcel in) {
            return new Mentor(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Mentor[] newArray(int size) {
            return new Mentor[size];
        }
    };
}
