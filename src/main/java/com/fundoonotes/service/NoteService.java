package com.fundoonotes.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fundoonotes.dto.NoteDto;
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

	public Response setReminder(long noteId, String reminder);

	
}
