package com.example.hello.kjschedule;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by owner on 11/9/2017.
 */


public class Note implements Parcelable{
    private int noteId;
    private String noteTitle;
    private String noteText;

    private static int highestNoteId = 0;

    private static ArrayList<Note> allNoteArray = new ArrayList<>();

    public Note (){
        this("","");
    }

    public Note (String noteTitle, String noteText){
        this.noteId = highestNoteId;
        highestNoteId++;

        this.noteTitle = noteTitle;
        this.noteText = noteText;

        addToNoteArrayList(this);
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public static ArrayList<Note> getAllNoteArray() {
        return allNoteArray;
    }

    public void addToNoteArrayList(Note note) {
        allNoteArray.add(note);
    }

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

        // We just need to copy this and change the type to match our class.
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
