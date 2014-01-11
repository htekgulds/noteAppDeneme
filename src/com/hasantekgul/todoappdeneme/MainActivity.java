package com.hasantekgul.todoappdeneme;

import java.util.ArrayList;
import java.util.List;

import com.hasantekgul.todoappdeneme.adapter.CustomListAdapter;
import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.CustomList;
import com.hasantekgul.todoappdeneme.model.Folder;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static final String APP_PREFS = "app_preferences";
	private static final String IS_FIRST_TIME = "is_first_time";
	protected static final String FOLDER_NAME = "folder_name";
	
	DatabaseHelper db;	
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_list);
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_custom_bg));
		
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
		final List<CustomList> folderList = new ArrayList<CustomList>();
		
		for(Folder folder : folders) {
			CustomList listItem = new CustomList("Klasör", folder.getFolderName());
			folderList.add(listItem);
		}
		
		db.close();
		
		ListView list = (ListView) findViewById(R.id.list);
		CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), folderList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  
		    	  Intent notesPage = new Intent(getApplicationContext(), NotesActivity.class);
		    	  notesPage.putExtra(FOLDER_NAME, folderList.get(position).getText());
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

}
