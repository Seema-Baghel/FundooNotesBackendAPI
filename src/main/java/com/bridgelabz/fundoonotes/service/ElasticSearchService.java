package com.bridgelabz.fundoonotes.service;
//package com.fundoonotes.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.UserModel;

@Component
public interface ElasticSearchService {

	String createNote(NoteModel note);

//	String deleteNote(NoteModel note);

	List<NoteModel> searchbytitle(String title) throws IOException;

	String updateNote(NoteModel note);

	String deleteNote(UserModel note);
}
