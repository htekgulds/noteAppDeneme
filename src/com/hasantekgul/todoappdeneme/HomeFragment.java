package com.hasantekgul.todoappdeneme;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hasantekgul.todoappdeneme.adapter.CustomListAdapter;
import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.CustomList;
import com.hasantekgul.todoappdeneme.model.Folder;

public class HomeFragment extends Fragment {
	
	DatabaseHelper db;
	
	public HomeFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		

		List<Folder> folders = db.getAllFolders();
		final List<CustomList> folderList = new ArrayList<CustomList>();
		
		for(Folder folder : folders) {
			CustomList listItem = new CustomList("Klasör", folder.getFolderName());
			folderList.add(listItem);
		}
		
		db.close();
		
		ListView list = (ListView) findViewById(R.id.list); 
		CustomListAdapter adapter = new CustomListAdapter(getActivity(), folderList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  
		    	  Intent notesPage = new Intent(getActivity(), NotesActivity.class);
		    	  notesPage.putExtra(FOLDER_NAME, folderList.get(position).getText());
		    	  startActivity(notesPage);
		      }
		});
		
		return rootView;
	}

}
