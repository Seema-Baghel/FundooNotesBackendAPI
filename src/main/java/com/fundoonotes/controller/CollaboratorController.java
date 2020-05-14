package com.fundoonotes.controller;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundoonotes.model.CollaboratorModel;
import com.fundoonotes.responses.Response;
import com.fundoonotes.service.CollaboratorService;

@RestController
@RequestMapping("/collaborator")
public class CollaboratorController {

	@Autowired
	private CollaboratorService collaboratorService;
	
	/*
	 * API to add a collaborator
	 * 
	 * @param collaborator email
	 * @param noteId
	 */

	@PostMapping("/addCollaborator")
	private ResponseEntity<Response> addCollaborator(@RequestParam("email") String email, @RequestParam("noteId") long noteId) {
		CollaboratorModel result = collaboratorService.addCollaborator(email, noteId);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Added collabrator sucessfully!!!"));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(400, "Sorry! No collaborator added"));
	}
	
	/*
	 * API to map a collaborator note
	 * 
	 * @header token
	 * @param collaborator id
	 * @param noteId
	 */
	
	@PostMapping("/mapCollaborator")
    public ResponseEntity<Response> mapCollaboratorToNote(@RequestHeader("token") String token,
            @RequestParam("collaboratorid") long collaboratorid, @RequestParam("noteid") long noteid) {
		CollaboratorModel result = collaboratorService.mapCollaboratorToNote(token, collaboratorid, noteid);
        if (result != null)
            return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "collaborator added"));
        return ResponseEntity.status(HttpStatus.OK).body(new Response(400, "Something went wrong"));
    }

	
	
	/*
	 * API to delete collaborator
	 * 
	 * @header collaborator email
	 * @param noteId
	 * @param collaboratorid
	 */

	@DeleteMapping("/deleteCollaborator")
	public ResponseEntity<Response> deleteCollaborator(@RequestParam(value = "noteId") long noteId, @RequestHeader("email") String email,
														@RequestHeader("collaboratorId") long collaboratorId) {
		Optional<CollaboratorModel> result = collaboratorService.deleteCollaborator(collaboratorId, email, noteId);
		if(result != null) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Deleted collaborator sucessfully!!!",200, result));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(400, "Sorry! Cannot delete "));
	}

	/*
	 * API to get all collaborator
	 * 
	 * @header collaborator email
	 * @param noteId
	 */
	
	@GetMapping("/getAllNoteCollaborator")
	public ResponseEntity<Response> getAllCollaborator(@RequestParam(value = "noteId") long noteId,
														@RequestHeader("email") String email) {
		List<CollaboratorModel> collaboratorList = collaboratorService.getNoteCollaborators(email, noteId);
		if(collaboratorList != null) 
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response("All note collaborators are", 200, collaboratorList));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(200, "Sorry! no collaborator found"));
	}
}