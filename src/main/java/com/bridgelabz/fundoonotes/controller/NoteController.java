package com.bridgelabz.fundoonotes.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDateTimeDto;
import com.bridgelabz.fundoonotes.exception.NoteException;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.Util;

@RestController
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	private NoteService noteService;

	/**
	 * This function takes {@link NoteDto} as request body and token from
	 * {@link RequestHeader} and verify originality of client
	 * {@link NoteServiceImpl} and accordingly returns the response.
	 * 
	 * @param noteDto as {@link UserDTO}
	 * @param token as String input parameter
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/notes/create
	 */
	
	@PostMapping("/create")
	private ResponseEntity<Response> createNote(@RequestBody NoteDto notedto, @RequestHeader("token") String token)throws NoteException{

		return noteService.createNote(notedto, token);
	}

	/**
	 * This function takes {@link NoteDTO} as request body and token from
	 * {@link RequestHeader} and verify originality of client
	 * {@link NoteServiceImpl} and after update accordingly it returns the response.
	 * 
	 * @param noteDto as {@link UserDTO}
	 * @param token   as String input parameter
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/notes/updatenote?id=85
	 */
	
	@PostMapping("/updatenote")
	public ResponseEntity<Response> updateNote(@RequestBody NoteDto notedto, @RequestParam("id") long id, @RequestHeader("token") String token) {
		return noteService.updateNote(notedto, id, token);
	}
	
	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of client {@link NoteServiceImpl} after verification allows user
	 * to change color of the personalized note.
	 * 
	 * @param token      as {@link RequestHeader}
	 * @param noteId     as {@link RequestParam}
	 * @param noteColour as {@link RequestParam}
	 * @return ResponseEntity<Response>
	 * @URL -> http://localhost:8080/notes/addcolor?id=1&color=red
	 */
	
	@PutMapping("/addcolor")
	public ResponseEntity<Response> addColor(@RequestHeader("token") String token, @RequestParam("id") long id, @RequestParam("color") String color){
		
		return noteService.addColor(token, id, color);
	}
	
	/**
	 * This function takes noteId as {@link RequestParam} and token as
	 * {@link RequestHeader} and verify originality of client
	 * {@link NoteServiceImpl} and after deletion operation of note accordingly it
	 * returns the response.
	 * 
	 * @param noteId as {@link RequestParam}
	 * @param token  as {@link RequestHeader}
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/note/delete?id=1
	 */
	
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteNote(@RequestHeader("token") String token, @RequestParam("id") long id){

		return noteService.deleteNote(token, id);
	}
	
	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of client {@link NoteServiceImpl} after verification allows user
	 * to get all notes which are not trashed.
	 * 
	 * @param token as {@link RequestHeader}
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/note/allnotes
	 */
	
	@GetMapping("/allnotes")
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token)  {
		
		List<NoteModel> notesList = noteService.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All notes of user", Util.OK_RESPONSE_CODE, notesList));
	}
	
	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of client {@link NoteServiceImpl} after verification allows user
	 * to search for notes based on title.
	 * 
	 * @param token as {@link RequestHeader}
	 * @param noteTitle as as {@link RequestParam}
	 * @return ResponseEntity<Response>
	 * @throws IOException 
	 * @URL -> http://localhost:8080/notes/searchByTitle?title=note1
	 */
	
	@GetMapping("/searchByTitle")
	public ResponseEntity<Response> searchByTitle(@RequestHeader("token") String token, @RequestParam("title") String noteTitle) throws IOException {
		
		List<NoteModel> findNotes = noteService.searchByTitle(token, noteTitle);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("found notes", Util.OK_RESPONSE_CODE, findNotes));
	}

	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of client {@link NoteServiceImpl} after verification allows user
	 * to search for notes based on title.
	 * 
	 * @param token as {@link RequestHeader}
	 * @param noteTitle as as {@link RequestParam}
	 * @return ResponseEntity<Response>
	 * @URL -> http://localhost:8080/notes/searchByDescription?description=helloworld
	 */
	
	@GetMapping("/searchByDescription")
	public ResponseEntity<Response> searchByDescription(@RequestHeader("token") String token, @RequestParam("description") String noteDescription) {
		
		List<NoteModel> findNotes = noteService.searchByDecription(token, noteDescription);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("found notes", Util.OK_RESPONSE_CODE, findNotes));
	}
	
	/**
	 * This function is used to sort the data in the table
	 * 
	 * @return ResponseEntity<Response>
	 * @URL -> http://localhost:8080/notes/sortByTitle
	 */
	
	@GetMapping("/sortByTitle")
	public ResponseEntity<Object> sortByTitle() {

		return noteService.sortByTitle();
	}
	
	/**
	 * This function is used to sort the data in the table
	 * 
	 * @return ResponseEntity<Response>
	 * @URL -> http://localhost:8080/notes/sortByDescription
	 */
	
	@GetMapping("/sortByDescription")
	public ResponseEntity<Object> sortByDescription() {

		return noteService.sortByDescription();
	}
	
	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of client {@link NoteServiceImpl} after verification allows user
	 * to add remainder to the personalized note.
	 * 
	 * @param token        as {@link RequestHeader}
	 * @param noteId       as {@link PathVariable}
	 * @param remainderDTO as {@link RequestBody ReminderDateTimeDto}
	 * @return ResponseEntity<Response>
	 * @URL -> http://localhost:8080/notes/setReminder/1/add
	 */
	
	@PostMapping("/setReminder/{id}")
	public ResponseEntity<Response> setReminder(@RequestHeader("token") String token, @RequestBody ReminderDateTimeDto reminderDateTimeDto, @PathVariable("id") long id) {
		
		return noteService.setReminder(token, reminderDateTimeDto, id);
	}
	
	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of client {@link NoteServiceImpl} after verification allows user
	 * to remove the remainder from the personalized note.
	 * 
	 * @param token  as {@link RequestHeader}
	 * @param noteId as {@link PathVariable}
	 * @return ResponseEntity<Response>
	 * @URL -> http://localhost:8080/notes/unsetReminder/1
	 */
	
	@PutMapping("/unsetReminder/{id}")
	public ResponseEntity<Response> unsetReminder(@PathVariable("id") long id, @RequestHeader("token") String token) {

		return noteService.unsetReminder(id, token);
	}
	
	/**
	 * This function takes noteId as {@link PathVariable} and token as
	 * {@link RequestHeader} and verify originality of client
	 * {@link NoteServiceImpl} and after pin operation of note accordingly it
	 * returns the response.
	 * 
	 * @param noteId as {@link PathVariable}
	 * @param token  as {@link RequestHeader}
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/notes/pin/1
	 */
	
	@PatchMapping("/pin/{id}")
	public ResponseEntity<Response> pinNote(@RequestHeader("token") String token, @PathVariable("id") long noteId) {

		return noteService.isPinnedNote(token, noteId);

	}
	
	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of client {@link NoteServiceImpl} after verification allows user
	 * to get all notes which are pinned.
	 * 
	 * @param token as {@link RequestHeader}
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/notes/allpinnednotes
	 */
		
	@GetMapping("/allpinnednotes")
	private ResponseEntity<Response> getPinnedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allPinnedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all pinned notes of user",Util.OK_RESPONSE_CODE, notesList));
	}
	
	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of client {@link NoteServiceImpl} after verification allows user
	 * to get all notes which are unpinned.
	 * 
	 * @param token as {@link RequestHeader}
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/notes/allunpinnednotes
	 */
	
	@GetMapping("/allunpinnednotes")
	private ResponseEntity<Response> getAllUnpinnedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allUnpinnedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all unpinned notes of user",Util.OK_RESPONSE_CODE, notesList));
	}
	
	/**
	 * This function takes noteId as {@link RequestParam} and token as
	 * {@link RequestHeader} and verify originality of client
	 * {@link NoteServiceImpl} and after trash operation of note accordingly it
	 * returns the response.
	 * 
	 * @param noteId as {@link RequestParam}
	 * @param token  as {@link RequestHeader}
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/notes/trash?id=1
	 */
	
	@DeleteMapping("/trash")
	public ResponseEntity<Response> trashNote(@RequestHeader("token") String token,@RequestParam("id") long noteId) {

		return noteService.trashNote(token, noteId); 
	}
	
	/**
	 * This function takes authentication token as {@link RequestHeader} and verify
	 * originality of user {@link NoteServiceImpl} after verification allows user to
	 * get all trashed notes.
	 * 
	 * @param token as {@link RequestHeader}
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/notes/alltrashednotes
	 */
	
	@GetMapping("/alltrashednotes")
	public ResponseEntity<Response> getAllTrashedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allTrashedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all trashed notes of user",Util.OK_RESPONSE_CODE, notesList));
	}
	
	/**
	 * This function takes noteId as {@link RequestParam} and token as
	 * {@link RequestHeader} and verify originality of client
	 * {@link NoteServiceImpl} and after restore operation of note accordingly it
	 * returns the response.
	 * 
	 * @param noteId as {@link RequestParam}
	 * @param token  as {@link RequestHeader}
	 * @return ResponseEntity<Response>
	 * @URL http://localhost:8080/notes/restore?id=1
	 */
	
	@PutMapping("/restore")
	public ResponseEntity<Response> restoreFromTrashed(@RequestHeader("token") String token, @RequestParam("id") long noteId) {
		 
		return noteService.restoreNote(token, noteId);
	}
	
	/*
	 * API to  a notes
	 */
	
	@DeleteMapping("/archive")
	public ResponseEntity<Response> archieveNote(@RequestHeader("token") String token, @RequestParam("id") long noteId) {
		
		return noteService.isArchivedNote(token, noteId);

	}
	
	/*
	 * API to get all archived notes
	 */
	
	@GetMapping("/getallarchived")
	private ResponseEntity<Response> getallarchived(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allArchived(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all archived notes of user",Util.OK_RESPONSE_CODE, notesList));
	}
	
	/*
	 * API to get all unarchived notes
	 */
	
	@GetMapping("/getallunarchived")
	private ResponseEntity<Response> getallunarchived(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allUnarchived(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all unarchived notes of user",Util.OK_RESPONSE_CODE, notesList));
	}
	
	/*
	 * API to add Collaborator
	 */
	
	@PutMapping("/addCollaborator")
	public ResponseEntity<Response> addCollaborator(@RequestHeader String token, @RequestParam long noteId , @RequestParam String email) throws UserNotFoundException{

		return noteService.addCollaborator(token ,email, noteId);
			
	}
	
	/*
	 * API to delete Collaborator
	 */
	
	@PutMapping("/deleteCollaborator")
	public ResponseEntity<Response> deleteCollaborator(@RequestHeader String token, @RequestParam long noteId,@RequestParam String email) throws UserNotFoundException{

		return noteService.deleteCollaboratorInNote(token, noteId, email);
	}
	
	
}
