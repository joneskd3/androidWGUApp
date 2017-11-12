package com.example.hello.kjschedule;

import android.content.Intent;
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
import android.widget.Toast;

public class AssessmentListActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20; //used to determine result type
    private Assessment selectedAssessment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);


          /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateAssessmentList();
    }
    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first

        populateAssessmentList();
    }


    public void populateAssessmentList(){


        final ListView assessmentList = findViewById(R.id.list_assessments);

        ArrayAdapter<Assessment> arrayAdapter = new ArrayAdapter<Assessment>(this, android.R.layout.simple_list_item_1, Assessment.getAllAssessmentArray());
        arrayAdapter.notifyDataSetChanged();
        assessmentList.setAdapter(arrayAdapter);


        assessmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {

                Assessment selectedAssessment = (Assessment) assessmentList.getItemAtPosition(position);

                Intent intent = new Intent(AssessmentListActivity.this, AssessmentDetailActivity.class);
                intent.putExtra("assessmentObject", selectedAssessment);
                intent.putExtra("mode", 2); // pass arbitrary data to launched activity
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toolbar_add_button:
                Intent intent = new Intent(this, AssessmentEditActivity.class);

                //Assessment newAssessment = new Assessment();

                //intent.putExtra("assessmentObject", newAssessment);
                intent.putExtra("New", true); // pass arbitrary data to launched activity
                startActivityForResult(intent, REQUEST_CODE);
                return true;

            case android.R.id.home: //handles back button
                onBackPressed();
                return true;

            default:
                return true;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            populateAssessmentList();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
