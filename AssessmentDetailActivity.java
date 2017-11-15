package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AssessmentDetailActivity extends AppCompatActivity {

    public Assessment selectedAssessment; //currently displayed Course object

    private final int REQUEST_CODE = 20; //used to determine result type

    private TextView assessmentTitleField;
    private TextView assessmentStartField;
    private TextView assessmentTypeField;
    private ImageView assessmentReminderField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        selectedAssessment = getIntent().getParcelableExtra("assessmentObject");
        selectedAssessment = Assessment.allAssessmentMap.get(selectedAssessment.getAssessmentId());

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateFields(); //updates fields with values
    }
    public void populateFieldVariables(){
        assessmentTitleField = findViewById(R.id.text_title);
        assessmentStartField = findViewById(R.id.text_phone);
        assessmentTypeField = findViewById(R.id.text_type);
        assessmentReminderField = findViewById(R.id.img_reminder);
    }
    public void populateFields() {

        populateFieldVariables();

        /*Update Fields*/
        assessmentTitleField.setText(selectedAssessment.getAssessmentTitle());
        assessmentStartField.setText(selectedAssessment.getAssessmentDueDate());
        assessmentTypeField.setText(selectedAssessment.getAssessmentType());

        if(selectedAssessment.isAssessmentReminder()){
            assessmentReminderField.setImageResource(R.mipmap.ic_notifications_active_black_24dp);
        } else {
            assessmentReminderField.setImageResource(R.mipmap.ic_notifications_off_black_24dp);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            selectedAssessment = data.getParcelableExtra("assessmentObject");
            populateFields();
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
                Intent intent = new Intent(this, AssessmentEditActivity.class);
                intent.putExtra("assessmentObject", selectedAssessment);
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