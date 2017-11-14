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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TermDetailActivity extends AppCompatActivity {

    public Term selectedTerm; //currently displayed Course object
    private final int REQUEST_CODE = 20; //used to determine result type


    private TextView termTitleField;
    private TextView termStartDateField;
    private TextView termEndDateField;
    private ListView termCourseListField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);


        //retrieves Course object from calling activity
        if(getIntent().getParcelableExtra("termObject") != null){
            selectedTerm = getIntent().getParcelableExtra("termObject");
            selectedTerm = Term.allTermMap.get(selectedTerm.getTermId());

        }
        //editTerm = getIntent().getBooleanExtra("editCourse",false);


        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateFields();
    }

    public void populateFieldVariables(){
        termTitleField = findViewById(R.id.term_text_title);
        termStartDateField = findViewById(R.id.text_phone);
        termEndDateField = findViewById(R.id.text_email);
        termCourseListField = findViewById(R.id.term_list_course);
    }
    public void populateFields() {

        populateFieldVariables();

        /*Update Fields*/
        termTitleField.setText(selectedTerm.getTermName());
        termStartDateField.setText(selectedTerm.getTermStart());
        termEndDateField.setText(selectedTerm.getTermEnd());

        populateTermCourseFields();
    }
    public void populateTermCourseFields(){


        if(selectedTerm.getTermCourseArray().size() > 0) {
            ArrayAdapter<Course> arrayAdapter = new ArrayAdapter<Course>(TermDetailActivity.this,
                    android.R.layout.simple_list_item_1, selectedTerm.getTermCourseArray());

            termCourseListField.setAdapter(arrayAdapter);

            termCourseListField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {

                    Course selectedCourse = (Course) termCourseListField.getItemAtPosition(position);

                    Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
                    intent.putExtra("courseObject", selectedCourse);
                    intent.putExtra("mode", 2); // pass arbitrary data to launched activity
                    startActivity(intent);
                }
            });

        } else {
            ArrayList<String> emptyArray = new ArrayList<>(); //formats mentor array list for display
            emptyArray.add("No Courses Assigned"); //If no mentors assigned

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TermDetailActivity.this,
                    android.R.layout.simple_list_item_1, emptyArray);

            termCourseListField.setAdapter(arrayAdapter);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            selectedTerm = data.getParcelableExtra("termObject");
            populateFields();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first

        populateFields();
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
                Intent intent = new Intent(this, TermEditActivity.class);
                intent.putExtra("termObject", selectedTerm);
                intent.putExtra("mode", 2); // pass arbitrary data to launched activity
                intent.putExtra("editTerm",true);
                startActivityForResult(intent, REQUEST_CODE);
                return true;

            case android.R.id.home: //handles back button
                NavUtils.navigateUpFromSameTask(this);

                return true;

            default:
                return true;
        }
    }



}
