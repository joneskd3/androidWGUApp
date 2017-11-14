package com.example.hello.kjschedule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static SQLiteDatabase appDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        reminder();





        createDatabase();




        createMentorTable();
        Mentor.createFromDB();

        createAssessmentTable();
        Assessment.createFromDB();

        createTermTable();
        Term.createFromDB();

        createCourseAssessmentTable();
        createCourseTable();

        createCourseMentorTable();
        createTermTable();

        createTermCourseTable();

        //createTestData();

        Course.createCourseFromDB();
        //Course.createCourseMentorFromDB();

        createNoteTable();
        Note.createFromDB();

    }

    public void reminder(){
        AlertDialog alerts = new AlertDialog.Builder(this).create();

        alerts.setTitle("Reminder");
        alerts.setMessage("Upcoming Exams");
        alerts.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alerts.setButton(AlertDialog.BUTTON_NEGATIVE, "Disable Reminder",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alerts.show();
    }
    public void handleRemindersButton(View view){
        AlertDialog alerts = new AlertDialog.Builder(this).create();

        alerts.setTitle("Reminder");
        alerts.setMessage("Upcoming Exams");
        alerts.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alerts.setButton(AlertDialog.BUTTON_NEGATIVE, "Disable Reminder",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alerts.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.add, menu);
        return true;
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

       /* Course testCourse1 = new Course("C183", "12/12/2018", true,
                "12/30/2018", true, "In Process", null,
                null, null);

        Course testCourse2 = new Course("C893", "12/12/2018", true,
                "12/30/2018", true, "In Process", null,
                null, null);
        Course testCourse3 = new Course("C778", "12/12/2018", true,
                "12/30/2018", true, "In Process", null,
                null, null);
        */
        /*
        Mentor testMentor1 = new Mentor("Joe","333-333-9993","joe@wgu.edu");
        Mentor testMentor2 = new Mentor("Sue","333-333-9993","Sue@wgu.edu");
        Mentor testMentor3 = new Mentor("Tom","333-333-9993","Tom@wgu.edu");
        Mentor testMentor4 = new Mentor("Barb","333-333-9993","Barb@wgu.edu");
        */
        Assessment testAssessment1 = new Assessment("Performance","Assess C183","12/1/2017",true);
        Assessment testAssessment2 = new Assessment("Objective","Perform 388","12/30/2017",true);
        Assessment testAssessment3 = new Assessment("Performance","Objective II","12/1/2017",true);
        /*
        Term testTerm1 = new Term("Term 1","12/1/2016","12/2/2016");
        Term testTerm2 = new Term("Term 2","12/1/2017","12/2/2017");
        Term testTerm3 = new Term("Term 3","12/1/2018","12/2/2018");
        */
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //appDatabase.delete("Course",null,null);
    }


}
