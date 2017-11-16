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
import android.widget.Toast;

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
            populateTermCourses();
        } else {
            selectedTerm = getIntent().getParcelableExtra("termObject");
            selectedTerm = Term.allTermMap.get(selectedTerm.getTermId());
            populateFields();
        }

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }
    private void updateTerm(){

        String termTitle = termTitleField.getText().toString();
        String termStartDate = termStartDateField.getText().toString();
        String termEndDate = termEndDateField.getText().toString();

        selectedTerm.setTermName(termTitle);
        selectedTerm.setTermStart(termStartDate);
        selectedTerm.setTermEnd(termEndDate);

        updateTermCourse();
    }

    private void populateFieldVariables(){
        termTitleField = findViewById(R.id.term_edit_text_title);
        termStartDateField = findViewById(R.id.text_phone);
        termEndDateField = findViewById(R.id.text_email);
        termCourseListField = findViewById(R.id.term_edit_list_course);
    }
    private void populateFields() {

        /*Update Fields*/
        termTitleField.setText(selectedTerm.getTermName());
        termStartDateField.setText(selectedTerm.getTermStart());
        termEndDateField.setText(selectedTerm.getTermEnd());

        populateTermCourses();
    }
    private void populateTermCourses(){
        for(Course course : Course.getAllCourseArray())
        {
            CheckBox courseCheckboxField = new CheckBox(this);
            courseCheckboxField.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            courseCheckboxField.setText(course.toString());
            termCourseListField.addView(courseCheckboxField);

            if(!newTerm)
            {
                for (Course termCourse : selectedTerm.getTermCourseArray())
                {
                    if (course.getCourseId() == termCourse.getCourseId())
                    {
                        courseCheckboxField.setChecked(true);
                    }
                }
            }
        }
    }
    private void updateTermCourse(){

        selectedTerm.clearTermCourseDB();

        for(int i = 0; i < termCourseListField.getChildCount(); i++){
            CheckBox courseChecked = (CheckBox) termCourseListField.getChildAt(i);

            if (courseChecked.isChecked()){
                selectedTerm.insertIntoDB(Course.getAllCourseArray().get(i));
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

                if(newTerm){
                    selectedTerm = new Term();
                }
               updateTerm();//updates term info

                Intent data = new Intent();
                data.putExtra("termObject", selectedTerm);
                setResult(RESULT_OK, data);

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();

                return true;

            case R.id.button_delete:

                if (!validationTermHasCourses()){
                    selectedTerm.deleteFromDB();

                    Intent deleteData = new Intent(this, TermListActivity.class);

                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();

                    startActivity(deleteData);

                    return true;
                }

                return false;


            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;

            default:
                return true;
        }
    }
    private Boolean validationTermHasCourses(){
        for(int i = 0; i < termCourseListField.getChildCount(); i++){
            CheckBox courseChecked = (CheckBox) termCourseListField.getChildAt(i);
            if (courseChecked.isChecked()){
                Toast.makeText(this, "Cannot delete term with courses", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }
}
