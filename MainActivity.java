package com.example.hello.kjschedule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    boolean reminderOnStart;
    Button reminderButton;
    public static SQLiteDatabase appDatabase = null;

    StringBuilder upcomingCourseStart;
    StringBuilder upcomingCourseEnd;
    StringBuilder upcomingAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //create sharedPreference to hold reminder on start preferences
        sharedPref = this.getSharedPreferences("PREFERENCE_FILE_KEY",Context.MODE_PRIVATE);
        reminderOnStart = sharedPref.getBoolean("reminderOnStart", true);  // getting boolean
        reminderButton = findViewById(R.id.button_reminder);

        //updates text to show state of reminder button
        if (reminderOnStart){
            reminderButton.setText(R.string.main_reminders_enabled);
        } else {
            reminderButton.setText(R.string.main_reminders_disabled);
        }

        /*SQL Database */
        createDatabase();

        //Create Tables
        createMentorTable();
        createAssessmentTable();
        createCourseTable();
        createTermTable();
        createCourseAssessmentTable();
        createCourseMentorTable();
        createTermCourseTable();
        createNoteTable();

        //Populate Tables
        Mentor.createFromDB();
        Assessment.createFromDB();
        Course.createFromDB();
        Term.createFromDB();
        Note.createFromDB();

        //Create Test Data
        createTestData();

        //Only show reminder alert if preference allows
        if(reminderOnStart) {
            reminder();
        }
    }

    public void reminder(){
        upcomingCourseStart = new StringBuilder();
        upcomingCourseEnd = new StringBuilder();
        upcomingAssessment = new StringBuilder();

        //Course Reminders
        for(Course course : Course.getAllCourseArray()){
            if  (course.isCourseStartAlert()){
                upcomingCourseStart. append("-" + course.getCourseName() + " (" + course.getCourseStartDate() + ")\n");
            }
            if (course.isCourseEndAlert()){
                upcomingCourseEnd.append("-" + course.getCourseName() + " (" + course.getCourseEndDate() + ")\n");
            }
        }
        //Assessment Reminders
        for(Assessment assessment : Assessment.getAllAssessmentArray()){
            if (assessment.isAssessmentReminder()){
                upcomingAssessment.append("-" + assessment.getAssessmentTitle() + " (" + assessment.getAssessmentDueDate() + ")\n");
            }
        }
        //Create Alert
        AlertDialog alerts = new AlertDialog.Builder(this).create();
        alerts.setTitle("Reminders");
        alerts.setMessage(
                "Upcoming Course Start:\n" + upcomingCourseStart +
                "\nUpcoming Course End:\n" + upcomingCourseEnd +
                "\nUpcoming Assessments:\n" + upcomingAssessment
        );

        //Create Buttons
        alerts.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if(reminderOnStart) {
            alerts.setButton(AlertDialog.BUTTON_NEGATIVE, "Disable Reminder On Start",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            editor.putBoolean("reminderOnStart", false); //updates shared preference if disable selected
                            editor.commit(); // commit changes
                            reminderOnStart = sharedPref.getBoolean("reminderOnStart", true);
                            reminderButton.setText(R.string.main_reminders_disabled); //updates button text

                            dialog.dismiss();
                        }
                    });
        } else {
            alerts.setButton(AlertDialog.BUTTON_POSITIVE, "Enable Reminder On Start",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean("reminderOnStart", true); //updates shared preference
                            editor.commit(); // commit changes
                            reminderOnStart = sharedPref.getBoolean("reminderOnStart", true);
                            reminderButton.setText(R.string.main_reminders_enabled); //updates button text

                            dialog.dismiss();
                        }
                    });
        }

        alerts.show();
    }

    /*Button handlers*/
    public void handleRemindersButton(View view){
       reminder();
    }
    public void handleTermsButton(View view){
        Intent intent = new Intent(this, TermListActivity.class);
        startActivity(intent);
    }
    public void handleCoursesButton(View view){
        Intent intent = new Intent(this, CourseListActivity.class);
        startActivity(intent);
    }
    public void handleAssessmentsButton(View view){
        Intent intent = new Intent(this, AssessmentListActivity.class);
        startActivity(intent);
    }
    public void handleMentorsButton(View view){
        Intent intent = new Intent(this, MentorListActivity.class);
        startActivity(intent);
    }

    /*Creates test data */
    public void createTestData() {

        if(Course.getAllCourseArray().size() == 0) {
            new Course("Test Course I", "12/01/2017",true,
                    "12/30/2017", true, "In Process");
        }

        if(Mentor.getAllMentorArray().size() == 0){
            new Mentor("Joe Test", "333-333-9928","joetest@wgu.edu");
        }

        if(Assessment.getAllAssessmentArray().size() == 0){
            new Assessment("Performance","Test Assessment I",
                    "12/15/2017",true);
        }

        if(Term.getAllTermArray().size() == 0){
            new Term("Term I", "12/01/2017","05/01/2018");
        }
    }

    /*Database Methods*/
    private void createDatabase() {
        try {
            appDatabase = openOrCreateDatabase("schedule.db", Context.MODE_PRIVATE, null);
        } catch (Exception e) {
            Log.e("DB ERROR", "Database Creation Error");
        }
    }
    public void createCourseTable() {
        appDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS course " +
                "(courseId INTEGER PRIMARY KEY, " +
                "courseName VARCHAR, " +
                "courseStartDate VARCHAR, " +
                "courseStartAlert INTEGER, " +
                "courseEndDate VARCHAR, " +
                "courseEndAlert INTEGER, " +
                "courseStatus VARCHAR);"
        );
    }
    public void createCourseMentorTable() {
        appDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS courseMentor " +
                "(courseId INTEGER, " +
                "mentorId INTEGER " +
                ");"
        );
    }
    public void createAssessmentTable(){
        appDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS assessment " +
                "(assessmentId INTEGER PRIMARY KEY, " +
                "assessmentType VARCHAR, " +
                "assessmentDescription VARCHAR, " +
                "assessmentDueDate VARCHAR, " +
                "assessmentReminder INTEGER)"
        );
    }
    public void createCourseAssessmentTable() {
        appDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS courseAssessment " +
                "(courseId INTEGER, " +
                "assessmentId INTEGER);"
        );
    }
    public void createMentorTable() {
        appDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS mentor " +
                "(mentorId INTEGER PRIMARY KEY, " +
                "mentorName VARCHAR, " +
                "mentorPhone VARCHAR, " +
                "mentorEmail VARCHAR);"
        );
    }
    public void createTermTable() {
        appDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS term " +
                "(termId INTEGER PRIMARY KEY, " +
                "termName VARCHAR, " +
                "termStart VARCHAR, " +
                "termEnd VARCHAR);"
        );
    }
    public void createTermCourseTable() {
        appDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS termCourse " +
                "(termId INTEGER, " +
                "courseId INTEGER, " +
                "PRIMARY KEY (termId, courseId));"
        );
    }
    public void createNoteTable() {
        appDatabase.execSQL("" +
                "CREATE TABLE IF NOT EXISTS note " +
                "(noteId INTEGER PRIMARY KEY, " +
                "noteTitle VARCHAR, " +
                "noteText VARCHAR, " +
                "courseId INTEGER);"
        );
    }
}
