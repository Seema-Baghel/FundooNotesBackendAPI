package com.fundoonotes.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fundoonotes.dto.NoteDto;
import com.fundoonotes.dto.ReminderDateTimeDto;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.responses.Response;

@Component
public interface NoteService {

	public NoteModel createNote(NoteDto noteDto, String token);
	
	public boolean updateNote(NoteDto noteDto, long noteId, String token);

	public boolean addColor(String token, long noteId, String noteColor);
	
	public boolean deleteNote(String token, long noteId);

	public List<NoteModel> getAllNotes(String token);

	public List<NoteModel> searchByTitle(String token, String noteTitle);

	public List<NoteModel> searchByDecription(String token, String noteDescription);
	
	public ResponseEntity<String> setReminder(ReminderDateTimeDto reminderDateTimeDto, long id);

	public ResponseEntity<String> unsetReminder(long id);
	
	public boolean isPinnedNote(String token, long noteId);
	
	public List<NoteModel> allPinnedNotes(String token);

	public List<NoteModel> allUnpinnedNotes(String token);

	public boolean isArchivedNote(String token, long noteId);

	public List<NoteModel> allArchived(String token);

	public List<NoteModel> allUnarchived(String token);

	public boolean trashNote(String token, long noteId);

	public List<NoteModel> allTrashedNotes(String token);

	public boolean restoreNote(String token, long noteId);

	public ResponseEntity<Object> sortByTitle();

	public ResponseEntity<Object> sortByDescription();

	

	
	
}
