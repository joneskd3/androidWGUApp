package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        createTestData();
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

        Course testCourse1 = new Course("C183", "12/12/2018", true,
                "12/30/2018", true, "In Process", null,
                null, null);
        Course testCourse2 = new Course("C893", "12/12/2018", true,
                "12/30/2018", true, "In Process", null,
                null, null);
        Course testCourse3 = new Course("C778", "12/12/2018", true,
                "12/30/2018", true, "In Process", null,
                null, null);

        Mentor testMentor1 = new Mentor("Joe","333-333-9993","joe@wgu.edu");
        Mentor testMentor2 = new Mentor("Sue","333-333-9993","Sue@wgu.edu");
        Mentor testMentor3 = new Mentor("Tom","333-333-9993","Tom@wgu.edu");
        Mentor testMentor4 = new Mentor("Barb","333-333-9993","Barb@wgu.edu");

        Assessment testAssessment1 = new Assessment("Performance","Assess C183","12/1/2017",true);
        Assessment testAssessment2 = new Assessment("Objective","Perform 388","12/30/2017",true);
        Assessment testAssessment3 = new Assessment("Performance","Objective II","12/1/2017",true);

        Term testTerm1 = new Term("Term 1","12/1/2016","12/2/2016",null);
        Term testTerm2 = new Term("Term 2","12/1/2017","12/2/2017",null);
        Term testTerm3 = new Term("Term 3","12/1/2018","12/2/2018",null);
    }
}
