package com.fundoonotes.service;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fundoonotes.model.CollaboratorModel;

@Component
public interface CollaboratorService {

	CollaboratorModel addCollaborator(String email, long noteId);

	Optional<CollaboratorModel> deleteCollaborator(long collaboratorId, String token, long noteId);

	List<CollaboratorModel> getNoteCollaborators(String token, long noteId);

	CollaboratorModel mapCollaboratorToNote(String token, long collaboratorid, long noteid);

}
