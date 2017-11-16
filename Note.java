package com.example.hello.kjschedule;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

import static com.example.hello.kjschedule.MainActivity.appDatabase;

@SuppressWarnings("unused")
public class Note implements Parcelable{

    //mapping
    @SuppressLint("UseSparseArrays")
    static HashMap<Integer, Note> allNoteMap = new HashMap<>();

    //instance variables
    private int noteId;
    private String noteTitle;
    private String noteText;
    private int courseId;

    private static int highestNoteId = 0;

    //constructors
    Note (){
        this("","",0);
    }
    private Note(String noteTitle, String noteText, int courseId){
        this.getHighestId();
        this.noteId = highestNoteId;

        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.courseId = courseId;

        allNoteMap.put(this.noteId,this);
        this.insertIntoDB();
    }
    private Note(int noteId, String noteTitle, String noteText, int courseId){
        this.noteId = noteId;

        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.courseId = courseId;

        allNoteMap.put(this.noteId,this);
    }

    //setters and getters
    String getNoteTitle() {
        return noteTitle;
    }
    void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
        this.updateDB();
    }
    int getNoteId() {
        return noteId;
    }
    String getNoteText() {
        return noteText;
    }
    void setNoteText(String noteText) {
        this.noteText = noteText;
        this.updateDB();
    }
    int getCourseId() {
        return courseId;
    }
    void setCourseId(int courseId) {
        this.courseId = courseId;
        this.updateDB();
    }

    //Database Methods
    private void getHighestId(){

        String query = "SELECT MAX(noteId) FROM note";
        Cursor cursor = appDatabase.rawQuery(query,null);
        cursor.moveToFirst();
        highestNoteId = cursor.getInt(0) + 1;
        cursor.close();
    }
    private void insertIntoDB(){
        appDatabase.execSQL(
                "INSERT INTO note(noteId, noteTitle, noteText, courseId) " +
                        "VALUES(" +
                        this.noteId + ", '" +
                        this.noteTitle + "', '" +
                        this.noteText + "', " +
                        this.courseId + ")"
        );
    }
    private void updateDB(){
        appDatabase.execSQL(
                "UPDATE note " +
                        "SET " +
                        "noteId = " + this.noteId + ", " +
                        "noteTitle = '" + this.noteTitle + "', " +
                        "noteText = '" + this.noteText + "', " +
                        "courseId = " + this.courseId + " " +
                        "WHERE noteId = " + this.noteId
        );
    }
    public void deleteFromDB(){
        appDatabase.execSQL("DELETE from note WHERE noteId = " + this.noteId);
    }
    static void createFromDB() {

        Cursor cursor = appDatabase.rawQuery("SELECT * FROM note", null);

        int noteIdField = cursor.getColumnIndex("noteId");
        int noteTitleField = cursor.getColumnIndex("noteTitle");
        int noteTextField = cursor.getColumnIndex("noteText");
        int courseIdField = cursor.getColumnIndex("courseId");

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                int noteId = cursor.getInt(noteIdField);
                String noteTitle = cursor.getString(noteTitleField);
                String noteText = cursor.getString(noteTextField);
                int courseId = cursor.getInt(courseIdField);

                new Note(noteId, noteTitle, noteText, courseId);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    //parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(noteId);
        parcel.writeString(noteTitle);
        parcel.writeString(noteText);
    }
    private Note(Parcel in) {
        noteId = in.readInt();
        noteTitle = in.readString();
        noteText = in.readString();
    }
    public static final Parcelable.Creator<Note> CREATOR
            = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };



    @Override
    public String toString() {
        return this.getNoteTitle();
    }
}
