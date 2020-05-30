package com.bridgelabz.fundoonotes.service;

import java.util.List;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.CollaboratorModel;
import com.bridgelabz.fundoonotes.responses.Response;

@Component
public interface CollaboratorService {

	ResponseEntity<Response> addCollaborator(String token, String email, long noteId);

	ResponseEntity<Response> deleteCollaborator(long collaboratorId, String token, long noteId) throws UserNotFoundException;

	List<CollaboratorModel> getNoteCollaborators(String token, long noteId) throws UserNotFoundException;

	ResponseEntity<Response> mapCollaboratorToNote(String token, long collaboratorid, long noteid);

}
