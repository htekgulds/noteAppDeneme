package com.hasantekgul.todoappdeneme;
import java.util.List;

import com.hasantekgul.todoappdeneme.R;
import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.Folder;
import com.hasantekgul.todoappdeneme.model.Note;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


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

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_custom_bg));
		
	}	

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_note, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			NavUtils.navigateUpFromSameTask(this);
			return true;
			
		case R.id.action_save:
			saveNote();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void saveNote() {
		Folder folder = db.getFolderByName("Genel");
		note  = new Note(text.getText().toString(), 0);
		db.createNote(note, null, new long[]{folder.getId()});
		
		this.finish();
	}
	
}
