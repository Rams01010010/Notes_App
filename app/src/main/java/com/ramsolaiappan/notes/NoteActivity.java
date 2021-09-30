package com.ramsolaiappan.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.nio.charset.StandardCharsets;

public class NoteActivity extends AppCompatActivity {

    EditText titleET,descET;
    int positionReceived;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent receivedIntent = getIntent();
        positionReceived = receivedIntent.getIntExtra("position",-1);
        String titleReceived = receivedIntent.getStringExtra("title");
        String descReceived = receivedIntent.getStringExtra("description");

        titleET = (EditText) findViewById(R.id.titleEditText);
        descET = (EditText) findViewById(R.id.descriptionEditText);

        if(titleReceived != null && descReceived != null)
        {
            titleET.setText(titleReceived);
            descET.setText(descReceived);
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        String titleToSend = "",descToSend = "";
        if(titleET.getText().toString().equals("") && descET.getText().toString().equals(""))
        {
            setResult(RESULT_CANCELED,returnIntent);
            finish();
        }
        if(titleET.getText().toString().equals(""))
            titleToSend = "Untitled";
        else
            titleToSend = titleET.getText().toString();
        if(descET.getText().toString().equals(""))
            descToSend = "No Description";
        else
            descToSend = descET.getText().toString();
        returnIntent.putExtra("position",positionReceived);
        returnIntent.putExtra("title",titleToSend);
        returnIntent.putExtra("description",descToSend);
        setResult(RESULT_OK,returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.saveoptions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.cancel: finish(); break;
            case R.id.save: onBackPressed(); break;
        }
        return super.onOptionsItemSelected(item);
    }
}