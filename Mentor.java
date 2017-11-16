package com.example.hello.kjschedule;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.hello.kjschedule.MainActivity.appDatabase;

@SuppressWarnings("unused")
public class Mentor implements Parcelable{

    //mapping from id to object
    @SuppressLint("UseSparseArrays")
    static HashMap<Integer,Mentor> allMentorMap = new HashMap<>();

    //instance variables
    private int mentorId;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmail;

    //highest id for new objects
    private static int highestMentorId = 0;

    //constructors
    public Mentor (){
        this("","","");
    }
    public Mentor (String mentorName, String mentorPhone, String mentorEmail){
        this.getHighestId();
        this.mentorId = highestMentorId;

        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;

        allMentorMap.put(this.mentorId,this);
        this.insertIntoDB();
    }
    private Mentor(int mentorId, String mentorName, String mentorPhone, String mentorEmail){
        this.mentorId = mentorId;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;

        allMentorMap.put(this.mentorId,this);
    }

    //setters and getters
    int getMentorId() {
        return mentorId;
    }
    String getMentorName() {
        return mentorName;
    }
    void setMentorName(String mentorName) {
        this.mentorName = mentorName;
        this.updateDB();
    }
    String getMentorPhone() {
        return mentorPhone;
    }
    void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
        this.updateDB();

    }
    String getMentorEmail() {
        return mentorEmail;
    }
    void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
        this.updateDB();

    }

    //array of all mentor objects
    static ArrayList<Mentor> getAllMentorArray() {
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
    private void getHighestId(){

        String query = "SELECT MAX(MentorId) FROM mentor";
        Cursor cursor = appDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        highestMentorId = cursor.getInt(0) + 1;
        cursor.close();
    }
    private void insertIntoDB(){
        appDatabase.execSQL(
                "INSERT INTO mentor(mentorId, mentorName, mentorPhone, mentorEmail) " +
                "VALUES(" +
                    this.mentorId + ", '" +
                    this.mentorName + "', '" +
                    this.mentorPhone + "', '" +
                    this.mentorEmail + "')"
        );
    }
    private void updateDB(){
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
    public void deleteFromDB(){
        appDatabase.execSQL("DELETE from Mentor WHERE mentorId = " + this.mentorId);
        appDatabase.execSQL("DELETE from courseMentor WHERE mentorId = " + this.mentorId);

    }
    static void createFromDB() {

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM mentor", null);

        int mentorIdField = cursor.getColumnIndex("mentorId");
        int mentorNameField = cursor.getColumnIndex("mentorName");
        int mentorPhoneField = cursor.getColumnIndex("mentorPhone");
        int mentorEmailField = cursor.getColumnIndex("mentorEmail");

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int mentorId = cursor.getInt(mentorIdField);
                String mentorName = cursor.getString(mentorNameField);
                String mentorPhone = cursor.getString(mentorPhoneField);
                String mentorEmail = cursor.getString(mentorEmailField);

                new Mentor(mentorId, mentorName, mentorPhone, mentorEmail);
            } while (cursor.moveToNext());
            cursor.close();
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
        @Override
        public Mentor createFromParcel(Parcel in) {
            return new Mentor(in);
        }

        @Override
        public Mentor[] newArray(int size) {
            return new Mentor[size];
        }
    };

    @Override
    public String toString() {
        return this.getMentorName() + "\nPhone: " + this.getMentorPhone() + "\nEmail: " + this.getMentorEmail();
    }
}
