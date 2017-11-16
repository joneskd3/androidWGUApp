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
import android.widget.Toast;

public class NoteEditActivity extends AppCompatActivity {

    private Note selectedNote; //currently displayed object
    private Course selectedCourse;
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
            selectedCourse = Course.allCourseMap.get(selectedNote.getCourseId());
            populateFields();
        }

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void updateNote() {

        String noteTitle = noteTitleField.getText().toString();
        String noteText = noteTextField.getText().toString();

        selectedNote.setNoteTitle(noteTitle);
        selectedNote.setNoteText(noteText);
        if (newNote){
            selectedNote.setCourseId(selectedCourse.getCourseId());
        }
    }

    private void populateFieldVariables(){
        noteTitleField = findViewById(R.id.text_title);
        noteTextField = findViewById(R.id.text_email);
    }
    private void populateFields() {

        populateFieldVariables();

        /*Update Fields*/
        noteTitleField.setText(selectedNote.getNoteTitle());
        noteTextField.setText(selectedNote.getNoteText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_save:

                if (newNote) {
                    selectedNote = new Note();
                }
                updateNote();//updates note info

                Intent data = new Intent();
                data.putExtra("noteObject", selectedNote);
                setResult(RESULT_OK, data);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();

                return true;

            case R.id.button_delete:

                selectedNote.deleteFromDB();

                Intent deleteData = new Intent(this, NoteListActivity.class);
                deleteData.putExtra("courseObject",selectedCourse);
                setResult(RESULT_OK, deleteData);
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                startActivityForResult(deleteData, 20);

                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return true;

        }
    }
}
