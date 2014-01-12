package com.hasantekgul.todoappdeneme;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.CustomList;
import com.hasantekgul.todoappdeneme.model.Folder;

public class MainActivity extends FragmentActivity {

	private static final String APP_PREFS = "app_preferences";
	private static final String IS_FIRST_TIME = "is_first_time";
	protected static final String FOLDER_NAME = "folder_name";

	DatabaseHelper db;	
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_main);
		setupActionBar();		

		db = new DatabaseHelper(getApplicationContext());
		
		// Check preference 
		pref = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
		if (pref.getBoolean(IS_FIRST_TIME, true)) {
			Folder defaultFolder = new Folder("Genel");
			db.createFolder(defaultFolder);
			Log.e("Yeni Klasör: ", defaultFolder.getFolderName());
			
			// first_time = false yapýlarak ilk giriþ onaylanýr.
			Editor editor = pref.edit();
			editor.putBoolean(IS_FIRST_TIME, false);
			editor.commit();
		}

		Fragment foldersFragment = new FoldersFragment();
		getSupportFragmentManager().beginTransaction()
			.add(R.id.frame_container, foldersFragment).commit();
		
		
		
		/*
		list.setOnItemClickListener(new OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  
		    	  Fragment notesFragment = new FoldersFragment();
		    	  Bundle args = new Bundle();
		    	  args.putString(FOLDER_NAME,
		    			  ((CustomList)parent.getItemAtPosition(position)).getTitle());
		    	  
		    	  getSupportFragmentManager().beginTransaction()
		    	  	.replace(R.layout.activity_custom_list, notesFragment).commit();
		      }
		});

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
		});*/
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		ListView list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,
					long id) {
				Fragment notesFragment = new NotesFragment();
				Bundle args = new Bundle();
				args.putString(FOLDER_NAME, ((CustomList)parent
						.getItemAtPosition(position)).getTitle());
				notesFragment.setArguments(args);
				FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
				
				trans.replace(R.id.frame_container, notesFragment);
				trans.addToBackStack(null);
				trans.commit();
				
			}
		});
	}

	private void setupActionBar() {

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.action_bar_custom_bg));
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
