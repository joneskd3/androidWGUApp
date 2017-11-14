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
import android.widget.Toast;

public class NoteDetailActivity extends AppCompatActivity {

    public Note selectedNote; //currently displayed Course object

    private final int REQUEST_CODE = 20; //used to determine result type

    private TextView noteTitleField;
    private TextView noteTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        selectedNote = getIntent().getParcelableExtra("noteObject");
        selectedNote = Note.allNoteMap.get(selectedNote.getNoteId());

        /* Set up interface */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateFields(); //updates fields with values
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            selectedNote = data.getParcelableExtra("noteObject");
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
                Intent intent = new Intent(this, NoteEditActivity.class);
                intent.putExtra("noteObject", selectedNote);
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