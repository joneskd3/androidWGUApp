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
import android.widget.Toast;

public class MentorListActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20; //used to determine result type
    private Mentor selectedMentor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_list);


          /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateMentorList();
    }
    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first

        populateMentorList();
    }


    public void populateMentorList(){


        final ListView mentorList = findViewById(R.id.list_mentors);

        ArrayAdapter<Mentor> arrayAdapter = new ArrayAdapter<Mentor>(this, android.R.layout.simple_list_item_1, Mentor.getAllMentorArray());
        arrayAdapter.notifyDataSetChanged();
        mentorList.setAdapter(arrayAdapter);


        mentorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {

                Mentor selectedMentor = (Mentor) mentorList.getItemAtPosition(position);

                Intent intent = new Intent(MentorListActivity.this, MentorDetailActivity.class);
                intent.putExtra("mentorObject", selectedMentor);
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
                Intent intent = new Intent(this, MentorEditActivity.class);

                //Mentor newMentor = new Mentor();

                //intent.putExtra("mentorObject", newMentor);
                intent.putExtra("New", true); // pass arbitrary data to launched activity
                startActivityForResult(intent, REQUEST_CODE);
                return true;

            case android.R.id.home: //handles back button
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return true;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            populateMentorList();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
