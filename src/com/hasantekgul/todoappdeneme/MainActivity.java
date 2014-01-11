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
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static final String APP_PREFS = "app_preferences";
	private static final String IS_FIRST_TIME = "is_first_time";
	protected static final String FOLDER_NAME = "folder_name";
	
	private DrawerLayout sliderMenu;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	
	private String[] drawerTitles;
	private ArrayAdapter<String> adapter;
	
	DatabaseHelper db;	
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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

		db.close();
		/*
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
		*/
		drawerTitles = getResources().getStringArray(R.array.drawer_list_items);
		sliderMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.list_slidermenu);
//		
//		adapter = new ArrayAdapter<String>(getApplicationContext(),
//				android.R.layout.simple_list_item_1, drawerTitles);
//		drawerList.setAdapter(adapter);
		
		List<CustomList> drawerListItems = new ArrayList<CustomList>();
		for(String title : drawerTitles) {
			CustomList listItem = new CustomList(title, "");
			drawerListItems.add(listItem);
		}
		CustomListAdapter customAdapter = new CustomListAdapter(getApplicationContext(), drawerListItems);
		drawerList.setAdapter(customAdapter);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		drawerToggle = new ActionBarDrawerToggle(this,
				sliderMenu, R.drawable.ic_launcher, R.string.app_name,
				R.string.app_name) {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
				super.onDrawerSlide(drawerView, slideOffset);
				
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
			}
			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
			}
			
		};
		
		sliderMenu.setDrawerListener(drawerToggle);
		drawerList.setOnItemClickListener(new SlideMenuClickListener());
		
		if(savedInstanceState == null) {
			displayView(0);
		}
		
		
	}
	
	private class SlideMenuClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			displayView(position);
		}
		
	}
	
	public void displayView(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;

		default:
			break;
		}
		
		if (fragment != null) {
			FragmentManager manager = getFragmentManager();
			manager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			sliderMenu.closeDrawer(drawerList);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Toggles drawer
		if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		
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
