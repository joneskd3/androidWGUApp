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


public class Mentor implements Parcelable{
    public static HashMap<Integer,Mentor> allMentorMap = new HashMap<>();

    private int mentorId;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;

    private static int highestMentorId = 0;

    public Mentor (){
        this("","","");
    }
    public Mentor (String mentorName, String mentorPhone, String mentorEmail){
        this.getHighestId();
        this.mentorId = highestMentorId;

        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;

        this.allMentorMap.put(this.mentorId,this);
        this.insertIntoDB();

    }
    public Mentor (int mentorId, String mentorName, String mentorPhone, String mentorEmail){
        this.mentorId = mentorId;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;

        this.allMentorMap.put(this.mentorId,this);
    }

    public int getMentorId() {
        return mentorId;
    }
    public String getMentorName() {
        return mentorName;
    }
    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
        this.updateDB();
    }
    public String getMentorPhone() {
        return mentorPhone;
    }
    public void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
        this.updateDB();

    }
    public String getMentorEmail() {
        return mentorEmail;
    }
    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
        this.updateDB();

    }


    public static ArrayList<Mentor> getAllMentorArray() {
        ArrayList<Mentor> allMentorArray = new ArrayList<>();

        String query = "SELECT * FROM mentor";
        Cursor cursor = appDatabase.rawQuery(query, null);

        int mentorIdField = cursor.getColumnIndex("mentorId");

        if ( cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int mentorId = cursor.getInt(mentorIdField);
                allMentorArray.add(Mentor.allMentorMap.get(mentorId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return allMentorArray;
    }

    /*Database Methods - add insert to constructor + add update into setters*/
    public void getHighestId(){

        String query = "SELECT COUNT(*) AS count FROM mentor";

        Cursor cursor = appDatabase.rawQuery(query,null);

        cursor.moveToFirst();

        highestMentorId = cursor.getInt(0);
    }
    //add to constructor
    public void insertIntoDB(){
        appDatabase.execSQL(
                "INSERT INTO mentor(mentorId, mentorName, mentorPhone, mentorEmail) " +
                "VALUES(" +
                    this.mentorId + ", '" +
                    this.mentorName + "', '" +
                    this.mentorPhone + "', '" +
                    this.mentorEmail + "')"
        );
    }
    public void updateDB(){
        appDatabase.execSQL(
                "UPDATE mentor " +
                "SET " +
                    "mentorId = " + this.mentorId + ", " +
                    "mentorName = '" + this.mentorName + "', " +
                    "mentorPhone = '" + this.mentorPhone + "', " +
                    "mentorEmail = '" + (this.mentorEmail ) + "' " +
                "WHERE mentorId = " + this.mentorId
        );
    }
    public static void createFromDB() {

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM mentor", null);

        int mentorIdField = cursor.getColumnIndex("mentorId");
        int mentorNameField = cursor.getColumnIndex("mentorName");
        int mentorPhoneField = cursor.getColumnIndex("mentorPhone");
        int mentorEmailField = cursor.getColumnIndex("mentorEmail");

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                int mentorId = cursor.getInt(mentorIdField);
                String mentorName = cursor.getString(mentorNameField);
                String mentorPhone = cursor.getString(mentorPhoneField);
                String mentorEmail = cursor.getString(mentorEmailField);


                Mentor mentor = new Mentor(mentorId, mentorName, mentorPhone, mentorEmail);

            } while (cursor.moveToNext());
        }
    }

    /*Parcelable Methods*/
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

    }
    private Mentor(Parcel in) {
        mentorId = in.readInt();
        mentorName = in.readString();
        mentorPhone = in.readString();
        mentorEmail = in.readString();

    }

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

    @Override
    public String toString() {
        return this.getMentorName();
    }
}
