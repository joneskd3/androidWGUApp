package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class TermDetailView extends AppCompatActivity {

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
        selectedTerm = getIntent().getParcelableExtra("termObject");
        //editTerm = getIntent().getBooleanExtra("editCourse",false);


        /* Set up interface */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateFields();
    }

    public void populateFieldVariables(){
        termTitleField = (TextView) findViewById(R.id.term_text_title);
        termStartDateField = (TextView) findViewById(R.id.term_edit_text_start_date);
        termEndDateField = (TextView) findViewById(R.id.term_edit_text_end_date);
        termCourseListField = (ListView) findViewById(R.id.term_list_course);
    }
    public void populateFields() {

        populateFieldVariables();

        /*Update Fields*/
        termTitleField.setText(selectedTerm.getTermTitle());
        termStartDateField.setText(selectedTerm.getTermStart());
        termEndDateField.setText(selectedTerm.getTermEnd());

      //  populateCourseFields();
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
                Intent intent = new Intent(this, TermEditView.class);
                intent.putExtra("termObject", selectedTerm);
                intent.putExtra("mode", 2); // pass arbitrary data to launched activity
                intent.putExtra("editTerm",true);
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
