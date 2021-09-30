package com.ramsolaiappan.notes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView notesListView;
    LinearLayout layout;
    EditText searchET;
    ImageButton imageButton;
    ArrayList<NotesList> notesList = new ArrayList<NotesList>();
    NotesListAdapter notesListAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesListView = (ListView) findViewById(R.id.listview);
        sharedPreferences = getSharedPreferences("com.ramsolaiappan.notes", MODE_PRIVATE);
        layout = (LinearLayout) findViewById(R.id.layout);
        searchET = (EditText) findViewById(R.id.searchET);
        imageButton = (ImageButton) findViewById(R.id.imageBtn);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchET.setText("");
                layout.setVisibility(View.GONE);
            }
        });

        ArrayList<String> notes = new ArrayList<String>(Arrays.asList(sharedPreferences.getString("notes","<~-b-~>").split("<~-b-~>")));
        Log.i("Activity", notes.toString());
        for(String note : notes)
        {
            ArrayList<String> noteItems = new ArrayList<String>(Arrays.asList(note.split("<~-s-~>")));
            notesList.add(new NotesList(noteItems.get(0),noteItems.get(1)));
        }
        updateNotesList();

        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openNote(notesList.get(position),position);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setTitle("Warning!").setMessage("Are you sure ? You want to delete this note ?").setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notesList.remove(position);
                        updateNotesList();
                    }
                }).show();
                return true;
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<NotesList> filteredList = new ArrayList<NotesList>();
                for(NotesList note : notesList)
                {
                    if(note.getTitle().toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        filteredList.add(note);
                    }
                }
                NotesListAdapter adapter = new NotesListAdapter(MainActivity.this,R.layout.adapter_view_noteslist,filteredList);
                notesListView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void updateNotesList()
    {
        if(!notesList.isEmpty())
        {
            notesListView.setVisibility(View.VISIBLE);
            notesListAdapter = new NotesListAdapter(MainActivity.this,R.layout.adapter_view_noteslist,notesList);
            notesListView.setAdapter(notesListAdapter);

            String strForSavingData = "";
            for(NotesList obj : notesList)
            {
                strForSavingData += obj.getTitle() + "<~-s-~>" + obj.getDescription() + "<~-b-~>";
            }
            Log.i("Activity",strForSavingData);
            sharedPreferences.edit().putString("notes",strForSavingData).apply();
        }
        else
        {
            notesListView.setVisibility(View.GONE);
            sharedPreferences.edit().remove("notes").apply();
        }
    }

    public void addNote()
    {
        Intent noteIntent = new Intent(MainActivity.this,NoteActivity.class);
        noteActivityLauncher.launch(noteIntent);
    }

    public void openNote(NotesList obj,int position)
    {
        Intent openNoteIntent = new Intent(MainActivity.this,NoteActivity.class);
        openNoteIntent.putExtra("position",position);
        openNoteIntent.putExtra("title",obj.getTitle());
        openNoteIntent.putExtra("description",obj.getDescription());
        noteActivityLauncher.launch(openNoteIntent);
    }

    ActivityResultLauncher<Intent> noteActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if(data.getIntExtra("position",-1) != -1)
                        {
                            notesList.set(data.getIntExtra("position",-1),new NotesList(data.getStringExtra("title"),data.getStringExtra("description")));
                        }
                        else
                            notesList.add(new NotesList(data.getStringExtra("title"),data.getStringExtra("description")));
                        updateNotesList();
                    }
                }
            }
    );

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.addNote: addNote(); break;

            case R.id.delete: deleteAll(); break;

            case R.id.search: layout.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteAll()
    {
        new AlertDialog.Builder(this).setTitle("Warning!").setMessage("Are you sure ? You want to delete all ?").setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notesList.clear();
                updateNotesList();
            }
        }).show();
    }
}