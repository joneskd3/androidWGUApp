package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CourseDetailActivity extends AppCompatActivity {

    public Course selectedCourse; //currently displayed Course object

    private final int REQUEST_CODE = 20; //used to determine result type

    private TextView courseTitleField;
    private TextView courseStartDateField;
    private TextView courseEndDateField;
    private ImageView courseStartReminder;
    private ImageView courseEndReminder;
    private TextView courseStatus;
    private ListView mentorList;
    private ListView assessmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        selectedCourse = getIntent().getParcelableExtra("courseObject");
        selectedCourse = Course.allCourseMap.get(selectedCourse.getCourseId());

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateFields(selectedCourse); //updates fields with values
    }
    /*Updates list of mentors */
    public void populateMentor(){

        if(selectedCourse.getCourseMentorArray().size() > 0) {
            ArrayAdapter<Mentor> arrayAdapter = new ArrayAdapter<Mentor>(CourseDetailActivity.this,
                    android.R.layout.simple_list_item_1, selectedCourse.getCourseMentorArray());

            mentorList.setAdapter(arrayAdapter);

            mentorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {

                    Mentor selectedMentor = (Mentor) mentorList.getItemAtPosition(position);

                    Intent intent = new Intent(CourseDetailActivity.this, MentorDetailActivity.class);
                    intent.putExtra("mentorObject", selectedMentor);
                    intent.putExtra("mode", 2); // pass arbitrary data to launched activity
                    startActivity(intent);
                }
            });

        } else {
            ArrayList<String> emptyArray = new ArrayList<>(); //formats mentor array list for display
            emptyArray.add("No Mentors Assigned"); //If no mentors assigned

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CourseDetailActivity.this,
                    android.R.layout.simple_list_item_1, emptyArray);

            mentorList.setAdapter(arrayAdapter);
        }
    }
    /*Updates list of assessments */
    public void populateAssessment() {


        if (selectedCourse.getCourseAssessmentArray().size() > 0) {
            ArrayAdapter<Assessment> arrayAdapter = new ArrayAdapter<Assessment>(CourseDetailActivity.this,
                    android.R.layout.simple_list_item_1, selectedCourse.getCourseAssessmentArray());

            assessmentList.setAdapter(arrayAdapter);

            assessmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {

                    Assessment selectedAssessment = (Assessment) assessmentList.getItemAtPosition(position);

                    Intent intent = new Intent(CourseDetailActivity.this, AssessmentDetailActivity.class);
                    intent.putExtra("assessmentObject", selectedAssessment);
                    intent.putExtra("mode", 2); // pass arbitrary data to launched activity
                    startActivity(intent);
                }
            });


        } else {
            ArrayList<String> emptyArray = new ArrayList<>(); //formats mentor array list for display
            emptyArray.add("No Assessments Assigned"); //If no mentors assigned

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CourseDetailActivity.this,
                    android.R.layout.simple_list_item_1, emptyArray);

            assessmentList.setAdapter(arrayAdapter);
        }
    }
    /*Updates all other fields */
    public void populateFields(Course testCourse) {

        populateFieldVariables();

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

    public void populateFieldVariables(){
            /*Field Variables*/
        courseTitleField = findViewById(R.id.text_title);
        courseStartDateField = findViewById(R.id.text_phone);
        courseEndDateField = findViewById(R.id.text_email);
        courseStartReminder = findViewById(R.id.img_start_reminder);
        courseEndReminder = findViewById(R.id.img_end_reminder);
        courseStatus = findViewById(R.id.text_type);
        mentorList = findViewById(R.id.list_mentor);
        assessmentList = findViewById(R.id.list_assessment);


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
        getMenuInflater().inflate(R.menu.course, menu);
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

            case R.id.note_button:
                Intent noteIntent = new Intent(this, NoteListActivity.class);
                noteIntent.putExtra("courseObject", selectedCourse);
                noteIntent.putExtra("mode", 2); // pass arbitrary data to launched activity
                noteIntent.putExtra("newNote",true);
                startActivityForResult(noteIntent, REQUEST_CODE);
                return true;

            case android.R.id.home: //handles back button
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return true;
        }
    }

}