package com.fundoonotes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fundoonotes.dto.CollaboratorDto;
import com.fundoonotes.model.CollaboratorModel;

@Component
public interface CollaboratorService {

	CollaboratorModel addCollaborator(CollaboratorDto collaboratorDto, String email, long noteId);

	Optional<CollaboratorModel> deleteCollaborator(Long collaboratorId, String email, long noteId);

	List<CollaboratorModel> getNoteCollaborators(String email, long noteId);

}
