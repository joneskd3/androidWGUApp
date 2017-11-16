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

    private Boolean newCourse = false;

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
        newCourse = getIntent().getBooleanExtra("new",false);

        if(!newCourse) {
            selectedCourse = getIntent().getParcelableExtra("courseObject");
            selectedCourse = Course.allCourseMap.get(selectedCourse.getCourseId());
        }
        populateFields();

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void populateFieldVariables(){
        courseTitleField = findViewById(R.id.text_title);
        courseStartDateField = findViewById(R.id.text_phone);
        courseEndDateField = findViewById(R.id.text_email);
        courseStartReminderField = findViewById(R.id.img_reminder);
        courseEndReminderField = findViewById(R.id.switch_stop_reminder);
        courseStatusField = findViewById(R.id.text_type);
        mentorListField = findViewById(R.id.list_mentor);
        assessmentListField = findViewById(R.id.list_assessment);
    }
    private void populateFields(){

        populateFieldVariables();

        if(!newCourse) {

            courseTitleField.setText(selectedCourse.getCourseName());
            courseStartDateField.setText(selectedCourse.getCourseStartDate());
            courseEndDateField.setText(selectedCourse.getCourseEndDate());
            courseStartReminderField.setChecked(selectedCourse.isCourseStartAlert());
            courseEndReminderField.setChecked(selectedCourse.isCourseEndAlert());
        }
        populateStatusFields();
        populateMentorFields();
        populateAssessmentFields();
    }
    private void populateMentorFields(){

        for(Mentor mentor : Mentor.getAllMentorArray()){

            CheckBox mentorCheckboxField = new CheckBox(this);

            mentorCheckboxField.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            mentorCheckboxField.setText(mentor.getMentorName());

            if(!newCourse)
            {
                for (Mentor courseMentor : selectedCourse.getCourseMentorArray()) {
                    if (mentor.getMentorId() == courseMentor.getMentorId()) {
                        mentorCheckboxField.setChecked(true);
                    }
                }
            }
            mentorListField.addView(mentorCheckboxField);
        }
    }
    private void populateAssessmentFields(){

        for(Assessment assessment : Assessment.getAllAssessmentArray()){

            CheckBox assessmentCheckboxField = new CheckBox(this);

            assessmentCheckboxField.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            assessmentCheckboxField.setText(assessment.getAssessmentTitle());

            if(!newCourse) {
                for (Assessment courseAssessment : selectedCourse.getCourseAssessmentArray()) {
                    if (assessment.getAssessmentId() == courseAssessment.getAssessmentId()) {
                        assessmentCheckboxField.setChecked(true);
                    }
                }
            }
            assessmentListField.addView(assessmentCheckboxField);
        }
    }
    private void populateStatusFields(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatusField.setAdapter(adapter);

        if(!newCourse) {
            int selectedStatus = 0;
            switch (selectedCourse.getCourseStatus()) {
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
    }

    private void updateCourse(){

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
    private void updateCourseMentor(){
        selectedCourse.clearCourseMentorDB();

        for(int i = 0; i < mentorListField.getChildCount(); i++){
            CheckBox mentorChecked = (CheckBox) mentorListField.getChildAt(i);

            if (mentorChecked.isChecked()){
                selectedCourse.insertIntoDB(Mentor.getAllMentorArray().get(i));
            }
        }
    }
    private void updateCourseAssessment(){

        selectedCourse.clearCourseAssessmentDB();
        for(int i = 0; i < assessmentListField.getChildCount(); i++){
            CheckBox assessmentChecked = (CheckBox) assessmentListField.getChildAt(i);

            if (assessmentChecked.isChecked()){
                selectedCourse.insertIntoDB(Assessment.getAllAssessmentArray().get(i));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_save:

                if(newCourse){
                    selectedCourse = new Course();
                }
                updateCourse();//updates course info

                Intent data = new Intent();
                data.putExtra("courseObject", selectedCourse);
                setResult(RESULT_OK, data);
                finish();

                return true;
            case R.id.button_delete:

                selectedCourse.deleteFromDB();

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
