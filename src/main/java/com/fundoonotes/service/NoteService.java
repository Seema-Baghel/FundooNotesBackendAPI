package com.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fundoonotes.dto.NoteDto;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.responses.Response;

@Service
public interface NoteService {

	public NoteModel createNote(NoteDto noteDto, String email);
	
	public boolean updateNote(NoteDto noteDto, long noteId, String email);

	public boolean addColor(String email, long noteId, String noteColor);
	
	public boolean deleteNote(String email, long noteId);

	public List<NoteModel> getAllNotes(String email);

	
}
