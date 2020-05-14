package com.fundoonotes.service;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fundoonotes.model.CollaboratorModel;

@Component
public interface CollaboratorService {

	CollaboratorModel addCollaborator(String email, long noteId);

	Optional<CollaboratorModel> deleteCollaborator(Long collaboratorId, String email, long noteId);

	List<CollaboratorModel> getNoteCollaborators(String email, long noteId);

	CollaboratorModel mapCollaboratorToNote(String token, long collaboratorid, long noteid);

}
