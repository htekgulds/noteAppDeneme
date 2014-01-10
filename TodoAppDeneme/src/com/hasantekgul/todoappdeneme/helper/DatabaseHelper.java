package com.hasantekgul.todoappdeneme.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hasantekgul.todoappdeneme.model.Folder;
import com.hasantekgul.todoappdeneme.model.Note;
import com.hasantekgul.todoappdeneme.model.Tag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String LOG = "DatabaseHelper";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "notesManager";
	
	// TABLES
	private static final String TABLE_NOTE = "notes";
	private static final String TABLE_TAG = "tags";
	private static final String TABLE_FOLDER = "folders";
	private static final String TABLE_NOTE_TAGS = "note_tags";
	private static final String TABLE_NOTE_FOLDERS = "note_folders";
	
	// COMMOM COLUMNS
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";
	
	// NOTES
	private static final String KEY_NOTE = "note";
	private static final String KEY_STATUS = "status";
	
	// TAGS
	private static final String KEY_TAG_NAME = "tag_name";
	
	// FOLDERS
	private static final String KEY_FOLDER_NAME = "folder_name";
	
	// NOTE_TAGS
	private static final String KEY_NOTE_ID = "note_id";
	private static final String KEY_TAG_ID = "tag_id";
	
	// NOTE FOLDERS
	private static final String KEY_FOLDER_ID = "folder_id";
	
	
	// TABLE CREATE STATEMENTS
	
	// NOTES
	private static final String CREATE_TABLE_NOTES = "create table "
			+ TABLE_NOTE + " (" + KEY_ID + " integer primary key, "
			+ KEY_NOTE + " text, " + KEY_STATUS + " integer, "
			+ KEY_CREATED_AT + " datetime)";
	
	// TAGS
	private static final String CREATE_TABLE_TAGS = "create table "
			+ TABLE_TAG + " (" + KEY_ID + " integer primary key, "
			+ KEY_TAG_NAME + " text, " + KEY_CREATED_AT
			+ " datetime)";
	
	// FOLDERS
	private static final String CREATE_TABLE_FOLDERS = "create table "
			+ TABLE_FOLDER + " (" + KEY_ID + " integer primary key, "
			+ KEY_FOLDER_NAME + " text, " + KEY_CREATED_AT
			+ " datetime)";
	
	// NOTE_TAGS
	private static final String CREATE_TABLE_NOTE_TAGS = "create table "
			+ TABLE_NOTE_TAGS + " (" + KEY_ID + " integer primary key, "
			+ KEY_NOTE_ID + " integer, " + KEY_TAG_ID + " integer, "
			+ KEY_CREATED_AT + " datetime)";
	
	// NOTE_FOLDERS
	private static final String CREATE_TABLE_NOTE_FOLDERS = "create table "
			+ TABLE_NOTE_FOLDERS + " (" + KEY_ID + " integer primary key,"
			+ KEY_NOTE_ID + " integer, " + KEY_FOLDER_ID + " integer, "
			+ KEY_CREATED_AT + " datetime)";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// Creating tables
		db.execSQL(CREATE_TABLE_NOTES);
		db.execSQL(CREATE_TABLE_TAGS);
		db.execSQL(CREATE_TABLE_FOLDERS);
		db.execSQL(CREATE_TABLE_NOTE_TAGS);
		db.execSQL(CREATE_TABLE_NOTE_FOLDERS);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// Dropping old tables
		db.execSQL("drop table if exists " + TABLE_NOTE);
		db.execSQL("drop table if exists " + TABLE_TAG);
		db.execSQL("drop table if exists " + TABLE_FOLDER);
		db.execSQL("drop table if exists " + TABLE_NOTE_TAGS);
		db.execSQL("drop table if exists " + TABLE_NOTE_FOLDERS);

		// Creating new tables
		onCreate(db);
	}
	
	// CRUD METHODS
	
	// NOTES
	
	// Create note
	public long createNote(Note note, long[] tag_ids, long[] folder_ids) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NOTE, note.getNote());
		values.put(KEY_STATUS, note.getStatus());
		values.put(KEY_CREATED_AT, getDateTime());
		
		// insert row
		long note_id = db.insert(TABLE_NOTE, null, values);
		
		// assigning tags
		if (tag_ids !=null) {
			for(long tag_id : tag_ids) {
				createNoteTag(note_id, tag_id);
			}
			
		}
		// assigning folders
		if (folder_ids != null) {
			for(long folder_id : folder_ids) {
				createNoteFolder(note_id, folder_id);
			}
		}
		
		return note_id;
	}
	
	// Get single note
	public Note getNote(long note_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String seletQuery = "select * from " + TABLE_NOTE
				+ "where " + KEY_ID + " = " + note_id;
		
		Log.e(LOG, seletQuery);
		
		Cursor c = db.rawQuery(seletQuery, null);
		if (c != null) {
			c.moveToFirst();
		}
		Note note = new Note();
		note.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		note.setNote(c.getString(c.getColumnIndex(KEY_NOTE)));
		note.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
		
		return note;
	}
	
	// Get all notes
	public List<Note> getAllNotes() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<Note> notes = new ArrayList<Note>();
		
		String selectQuery = "select * from " + TABLE_NOTE;
		
		Log.e(LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				Note note = new Note();
				note.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				note.setNote(c.getString(c.getColumnIndex(KEY_NOTE)));
				note.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				
				notes.add(note);
			} while (c.moveToNext());
		}
		
		return notes;
	}
	
	// Get all notes under tag
	public List<Note> getAllNotesByTag(String tagName) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<Note> notes = new ArrayList<Note>();
		
		String selectQuery = "select * from " + TABLE_NOTE + " nt, "
				+ TABLE_TAG + " tg, " + TABLE_NOTE_TAGS + " ntg where tg."
				+ KEY_TAG_NAME + " = '" + tagName + "' and tg." + KEY_ID
				+ " = ntg." + KEY_TAG_ID + " and ntg." + KEY_NOTE_ID
				+ " = nt." + KEY_ID;
		
		Log.e(LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				Note note = new Note();
				note.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				note.setNote(c.getString(c.getColumnIndex(KEY_NOTE)));
				note.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				
				notes.add(note);				
			} while (c.moveToNext());
		}
		
		return notes;
	}
	
	// Get all notes under folder
	public List<Note> getAllNotesByFolder(String folderName) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<Note> notes = new ArrayList<Note>();
		
		String selectQuery = "select * from " + TABLE_NOTE + " nt, "
				+ TABLE_FOLDER + " tf, " + TABLE_NOTE_FOLDERS + " ntf where tf."
				+ KEY_FOLDER_NAME + " = '" + folderName + "' and tf." + KEY_ID
				+ " = ntf." + KEY_FOLDER_ID + " and ntf." + KEY_NOTE_ID
				+ " = nt." + KEY_ID;
		
		Log.e(LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				Note note = new Note();
				note.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				note.setNote(c.getString(c.getColumnIndex(KEY_NOTE)));
				note.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				
				notes.add(note);
			} while (c.moveToNext());
		}
		
		return notes;
	}
	
	// Update note
	public int updateNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_NOTE, note.getNote());
		values.put(KEY_STATUS, note.getStatus());
			
		return db.update(TABLE_NOTE, values, KEY_ID + "=?",
				new String[] {String.valueOf(note.getId())});
	}
	
	// Delete note
	public void deleteNote(long note_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTE, KEY_ID + "=?",
				new String[] {String.valueOf(note_id)});
	}
	
	
	// TAGS
	
	// Create tag
	public long createTag(String tagName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_TAG_NAME, tagName);
		values.put(KEY_CREATED_AT, getDateTime());
		
		long tag_id = db.insert(TABLE_TAG, null, values);
		
		return tag_id;
	}
	
	// Get tag by name
	public Tag getTagByName(String tagName) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "select * from " + TABLE_TAG
				+ " where " + KEY_TAG_NAME + " = '" + tagName + "'";
		
		Log.e(LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null) {
			c.moveToFirst();
		}
		Tag tag = new Tag();
		tag.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		tag.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));
		tag.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
		
		return tag;
	}
	
	// Get tags
	public List<Tag> getAllTags() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<Tag> tags = new ArrayList<Tag>();
		
		String selectQuery = "select * from " + TABLE_TAG;
		
		Log.e(LOG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				Tag tag = new Tag();
				tag.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				tag.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));
				tag.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				
				tags.add(tag);				
			} while (c.moveToNext());
		}
		
		return tags;
	}
	
	// Update tag
	public int updateTag(Tag tag) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_TAG_NAME, tag.getTagName());
		
		return db.update(TABLE_TAG, values, KEY_ID + "=?",
				new String[] {String.valueOf(tag.getId())});
	}
	
	// Delete tag
	
	// TODO : NOTE_TAGS tablosundan silme. rapor için kullanabilirsin...
	public void deleteTag(Tag tag, boolean should_delete_all_tag_notes) {
		SQLiteDatabase db = this.getWritableDatabase();

		List<Note> allTagNotes = getAllNotesByTag(tag.getTagName());
		if (should_delete_all_tag_notes) {
			for(Note note : allTagNotes) {
				deleteNote(note.getId());
			}
		}
		else {
			for(Note note : allTagNotes) {
				removeTagFromNote(tag.getId(), note.getId());
			}
		}
		db.delete(TABLE_TAG, KEY_ID + "=?",
				new String[] {String.valueOf(tag.getId())});
	}
	
	
	// FOLDERS
	
	// Create folder
	public long createFolder(Folder folder) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_FOLDER_NAME, folder.getFolderName());
		values.put(KEY_CREATED_AT, getDateTime());
		
		long folder_id = db.insert(TABLE_FOLDER, null, values);
		
		return folder_id;
	}
	
	// Get folder by name
	public Folder getFolderByName(String folderName) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "select * from " + TABLE_FOLDER
				+ " where " + KEY_FOLDER_NAME + " = '" + folderName + "'"; 
		
		Log.e(LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		if (c != null) {
			c.moveToFirst();
		}
		Folder folder = new Folder();
		folder.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		folder.setFolderName(c.getString(c.getColumnIndex(KEY_FOLDER_NAME)));
		folder.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
		
		return folder;
	}
	
	// Get all folders
	public List<Folder> getAllFolders() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<Folder> folders = new ArrayList<Folder>();
		
		String selectQurey = "select * from " + TABLE_FOLDER;
		
		Log.e(LOG, selectQurey);
		
		Cursor c = db.rawQuery(selectQurey, null);
		if (c.moveToFirst()) {
			do {
				Folder folder = new Folder();
				folder.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				folder.setFolderName(c.getString(c.getColumnIndex(KEY_FOLDER_NAME)));
				folder.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				
				folders.add(folder);	
			} while (c.moveToNext());
		}
		
		return folders;
	}
	
	// Update folder
	public int updateFolder(Folder folder) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_FOLDER_NAME, folder.getFolderName());
		
		return db.update(TABLE_FOLDER, values, KEY_ID + "=?",
				new String[] {String.valueOf(folder.getId())});
	}
	
	// Delete folder
	public void deleteFolder(Folder folder, boolean should_delete_all_folder_notes) {
		SQLiteDatabase db = this.getWritableDatabase();

		List<Note> allFolderNotes = getAllNotesByFolder(folder.getFolderName());
		if (should_delete_all_folder_notes) {
			for(Note note : allFolderNotes) {
				deleteNote(note.getId());
			}
		}
		else {
			for(Note note : allFolderNotes) {
				removeFolderFromNote(folder.getId(), note.getId());
			}
		}
		db.delete(TABLE_FOLDER, KEY_ID + "=?",
				new String[] {String.valueOf(folder.getId())});
	}
	
	
	// Assign a tag to note
	public long createNoteTag(long note_id, long tag_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_NOTE_ID, note_id);
		values.put(KEY_TAG_ID, tag_id);
		values.put(KEY_CREATED_AT, getDateTime());
		
		long id = db.insert(TABLE_NOTE_TAGS, null, values);
		
		return id;
	}
	
	// Assign a folder to note
	public long createNoteFolder(long note_id, long folder_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_NOTE_ID, note_id);
		values.put(KEY_FOLDER_ID, folder_id);
		values.put(KEY_CREATED_AT, getDateTime());
		
		long id = db.insert(TABLE_NOTE_FOLDERS, null, values);
		
		return id;
	}
	
	// Remove a tag from note
	public void deleteNoteTag(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTE_TAGS, KEY_ID + "=?",
				new String[] {String.valueOf(id)});
	}
	
	public void removeTagFromNote(long note_id, long tag_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTE_TAGS, KEY_NOTE_ID + "=? and "
				+ KEY_TAG_ID + "=?", new String[] {String.valueOf(note_id), String.valueOf(tag_id)});
	}
	
	// Remove a folder from note
	public void deleteNoteFolder(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTE_FOLDERS, KEY_ID + "=?",
				new String[] {String.valueOf(id)});
	}
	
	public void removeFolderFromNote(long note_id, long folder_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTE_FOLDERS, KEY_NOTE_ID + "=? and "
				+ KEY_FOLDER_ID + "=?", new String[] {String.valueOf(note_id), String.valueOf(folder_id)});
	}
	
	// Changing tag of note
	public int updateNoteTag(long id, long tag_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_TAG_ID, tag_id);	
		
		return db.update(TABLE_NOTE_TAGS, values, KEY_ID + "=?",
				new String[] {String.valueOf(id)});
	}
	
	// Changing folder of note
	public int updateNoteFolder(long id, long folder_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_FOLDER_ID, folder_id);	
		
		return db.update(TABLE_NOTE_FOLDERS, values, KEY_ID + "=?",
				new String[] {String.valueOf(id)});
	}
	
	// Close Database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	
	// Get Date and Time
	public String getDateTime() {
		return new Date().toString();
	}

}
