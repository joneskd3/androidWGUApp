package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AssessmentEditActivity extends AppCompatActivity {

    public Assessment selectedAssessment; //currently displayed Course object
    private final int REQUEST_CODE = 20; //used to deassessmentine result type
    private boolean newAssessment;

    private EditText assessmentTitleField;
    private EditText assessmentStartField;
    private Spinner assessmentTypeField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_edit);
        newAssessment = getIntent().getBooleanExtra("New", false);

        populateFieldVariables();



        if (newAssessment) {
            populateTypeField();
        } else {
            selectedAssessment = getIntent().getParcelableExtra("assessmentObject");
            selectedAssessment = Assessment.getAllAssessmentArray().get(selectedAssessment.getAssessmentId());
            populateFields();
        }

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);


    }

    public void updateAssessment() {

        String assessmentTitle = assessmentTitleField.getText().toString();
        String assessmentDueDate = assessmentStartField.getText().toString();
        String assessmentType = assessmentTypeField.getSelectedItem().toString();

        selectedAssessment.setAssessmentTitle(assessmentTitle);
        selectedAssessment.setAssessmentDueDate(assessmentDueDate);
        selectedAssessment.setAssessmentType(assessmentType);

    }

    public void populateFieldVariables() {
        assessmentTitleField = findViewById(R.id.text_title);
        assessmentStartField = findViewById(R.id.text_phone);
        assessmentTypeField = findViewById(R.id.text_type);
    }

    public void populateFields() {

        /*Update Fields*/
        assessmentTitleField.setText(selectedAssessment.getAssessmentTitle());
        assessmentStartField.setText(selectedAssessment.getAssessmentDueDate());

        populateTypeField();
    }

    public void populateTypeField() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        assessmentTypeField.setAdapter(adapter);

        if (!newAssessment) {
            int selectedStatus = 0;
            switch (selectedAssessment.getAssessmentType()) {
                case "Objective":
                    selectedStatus = 0;
                    break;
                case "Performance":
                    selectedStatus = 1;
                    break;
                default:
                    break;
            }
            assessmentTypeField.setSelection(selectedStatus);
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

                if (newAssessment) {
                    selectedAssessment = new Assessment();
                }
                updateAssessment();//updates assessment info

                Intent data = new Intent();
                data.putExtra("assessmentObject", selectedAssessment);
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent

                return true;

            case android.R.id.home: //handles back button
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return true;

        }
    }
}
