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

import java.util.ArrayList;

public class TermListActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20; //used to determine result type


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);


          /* Set up interface */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);

        populateTermList();
    }

    public void populateTermList(){


        final ListView termList = (ListView) findViewById(R.id.list_terms);
        ArrayAdapter<Term> arrayAdapter = new ArrayAdapter<Term>(this, android.R.layout.simple_list_item_1, Term.getAllTermArray());
        termList.setAdapter(arrayAdapter);


        termList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {

                Term selectedTerm = (Term) termList.getItemAtPosition(position);
                Toast.makeText(TermListActivity.this, "Position: " + position + "\nTerm: " + selectedTerm, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TermListActivity.this, TermDetailView.class);
                intent.putExtra("termObject", selectedTerm);
                intent.putExtra("mode", 2); // pass arbitrary data to launched activity
                //intent.putExtra("editTerm",true);
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
                Intent intent = new Intent(this, TermEditView.class);

                Term newTerm = new Term();

                intent.putExtra("termObject", newTerm);
                intent.putExtra("mode", 2); // pass arbitrary data to launched activity
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
            //selectedTerm = data.getParcelableExtra("courseObject");
            populateTermList();
            Toast.makeText(this, "Term Added", Toast.LENGTH_SHORT).show();
        }
    }
}

