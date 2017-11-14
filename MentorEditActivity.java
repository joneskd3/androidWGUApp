package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class MentorEditActivity extends AppCompatActivity {

    public Mentor selectedMentor; //currently displayed Course object
    private final int REQUEST_CODE = 20; //used to dementorine result type
    private boolean newMentor;

    private TextView mentorNameField;
    private TextView mentorPhoneField;
    private TextView mentorEmailField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_edit);
        newMentor = getIntent().getBooleanExtra("New", false);

        populateFieldVariables();



        if (!newMentor) {
            selectedMentor = getIntent().getParcelableExtra("mentorObject");
            selectedMentor = Mentor.getAllMentorArray().get(selectedMentor.getMentorId());
            populateFields();
        }

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void updateMentor() {

        String mentorName = mentorNameField.getText().toString();
        String mentorPhone = mentorPhoneField.getText().toString();
        String mentorEmail = mentorEmailField.getText().toString();

        selectedMentor.setMentorName(mentorName);
        selectedMentor.setMentorPhone(mentorPhone);
        selectedMentor.setMentorEmail(mentorEmail);
    }

    public void populateFieldVariables(){
        mentorNameField = findViewById(R.id.text_name);
        mentorPhoneField = findViewById(R.id.text_phone);
        mentorEmailField = findViewById(R.id.text_email);
    }
    public void populateFields() {

        populateFieldVariables();

        /*Update Fields*/
        mentorNameField.setText(selectedMentor.getMentorName());
        mentorPhoneField.setText(selectedMentor.getMentorPhone());
        mentorEmailField.setText(selectedMentor.getMentorEmail());
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

                if (newMentor) {
                    selectedMentor = new Mentor();
                }
                updateMentor();//updates mentor info

                Intent data = new Intent();
                data.putExtra("mentorObject", selectedMentor);
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
