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

public class NoteEditActivity extends AppCompatActivity {

    public Note selectedNote; //currently displayed Course object
    public Course selectedCourse;
    private final int REQUEST_CODE = 20; //used to denoteine result type
    private boolean newNote;

    private EditText noteTitleField;
    private EditText noteTextField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        newNote = getIntent().getBooleanExtra("New", false);

        populateFieldVariables();



        if (newNote) {
            selectedCourse = getIntent().getParcelableExtra("courseObject");
            selectedCourse = Course.allCourseMap.get(selectedCourse.getCourseId());
        } else {
            selectedNote = getIntent().getParcelableExtra("noteObject");
            selectedNote = Note.allNoteMap.get(selectedNote.getNoteId());
            populateFields();
        }

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);


    }

    public void updateNote() {

        String noteTitle = noteTitleField.getText().toString();
        String noteText = noteTextField.getText().toString();

        selectedNote.setNoteTitle(noteTitle);

        selectedNote.setNoteText(noteText);
        selectedNote.setCourseId(selectedCourse.getCourseId());

    }

    public void populateFieldVariables(){
        noteTitleField = findViewById(R.id.text_title);
        noteTextField = findViewById(R.id.text_email);
    }
    public void populateFields() {

        populateFieldVariables();

        /*Update Fields*/
        noteTitleField.setText(selectedNote.getNoteTitle());
        noteTextField.setText(selectedNote.getNoteText());
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

                if (newNote) {
                    selectedNote = new Note();
                    //selectedCourse.addToCourseNoteArray(selectedNote);
                }
                updateNote();//updates note info

                Intent data = new Intent();
                data.putExtra("noteObject", selectedNote);
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
