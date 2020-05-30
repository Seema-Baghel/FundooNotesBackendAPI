package com.bridgelabz.fundoonotes.controller;

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

import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.CollaboratorModel;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.CollaboratorService;
import com.bridgelabz.fundoonotes.utility.Util;

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
	private ResponseEntity<Response> addCollaborator(@RequestHeader("token") String token, @RequestParam("email") String email, @RequestParam("noteId") long noteId) {
		
		return collaboratorService.addCollaborator(token, email, noteId);
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

		return collaboratorService.mapCollaboratorToNote(token, collaboratorid, noteid);
    }
	
	/*
	 * API to delete collaborator
	 * 
	 * @header collaborator email
	 * @param noteId
	 * @param collaboratorid
	 */

	@DeleteMapping("/deleteCollaborator")
	public ResponseEntity<Response> deleteCollaborator(@RequestParam(value = "noteId") long noteId, @RequestHeader("token") String token,
														@RequestParam("collaboratorId") long collaboratorId) throws UserNotFoundException {
		
		return collaboratorService.deleteCollaborator(collaboratorId, token, noteId);
	}

	/*
	 * API to get all collaborator
	 * 
	 * @header collaborator email
	 * @param noteId
	 */
	
	@GetMapping("/getAllNoteCollaborator")
	public ResponseEntity<Response> getAllCollaborator(@RequestParam(value = "noteId") long noteId, @RequestHeader("token") String token) throws UserNotFoundException {
		List<CollaboratorModel> collaboratorList = collaboratorService.getNoteCollaborators(token, noteId);
		if(collaboratorList != null) 
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response("All note collaborators are", Util.OK_RESPONSE_CODE, collaboratorList));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.OK_RESPONSE_CODE, "Sorry! no collaborator found"));
	}
}