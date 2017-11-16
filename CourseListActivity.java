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

public class CourseListActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20; //used to determine result type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateCourseList();
    }
    @Override
    public void onResume() {
        super.onResume();
        populateCourseList();
    }

    private void populateCourseList(){

        final ListView courseList = findViewById(R.id.list_courses);

        ArrayAdapter<Course> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Course.getAllCourseArray());
        arrayAdapter.notifyDataSetChanged();
        courseList.setAdapter(arrayAdapter);

        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {
                Course selectedCourse = (Course) courseList.getItemAtPosition(position);

                Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);
                intent.putExtra("courseObject", selectedCourse);
                intent.putExtra("mode", 2);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toolbar_add_button:
                Intent intent = new Intent(this, CourseEditActivity.class);

                //Course newCourse = new Course();
                intent.putExtra("new", true);
                intent.putExtra("mode", 2);
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
            populateCourseList();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
