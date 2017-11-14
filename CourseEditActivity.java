package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class CourseEditActivity extends AppCompatActivity {

    private Course selectedCourse;

    private TextView courseTitleField;
    private TextView courseStartDateField;
    private TextView courseEndDateField;
    private Switch courseStartReminderField;
    private Switch courseEndReminderField;
    private Spinner courseStatusField;
    private LinearLayout mentorListField;
    private LinearLayout assessmentListField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit); //sets layout

        //retrieves Course object from calling activity
        selectedCourse = getIntent().getParcelableExtra("courseObject");
        selectedCourse = Course.allCourseMap.get(selectedCourse.getCourseId());




        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateFields(selectedCourse);
    }

    public void populateFieldVariables(){
        courseTitleField = findViewById(R.id.text_title);
        courseStartDateField = findViewById(R.id.text_phone);
        courseEndDateField = findViewById(R.id.text_email);
        courseStartReminderField = findViewById(R.id.img_start_reminder);
        courseEndReminderField = findViewById(R.id.switch_stop_reminder);
        courseStatusField = findViewById(R.id.text_type);
        mentorListField = findViewById(R.id.list_mentor);
        assessmentListField = findViewById(R.id.list_assessment);
    }
    public void populateFields(Course testCourse){

        populateFieldVariables();

        courseTitleField.setText(testCourse.getCourseName());
        courseStartDateField.setText(testCourse.getCourseStartDate());
        courseEndDateField.setText(testCourse.getCourseEndDate());
        courseStartReminderField.setChecked(testCourse.isCourseStartAlert());
        courseEndReminderField.setChecked(testCourse.isCourseEndAlert());

        populateStatusFields();
        populateMentorFields();
        populateAssessmentFields();
    }
    public void populateMentorFields(){

        for(Mentor mentor : Mentor.getAllMentorArray()){

            CheckBox mentorCheckboxField = new CheckBox(this);

            mentorCheckboxField.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            mentorCheckboxField.setText(mentor.getMentorName());

            for(Mentor courseMentor : selectedCourse.getCourseMentorArray()){
                if (mentor.getMentorId() == courseMentor.getMentorId()){
                    mentorCheckboxField.setChecked(true);
                }
            }

            mentorListField.addView(mentorCheckboxField);
        }
    }
    public void populateAssessmentFields(){


        for(Assessment assessment : Assessment.getAllAssessmentArray()){

            CheckBox assessmentCheckboxField = new CheckBox(this);

            assessmentCheckboxField.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            assessmentCheckboxField.setText(assessment.getAssessmentTitle());

            for(Assessment courseAssessment : selectedCourse.getCourseAssessmentArray()){
                if (assessment.getAssessmentId() == courseAssessment.getAssessmentId()){
                    assessmentCheckboxField.setChecked(true);
                }
            }

            assessmentListField.addView(assessmentCheckboxField);
        }
    }
    public void populateStatusFields(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        courseStatusField.setAdapter(adapter);

        int selectedStatus = 0;
        switch (selectedCourse.getCourseStatus()){
            case "In Progress":
                selectedStatus = 0;
                break;
            case "Dropped":
                selectedStatus = 1;
                break;
            case "Completed":
                selectedStatus = 2;
                break;
            case "Plan To Take":
                selectedStatus = 3;
                break;
        }
        courseStatusField.setSelection(selectedStatus);
    }

    public void updateCourse(){

        String courseTitle = courseTitleField.getText().toString();
        String courseStartDate = courseStartDateField.getText().toString();
        String courseEndDate = courseEndDateField.getText().toString();
        Boolean courseStartReminder = courseStartReminderField.isChecked();
        Boolean courseEndReminder = courseEndReminderField.isChecked();
        String courseStatus = courseStatusField.getSelectedItem().toString();

        selectedCourse.setCourseName(courseTitle);
        selectedCourse.setCourseStartDate(courseStartDate);
        selectedCourse.setCourseEndDate(courseEndDate);
        selectedCourse.setCourseStartAlert(courseStartReminder);
        selectedCourse.setCourseEndAlert(courseEndReminder);
        selectedCourse.setCourseStatus(courseStatus);

        updateCourseMentor();
        updateCourseAssessment();
    }
    public void updateCourseMentor(){
        selectedCourse.clearCourseMentorDB();

        for(int i = 0; i < mentorListField.getChildCount(); i++){
            CheckBox mentorChecked = (CheckBox) mentorListField.getChildAt(i);

            if (mentorChecked.isChecked()){
                selectedCourse.insertIntoDB(Mentor.allMentorMap.get(i));
            }
        }
    }
    public void updateCourseAssessment(){
        selectedCourse.getCourseAssessmentArray().clear();

        for(int i = 0; i < assessmentListField.getChildCount(); i++){
            CheckBox assessmentChecked = (CheckBox) assessmentListField.getChildAt(i);

            if (assessmentChecked.isChecked()){
                selectedCourse.insertIntoDB(Assessment.allAssessmentMap.get(i));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_save:

                updateCourse();//updates course info

                Intent data = new Intent();
                data.putExtra("courseObject", selectedCourse);
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent

                return true;
            case R.id.button_delete:

                //selectedCourse.deleteFromAllCourseArray(selectedCourse.getCourseId());

                Intent deleteIntent = new Intent(this, CourseListActivity.class);
                startActivity(deleteIntent);
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();

                return true;

            case android.R.id.home: //handles back button
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return true;

        }
    }
}
