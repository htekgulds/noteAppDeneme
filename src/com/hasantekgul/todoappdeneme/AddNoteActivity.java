package com.hasantekgul.todoappdeneme;
import java.util.List;

import com.hasantekgul.todoappdeneme.R;
import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.Folder;
import com.hasantekgul.todoappdeneme.model.Note;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddNoteActivity extends Activity {
	
	Note note;
	EditText text;
	DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_add_note);
		setupActionBar();
		text = (EditText) findViewById(R.id.editText1);
		db = new DatabaseHelper(getApplicationContext());
	}	

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void saveNote(View view) {
		Folder folder = db.getFolderByName("Genel");
		note  = new Note(text.getText().toString(), 0);
		db.createNote(note, null, new long[]{folder.getId()});
		
		List<Folder> allFolders = db.getAllFolders();
		
		for(Folder f : allFolders) {
			Log.e("Klasör: ", f.getFolderName());
		}
		
		List<Note> allNotes = db.getAllNotes();
		for(Note n : allNotes) {
			Log.e("Notlar: ", n.getNote());
		}
		
		this.finish();
	}
	
}
