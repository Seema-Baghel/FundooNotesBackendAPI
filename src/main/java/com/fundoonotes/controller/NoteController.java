package com.fundoonotes.controller;

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

import com.fundoonotes.dto.NoteDto;
import com.fundoonotes.exception.NoteException;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.responses.Response;
import com.fundoonotes.service.NoteService;

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

		NoteModel note = noteService.createNote(notedto, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Note is created successfully"));
	}

	/*
	 * API to update notes
	 * 
	 * @param noteid
	 * @header token
	 */
	
	@PostMapping("/updatenote")
	public ResponseEntity<Response> updateNote(@RequestBody NoteDto notedto, @RequestParam("id") long id, @RequestHeader("token") String token) {
		boolean result = noteService.updateNote(notedto, id, token);
		if (result) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Note is update successfully"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Something went wrong"));
	}
	
	/*
	 * API to add color to notes
	 * 
	 * @param color
	 * @param noteid
	 * @header token
	 */

	@PutMapping("/addcolor")
	public ResponseEntity<Response> addColor(@RequestHeader("token") String token, @RequestParam("id") long id, @RequestParam("color") String color)
	{
		boolean result = noteService.addColor(token, id, color);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Color is added"));
		return ResponseEntity.status(HttpStatus.OK).body(new Response(400, "Error! color is not added"));
	}
	
	/*
	 * API to delete notes permanently
	 * 
	 * @param noteid
	 * @header token
	 */
	
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteNote(@RequestHeader("token") String token, @RequestParam("id") long id){

		boolean result = noteService.deleteNote(token, id);
		if (result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Deleted Succussfully"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Error! note can't be deleted"));
	}
	
	/*
	 * API to get all notes
	 * 
	 * @header token
	 */
	
	@GetMapping("/allnotes")
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token)  {
		
		List<NoteModel> notesList = noteService.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All notes of user", 200, notesList));
	}
	
	/*
	 * API to search notes by its title
	 */
	
	@GetMapping("/searchByTitle")
	public ResponseEntity<Response> searchByTitle(@RequestHeader("token") String token, @RequestParam("title") String noteTitle) {
		List<NoteModel> findNotes = noteService.searchByTitle(token, noteTitle);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("found notes", 200, findNotes));

	}
	
	@GetMapping("/searchByDescription")
	public ResponseEntity<Response> searchByDescription(@RequestHeader("token") String token, @RequestParam("description") String noteDescription) {
		List<NoteModel> findNotes = noteService.searchByDecription(token, noteDescription);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("found notes", 200, findNotes));

	}
	
	/*
	 * API to set reminder to notes
	 */
	
	@PutMapping("/reminder/{noteId}/{reminder}")
	public Response setReminder(@PathVariable String reminder, @PathVariable long noteId) throws Exception {
		return noteService.setReminder(noteId, reminder);
	}
	
	/*
	 * API to pin and unpin a notes
	 */
	
	@PatchMapping("/pin/{id}")
	public ResponseEntity<Response> pinNote(@RequestHeader("token") String token, @PathVariable("id") long noteId) {
		if (noteService.isPinnedNote(token, noteId))
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Note pinned"));
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response(200, "Note unpinned"));
	}
	
	/*
	 * API to get all pined notes
	 */
		
	@GetMapping("/allpinnednotes")
	private ResponseEntity<Response> getPinnedNotes(@RequestHeader("token") String token) {
		List<NoteModel> notesList = noteService.allPinnedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all pinned notes of user",200, notesList));
	}
	
	/*
	 * API to get all unpined notes
	 */
	
	@GetMapping("/allunpinnednotes")
	private ResponseEntity<Response> getAllUnpinnedNotes(@RequestHeader("token") String token) {
		
		List<NoteModel> notesList = noteService.allUnpinnedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all unpinned notes of user",200, notesList));
	}
	
	/*
	 * API to delete a notes and put it in trash
	 */
	
	@DeleteMapping("/trash")
	public ResponseEntity<Response> trashNote(@RequestHeader("token") String token, @RequestParam("id") long noteId) {
		if (noteService.trashNote(token, noteId)) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Note trashed"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400,"Error! Note is not trashed"));
	}
	
	/*
	 * API to get all trashed notes from trash
	 */
	
	@GetMapping("/alltrashednotes")
	public ResponseEntity<Response> getAllTrashedNotes(@RequestHeader("token") String token) {
		List<NoteModel> notesList = noteService.allTrashedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all trashed notes of user",200, notesList));
	}
	
	/*
	 * API to restore a notes and from trash
	 */
	
	@PutMapping("/restore")
	public ResponseEntity<Response> restoreFromTrashed(@RequestHeader("token") String token, @RequestParam("id") long noteId) {
		if (noteService.restoreNote(token, noteId)) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200,"Note restored"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400,"Error in Restoring note!"));

	}
	
	/*
	 * API to  a notes
	 */
	
	@DeleteMapping("/archive")
	public ResponseEntity<Response> archieveNote(@RequestHeader("token") String token, @RequestParam("id") long noteId) {
		if (noteService.isArchivedNote(token, noteId)) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Note archieved"));
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response(200, "Note unarchived"));
	}
	
	/*
	 * API to get all archived notes
	 */
	
	@GetMapping("/getallarchived")
	private ResponseEntity<Response> getallarchived(@RequestHeader("token") String token) {
		List<NoteModel> notesList = noteService.allArchived(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all archived notes of user",200, notesList));
	}
	
	/*
	 * API to get all unarchived notes
	 */
	
	@GetMapping("/getallunarchived")
	private ResponseEntity<Response> getallunarchived(@RequestHeader("token") String token) {
		List<NoteModel> notesList = noteService.allUnarchived(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("all unarchived notes of user",200, notesList));
	}
}
