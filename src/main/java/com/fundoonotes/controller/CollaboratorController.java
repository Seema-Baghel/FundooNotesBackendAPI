package com.fundoonotes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundoonotes.dto.CollaboratorDto;
import com.fundoonotes.model.CollaboratorModel;
import com.fundoonotes.responses.Response;
import com.fundoonotes.service.CollaboratorService;

@RestController
@RequestMapping("/collaborator")
public class CollaboratorController {

	@Autowired
	private CollaboratorService collaboratorService;

	@PostMapping("/addCollaborator")
	private ResponseEntity<Response> addCollaborator(@RequestParam("email") String email, @RequestParam("noteId") long noteId) {
		CollaboratorDto collaboratorDto=new CollaboratorDto();
		collaboratorDto.setEmail(email);
		CollaboratorModel result = collaboratorService.addCollaborator(collaboratorDto, email, noteId);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Added collabrator sucessfully!!!",200, result));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Sorry! No collaborator added", 400));
	}

	@DeleteMapping("/deleteCollaborator")
	public ResponseEntity<Response> deleteCollaborator(@RequestParam(value = "noteId") long noteId, @RequestHeader("email") String email,
														@RequestHeader("collaboratorId") long collaboratorId) {
		Optional<CollaboratorModel> result = collaboratorService.deleteCollaborator(collaboratorId, email, noteId);
		if(result != null) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Deleted collaborator sucessfully!!!",200, result));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Sorry! Cannot delete ", 400));
	}

	@GetMapping("/getAllNoteCollaborator")
	public ResponseEntity<Response> getAllCollaborator(@RequestParam(value = "noteId") long noteId,
			@RequestHeader("email") String email) {
		List<CollaboratorModel> collaboratorList = collaboratorService.getNoteCollaborators(email, noteId);
		if(collaboratorList != null) 
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response("All note collaborators are", 200, collaboratorList));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Sorry! no collaborator found", 400));
	}
}