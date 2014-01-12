package com.hasantekgul.todoappdeneme;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hasantekgul.todoappdeneme.adapter.CustomListAdapter;
import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.CustomList;
import com.hasantekgul.todoappdeneme.model.Note;

public class NotesActivity extends Activity {

	DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_list);
		// Show the Up button in the action bar.
		setupActionBar();

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_custom_bg));
		
		makeList();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		makeList();
	}
	
	public void makeList() {
		db = new DatabaseHelper(getApplicationContext());
		Intent notesPage = getIntent();
		
		List<Note> notes = db.getAllNotesByFolder(notesPage.getStringExtra(FoldersFragment.FOLDER_NAME));
		List<CustomList> noteList = new ArrayList<CustomList>();
		
		for(Note note : notes) {
			CustomList listItem = new CustomList("Not", note.getNote());
			noteList.add(listItem);
		}
		
		db.close();
		
		ListView list = (ListView) findViewById(R.id.list);
		CustomListAdapter adapter = new CustomListAdapter (getApplicationContext(), noteList);
		list.setAdapter(adapter);
		
	}

	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
			
		case R.id.action_add_note:
			Intent intentAddNote = new Intent(getApplicationContext(), AddNoteActivity.class);
			startActivity(intentAddNote);
			break;
		}
		return super.onOptionsItemSelected(item);
	}


}
