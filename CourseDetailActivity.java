package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CourseDetailActivity extends AppCompatActivity {

    public Course selectedCourse; //currently displayed Course object


    private final int REQUEST_CODE = 20; //used to determine result type

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        selectedCourse = getIntent().getParcelableExtra("courseObject");
        selectedCourse = Course.getAllCourseArray().get(selectedCourse.getCourseId());

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateFields(selectedCourse); //updates fields with values
    }
    /*Updates list of mentors */
    public void populateMentor(){

        ArrayList<String> formattedMentorArray = new ArrayList<>(); //formats mentor array list for display

        if(selectedCourse.getCourseMentorArray().size() > 0) {
            for (Mentor mentor : selectedCourse.getCourseMentorArray()) {
                formattedMentorArray.add(mentor.getMentorName());
            }
        } else {
            formattedMentorArray.add("No Mentor Assigned"); //If no mentors assigned
        }

        ListView mentorList = findViewById(R.id.list_mentor);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, formattedMentorArray);
        mentorList.setAdapter(arrayAdapter);
    }
    /*Updates list of assessments */
    public void populateAssessment(){

        ArrayList<String> formattedAssessmentArray = new ArrayList<>();

        if (selectedCourse.getCourseAssessmentArray().size() > 0) {
            for (Assessment assessment : selectedCourse.getCourseAssessmentArray()) {
                formattedAssessmentArray.add(assessment.getAssessmentTitle());
            }
        } else {
            formattedAssessmentArray.add("No Assessment Assigned");
        }

        ListView assessmentList = findViewById(R.id.list_assessment);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, formattedAssessmentArray);
        assessmentList.setAdapter(arrayAdapter);
    }
    /*Updates all other fields */
    public void populateFields(Course testCourse) {

        /*Field Variables*/
        TextView courseTitleField = findViewById(R.id.text_title);
        TextView courseStartDateField = findViewById(R.id.text_phone);
        TextView courseEndDateField = findViewById(R.id.text_email);
        ImageView courseStartReminder = findViewById(R.id.img_start_reminder);
        ImageView courseEndReminder = findViewById(R.id.img_end_reminder);
        TextView courseStatus = findViewById(R.id.text_type);

        /*Update Fields*/
        courseTitleField.setText(testCourse.getCourseName());
        courseStartDateField.setText(testCourse.getCourseStartDate());
        courseEndDateField.setText(testCourse.getCourseEndDate());

        if(testCourse.isCourseStartAlert()){
            courseStartReminder.setImageResource(R.mipmap.ic_notifications_active_black_24dp);
        } else {
            courseStartReminder.setImageResource(R.mipmap.ic_notifications_off_black_24dp);
        }

        if(testCourse.isCourseEndAlert()){
            courseEndReminder.setImageResource(R.mipmap.ic_notifications_active_black_24dp);
        } else {
            courseEndReminder.setImageResource(R.mipmap.ic_notifications_off_black_24dp);
        }

        courseStatus.setText(testCourse.getCourseStatus());

        populateMentor();
        populateAssessment();
    }



    /*Returns result after launching edit activity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            selectedCourse = data.getParcelableExtra("courseObject");
            populateFields(selectedCourse);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_button:
                Intent intent = new Intent(this, CourseEditActivity.class);
                intent.putExtra("courseObject", selectedCourse);
                intent.putExtra("mode", 2); // pass arbitrary data to launched activity
                intent.putExtra("editCourse",true);
                startActivityForResult(intent, REQUEST_CODE);
                return true;

            case android.R.id.home: //handles back button
                onBackPressed();
                return true;

            default:
                return true;
        }
    }

}