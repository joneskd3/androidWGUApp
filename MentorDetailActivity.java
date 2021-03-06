package com.example.hello.kjschedule;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MentorDetailActivity extends AppCompatActivity {

    private Mentor selectedMentor; //currently displayed object

    private final int REQUEST_CODE = 20; //used to determine result type

    private TextView mentorNameField;
    private TextView mentorPhoneField;
    private TextView mentorEmailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);

        selectedMentor = getIntent().getParcelableExtra("mentorObject");
        selectedMentor = Mentor.allMentorMap.get(selectedMentor.getMentorId());

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateFields(); //updates fields with values
    }
    private void populateFieldVariables(){
        mentorNameField = findViewById(R.id.text_name);
        mentorPhoneField = findViewById(R.id.text_phone);
        mentorEmailField = findViewById(R.id.text_email);
    }
    private void populateFields() {

        populateFieldVariables();

        /*Update Fields*/
        mentorNameField.setText(selectedMentor.getMentorName());
        mentorPhoneField.setText(selectedMentor.getMentorPhone());
        mentorEmailField.setText(selectedMentor.getMentorEmail());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            selectedMentor = data.getParcelableExtra("mentorObject");
            populateFields();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_button:
                Intent intent = new Intent(this, MentorEditActivity.class);
                intent.putExtra("mentorObject", selectedMentor);
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