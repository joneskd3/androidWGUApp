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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TermEditActivity extends AppCompatActivity {

    private Term selectedTerm;
    private Boolean newTerm = false;

    private TextView termTitleField;
    private TextView termStartDateField;
    private TextView termEndDateField;
    private LinearLayout termCourseListField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);

        newTerm = getIntent().getBooleanExtra("New",false);

        populateFieldVariables();

        if (newTerm){
            populateNewTermCourses();
        } else {
            selectedTerm = getIntent().getParcelableExtra("termObject");
            selectedTerm = Term.allTermMap.get(selectedTerm.getTermId());
            populateFields();
        }

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);


    }
    public void updateTerm(){

        String termTitle = termTitleField.getText().toString();
        String termStartDate = termStartDateField.getText().toString();
        String termEndDate = termEndDateField.getText().toString();

        selectedTerm.setTermName(termTitle);
        selectedTerm.setTermStart(termStartDate);
        selectedTerm.setTermEnd(termEndDate);

        updateTermCourse();

    }

    public void populateFieldVariables(){
        termTitleField = findViewById(R.id.term_edit_text_title);
        termStartDateField = findViewById(R.id.text_phone);
        termEndDateField = findViewById(R.id.text_email);
        termCourseListField = findViewById(R.id.term_edit_list_course);
    }
    public void populateFields() {


        /*Update Fields*/
        termTitleField.setText(selectedTerm.getTermName());
        termStartDateField.setText(selectedTerm.getTermStart());
        termEndDateField.setText(selectedTerm.getTermEnd());

        populateTermCourses();
    }
    public void populateTermCourses(){
        for(Course course : Course.getAllCourseArray()){

            CheckBox courseCheckboxField = new CheckBox(this);

            courseCheckboxField.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            courseCheckboxField.setText(course.toString());

            for(Course termCourse : selectedTerm.getTermCourseArray()){
                if (course.getCourseId() == termCourse.getCourseId()){
                    courseCheckboxField.setChecked(true);
                }
            }

            termCourseListField.addView(courseCheckboxField);
        }
    }
    public void populateNewTermCourses(){
        for(Course course : Course.getAllCourseArray()){

            CheckBox courseCheckboxField = new CheckBox(this);

            courseCheckboxField.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            courseCheckboxField.setText(course.toString());

            termCourseListField.addView(courseCheckboxField);
        }
    }
    public void updateTermCourse(){
        selectedTerm.getTermCourseArray().clear(); //

        for(int i = 0; i < termCourseListField.getChildCount(); i++){
            CheckBox courseChecked = (CheckBox) termCourseListField.getChildAt(i);

            if (courseChecked.isChecked()){
                selectedTerm.addToTermCourseArray(Course.getAllCourseArray().get(i));
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

                if(newTerm){
                    selectedTerm = new Term();
                }
               updateTerm();//updates term info

                Intent data = new Intent();
                data.putExtra("termObject", selectedTerm);
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent

                return true;

            case R.id.button_delete:

                selectedTerm.deleteFromDB();

                Intent deleteData = new Intent(this, TermListActivity.class);
                //data.putExtra("termObject", selectedTerm);
                //setResult(RESULT_OK, data); // set result code and bundle data for response
                startActivity(deleteData);
                //finish(); // closes the activity, pass data to parent

                return true;

            case android.R.id.home: //handles back button
                NavUtils.navigateUpFromSameTask(this);

                return true;

            default:
                return true;

        }
    }

}
