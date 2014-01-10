package com.hasantekgul.todoappdeneme;

import java.util.ArrayList;
import java.util.List;

import com.hasantekgul.todoappdeneme.MainActivity.CustomListAdapter;
import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.Folder;
import com.hasantekgul.todoappdeneme.model.Note;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class NotesActivity extends Activity {

	DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Show the Up button in the action bar.
		setupActionBar();

		db = new DatabaseHelper(getApplicationContext());
		Intent notesPage = getIntent();
		

		List<Note> notes = db.getAllNotesByFolder(notesPage.getStringExtra(MainActivity.FOLDER_NAME));
		for(Note note : notes) {
			Log.e("Notlar: ", note.getNote());
		}
		
		db.close();
		
		List<String> noteTexts = new ArrayList<String>();
		for(Note note : notes) {
			String noteText = note.getNote();
			noteTexts.add(noteText);
		}
		ListView list = (ListView) findViewById(R.id.list);
		CustomListAdapter adapter = new CustomListAdapter (getApplicationContext(), noteTexts);
		list.setAdapter(adapter);
		
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	
	public class CustomListAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final List<String> values;
		
		public CustomListAdapter(Context context, List<String> values) {
			super(context, R.layout.custom_list_row, values);
			this.context = context;
			this.values = values;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.custom_list_row, parent, false);
			TextView title = (TextView) rowView.findViewById(R.id.title);
			TextView text = (TextView) rowView.findViewById(R.id.text);
			
			title.setText("Not");
			text.setText(values.get(position));
			
			return rowView;
		}
		
	}

}
