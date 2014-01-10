package com.hasantekgul.todoappdeneme;

import java.util.ArrayList;
import java.util.List;

import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.Folder;
import com.hasantekgul.todoappdeneme.model.Note;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String IS_FIRST_TIME = "is_first_time";
	private static final String APP_PREFS = "app_preferences";
	protected static final String FOLDER_NAME = "folder_name";
	
	DatabaseHelper db;	
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new DatabaseHelper(getApplicationContext());
		
		// Check preference 
		pref = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
		if (pref.getBoolean(IS_FIRST_TIME, true)) {
			Folder defaultFolder = new Folder("Genel");
			db.createFolder(defaultFolder);
			Log.e("Yeni Klasör: ", defaultFolder.getFolderName());
			
			// first_time = true yapýlarak ilk giriþ onaylanýr.
			Editor editor = pref.edit();
			editor.putBoolean(IS_FIRST_TIME, false);
			editor.commit();
		}
		
		List<Folder> folders = db.getAllFolders();
		for(Folder folder : folders) {
			Log.e("Klasörler: ", folder.getFolderName());
		}		

		List<Note> notes = db.getAllNotes();
		for(Note note : notes) {
			Log.e("Notlar: ", note.getNote());
		}
		
		db.close();
		
		final List<String> folderNames = new ArrayList<String>();
		for(Folder folder : folders) {
			String folderName = folder.getFolderName();
			folderNames.add(folderName);
		}
		ListView list = (ListView) findViewById(R.id.list);
		CustomListAdapter adapter = new CustomListAdapter (getApplicationContext(), folderNames);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  
		    	  Intent notesPage = new Intent(getApplicationContext(), NotesActivity.class);
		    	  notesPage.putExtra(FOLDER_NAME, (String)parent.getItemAtPosition(position));
		    	  startActivity(notesPage);
		      }
		});
		
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
		case R.id.action_add_note:
			Intent intentAddNote = new Intent(getApplicationContext(), AddNoteActivity.class);
			startActivity(intentAddNote);
			break;

		default:
			break;
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
			
			title.setText("Klasör");
			text.setText(values.get(position));
			
			return rowView;
		}
		
	}

}
