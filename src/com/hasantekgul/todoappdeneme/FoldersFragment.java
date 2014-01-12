package com.hasantekgul.todoappdeneme;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hasantekgul.todoappdeneme.adapter.CustomListAdapter;
import com.hasantekgul.todoappdeneme.helper.DatabaseHelper;
import com.hasantekgul.todoappdeneme.model.CustomList;
import com.hasantekgul.todoappdeneme.model.Folder;

public class FoldersFragment extends Fragment {
	
	private DatabaseHelper db;
	ListView list;
	
	public FoldersFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_custom_list, container, false);
		db = new DatabaseHelper(getActivity());
		
		if (db != null) {

			List<Folder> folders = db.getAllFolders();
			final List<CustomList> folderList = new ArrayList<CustomList>();
			
			for(Folder folder : folders) {
				int noteCount = db.getAllNotesByFolder(folder.getFolderName()).size();
				CustomList listItem = new CustomList(folder.getFolderName(),
						"Burada " + String.valueOf(noteCount) + " not var");
				folderList.add(listItem);
			}
			
			db.close();
			
			list = (ListView) rootView.findViewById(R.id.list);
			CustomListAdapter adapter = new CustomListAdapter(getActivity(), folderList);
			list.setAdapter(adapter);
			
		}
		
		
		return rootView;
	}


}
