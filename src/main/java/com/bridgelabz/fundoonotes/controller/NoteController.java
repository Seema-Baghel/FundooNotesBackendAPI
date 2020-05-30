package com.bridgelabz.fundoonotes.controller;

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
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.Util;

@RestController
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	private NoteService noteService;

	/*
	 * API to create notes
	 */
	
	@PostMapping("/create")
	private ResponseEntity<Response> createNote(@RequestBody NoteDto notedto, @RequestHeader("token") String token)throws NoteException{

		return noteService.createNote(notedto, token);
	}

	/*
	 * API to update notes
	 * 
	 * @param noteid
	 * @header token
	 */
	
	@PostMapping("/updatenote")
	public ResponseEntity<Response> updateNote(@RequestBody NoteDto notedto, @RequestParam("id") long id, @RequestHeader("token") String token) {
		return noteService.updateNote(notedto, id, token);
	}
	
	/*
	 * API to add color to notes
	 * 
	 * @param color
	 * @param noteid
	 * @header token
	 */

	@PutMapping("/addcolor")
	public ResponseEntity<Response> addColor(@RequestHeader("token") String token, @RequestParam("id") long id, @RequestParam("color") String color){
		
		return noteService.addColor(token, id, color);
	}
	
	/*
	 * API to delete notes permanently
	 * 
	 * @param noteid
	 * @header token
	 */
	
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteNote(@RequestHeader("token") String token, @RequestParam("id") long id){

		return noteService.deleteNote(token, id);
	}
	
	/*
	 * API to get all notes
	 * 
	 * @header token
	 */
	
	@GetMapping("/allnotes")
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token)  {
		
		List<NoteModel> notesList = noteService.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All notes of user", Util.OK_RESPONSE_CODE, notesList));
	}
	
	/*
	 * API to search notes by its title
	 */
	
	@GetMapping("/searchByTitle")
	public ResponseEntity<Response> searchByTitle(@RequestHeader("token") String token, @RequestParam("title") String noteTitle) {
		
		List<NoteModel> findNotes = noteService.searchByTitle(token, noteTitle);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("found notes", Util.OK_RESPONSE_CODE, findNotes));
	}
	
	/*
	 * API to search notes by its Description
	 */
	
	@GetMapping("/searchByDescription")
	public ResponseEntity<Response> searchByDescription(@RequestHeader("token") String token, @RequestParam("description") String noteDescription) {
		
		List<NoteModel> findNotes = noteService.searchByDecription(token, noteDescription);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("found notes", Util.OK_RESPONSE_CODE, findNotes));
	}
	
	/*
	 * API to sort notes by its title
	 */
	
	@GetMapping("/sortByTitle")
	public ResponseEntity<Object> sortByTitle() {

		return noteService.sortByTitle();
	}
	
	/*
	 * API to sort notes by its Description
	 */
	
	@GetMapping("/sortByDescription")
	public ResponseEntity<Object> sortByDescription() {

		return noteService.sortByDescription();
	}
	
	/*
	 * API to set reminder to notes
	 */
	
	@PostMapping("/setReminder/{id}")
	public ResponseEntity<Response> setReminder(@RequestHeader("token") String token, @RequestBody ReminderDateTimeDto reminderDateTimeDto, @PathVariable("id") long id) {
		
		return noteService.setReminder(token, reminderDateTimeDto, id);
	}
	
	/*
	 * API to unset reminder to notes
	 */
	@PutMapping("/unsetReminder/{id}")
	public ResponseEntity<Response> unsetReminder(@PathVariable("id") long id, @RequestHeader("token") String token) {

		return noteService.unsetReminder(id, token);
	}
	
	/*
	 * API to pin and unpin a notes
	 */
	
	@PatchMapping("/pin/{id}")
	public ResponseEntity<Response> pinNote(@RequestHeader("token") String token, @PathVariable("id") long noteId) {

		return noteService.isPinnedNote(token, noteId);

	}
	
	/*
	 * API to get all pined notes
	 */
		
	@GetMapping("/allpinnednotes")
	private ResponseEntity<Response> getPinnedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allPinnedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all pinned notes of user",Util.OK_RESPONSE_CODE, notesList));
	}
	
	/*
	 * API to get all unpined notes
	 */
	
	@GetMapping("/allunpinnednotes")
	private ResponseEntity<Response> getAllUnpinnedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allUnpinnedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all unpinned notes of user",Util.OK_RESPONSE_CODE, notesList));
	}
	
	/*
	 * API to delete a notes and put it in trash
	 */
	
	@DeleteMapping("/trash")
	public ResponseEntity<Response> trashNote(@RequestHeader("token") String token,@RequestParam("id") long noteId) {

		return noteService.trashNote(token, noteId); 
	}
	
	/*
	 * API to get all trashed notes from trash
	 */
	
	@GetMapping("/alltrashednotes")
	public ResponseEntity<Response> getAllTrashedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allTrashedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all trashed notes of user",Util.OK_RESPONSE_CODE, notesList));
	}
	
	/*
	 * API to restore a notes and from trash
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
	
	
}
