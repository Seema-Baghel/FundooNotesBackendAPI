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
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully",200));
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
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is update successfully", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
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
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Color is added", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Error! color is not added", 400));
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
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Deleted Succussfully", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Error! note can't be deleted", 400));
	}
	
	/*
	 * API to get all notes
	 */
	
	@GetMapping("/allnotes")
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token)  {
		
		List<NoteModel> notesList = noteService.getAllNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All notes of user", 200, notesList));
	}
	
	/*
	 * API to search notes by its title
	 */
	
	@GetMapping("search")
	public ResponseEntity<Response> searchByTitle(@RequestHeader("token") String token, @RequestParam("title") String noteTitle) {
		List<NoteModel> findNotes = noteService.searchByTitle(token, noteTitle);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("found notes", 200, findNotes));

	}
	
	/*
	 * API to set reminder to notes
	 */
	
	@PutMapping("/reminder/{noteId}/{reminder}")
	public Response setReminder(@PathVariable String reminder, @PathVariable long noteId) throws Exception {
		return noteService.setReminder(noteId, reminder);
	}
}
