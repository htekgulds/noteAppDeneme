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
import com.hasantekgul.todoappdeneme.model.Note;

public class NotesFragment extends Fragment {
	
	private DatabaseHelper db;
	
	public NotesFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_custom_list, container, false);
		db = new DatabaseHelper(getActivity());
		
		List<Note> notes = db.getAllNotesByFolder(getArguments().getString(MainActivity.FOLDER_NAME));
		final List<CustomList> noteList = new ArrayList<CustomList>();
		
		for(Note note : notes) {
			CustomList listItem = new CustomList(note.getNote(),
					"Eklenme tarihi: " + note.getCreatedAt());
			noteList.add(listItem);
		}
		
		db.close();
		
		ListView list = (ListView) rootView.findViewById(R.id.list); 
		CustomListAdapter adapter = new CustomListAdapter(getActivity(), noteList);
		list.setAdapter(adapter);		
		
		return rootView;
	}

}
