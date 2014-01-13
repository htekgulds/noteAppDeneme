package com.hasantekgul.todoappdeneme;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hasantekgul.todoappdeneme.adapter.CustomListAdapter;
import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.CustomList;
import com.hasantekgul.todoappdeneme.model.Folder;
import com.hasantekgul.todoappdeneme.model.Note;

public class MainActivity extends Activity {

	private static final String APP_PREFS = "app_preferences";
	private static final String IS_FIRST_TIME = "is_first_time";
	protected static final String FOLDER_NAME = "folder_name";

	DatabaseHelper db;	
	SharedPreferences pref;
	
	ListView list;
	Button button_back;
	EditText edittext_search;
	RelativeLayout search_overlay;
	
	private int animDuration = 400;
	private AnimatorListener overlayAnimListener;
	private boolean isVisible = false;
	
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_main);
		setupActionBar();

		db = new DatabaseHelper(getApplicationContext());		
		
		// Overlay Animation Listener
		overlayAnimListener = new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if (isVisible) {
					search_overlay.setVisibility(View.GONE);
					isVisible = false;
				}
				else {
					isVisible = true;
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		};
		
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

		// ListView init ve listener
		list = (ListView) findViewById(R.id.list);
		list.setAlpha(0f);
		showFolders();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,
					long id) {
				String folderName = ((CustomList)parent.getItemAtPosition(position)).getTitle();
				showNotesByFolder(folderName);
			}
			
		});
		
		// Back Button listener
		button_back = (Button) findViewById(R.id.button_back);
		button_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showFolders();
				button_back.setVisibility(View.GONE);
			}
		});
		
		// EditText touch event
		search_overlay = (RelativeLayout) findViewById(R.id.overlay_area);
		
		edittext_search = (EditText) findViewById(R.id.edittext_search);
		edittext_search.setFocusable(false);		
		edittext_search.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					openSearchOverlay();
				}
				return false;
			}
		});
		
		search_overlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeSearchOverlay();
			}
		});
	}

	private void setupActionBar() {

		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.action_bar_custom_bg));
	}
	
	public void showFolders() {
		new AsyncShowFolders().execute("");
	}
	
	private class AsyncShowFolders extends AsyncTask<String, Void, Void> {

		ListView list = (ListView) findViewById(R.id.list);
		@Override
		protected Void doInBackground(String... params) {

			List<CustomList> folderList = new ArrayList<CustomList>();
			List<Folder> folders = db.getAllFolders();
			
			for(Folder folder : folders) {
				int noteCount = db.getAllNotesByFolder(folder.getFolderName()).size();
				CustomList listItem = new CustomList(folder.getFolderName(),
						"Burada " + noteCount + " adet not var");
				folderList.add(listItem);
			}
			CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), folderList);
			list.setAdapter(adapter);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);			
			list.animate().alpha(1f).setDuration(animDuration);
		}
	}
	
	public void showNotesByFolder(String folderName) {
		new AsyncShowNotesByFolder().execute(folderName);
	}
	
	private class AsyncShowNotesByFolder extends AsyncTask<String, Void, Void> {

		ListView list = (ListView) findViewById(R.id.list);
		@Override
		protected Void doInBackground(String... params) {

			list.animate().alpha(0f).setDuration(animDuration);
			List<CustomList> folderList = new ArrayList<CustomList>();
			List<Note> notes = db.getAllNotesByFolder(params[0]);
			
			for(Note note : notes) {
				String date = note.getCreatedAt();
				CustomList listItem = new CustomList(note.getNote(),
						"Eklenme tarihi: " + date);
				folderList.add(listItem);
			}
			CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), folderList);
			list.setAdapter(adapter);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			list.animate().alpha(1f).setDuration(animDuration);
			button_back.setAlpha(0f);
			button_back.setVisibility(View.VISIBLE);
			button_back.animate().alpha(1f).setDuration(animDuration);
		}
		
	}
	
	public void openSearchOverlay() {
		EditText edit = (EditText) search_overlay.findViewById(R.id.edittext_search_top);
		edit.setText("");
		edit.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(getCurrentFocus(), 0);
		
		search_overlay.setAlpha(0f);
		search_overlay.setVisibility(View.VISIBLE);
		search_overlay.animate().alpha(1f).setDuration(animDuration)
			.setListener(overlayAnimListener);
	}
	
	public void closeSearchOverlay() {
		search_overlay.animate().alpha(0f).setDuration(animDuration);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edittext_search.getWindowToken(), 0);
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
