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

public class NoteListActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    private static Course selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        selectedCourse = getIntent().getParcelableExtra("courseObject");
        selectedCourse = Course.allCourseMap.get(selectedCourse.getCourseId());

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateNoteList();
    }
    @Override
    public void onResume() {
        super.onResume();
        populateNoteList();
    }

    private void populateNoteList(){

        final ListView noteList = findViewById(R.id.list_notes);

        ArrayAdapter<Note> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedCourse.getCourseNoteArray());
        arrayAdapter.notifyDataSetChanged();
        noteList.setAdapter(arrayAdapter);

        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {

                Note selectedNote = (Note) noteList.getItemAtPosition(position);

                Intent intent = new Intent(NoteListActivity.this, NoteDetailActivity.class);
                intent.putExtra("noteObject", selectedNote);
                intent.putExtra("CourseObject",selectedCourse);
                intent.putExtra("mode", 2); // pass arbitrary data to launched activity
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
                Intent intent = new Intent(this, NoteEditActivity.class);

                intent.putExtra("New", true);
                intent.putExtra("courseObject", selectedCourse);
                startActivityForResult(intent, REQUEST_CODE);
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return true;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            selectedCourse = getIntent().getParcelableExtra("courseObject");
            selectedCourse = Course.allCourseMap.get(selectedCourse.getCourseId());
            populateNoteList();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
