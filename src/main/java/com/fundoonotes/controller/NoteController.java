package com.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	@PostMapping("/create")
	private ResponseEntity<Response> createNote(@RequestBody NoteDto notedto, @RequestParam String email)throws NoteException{

		NoteModel note = noteService.createNote(notedto, email);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is created successfully",200));
	}

	@PostMapping("/updatenote")
	public ResponseEntity<Response> updateNote(@RequestBody NoteDto notedto, @RequestParam("id") long id, @RequestParam("email") String email) {
		boolean result = noteService.updateNote(notedto, id, email);
		if (result) 
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note is update successfully", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Something went wrong", 400));
		
	}

	@PutMapping("/addcolor")
	public ResponseEntity<Response> addColor(@RequestParam("email") String email, @RequestParam("id") long id, @RequestParam("color") String color)
	{
		boolean result = noteService.addColor(email, id, color);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Color is added", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Error! color is not added", 400));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteNote(@RequestParam("email") String email, @RequestParam("id") long id){

		boolean result = noteService.deleteNote(email, id);
		if (result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Deleted Succussfully", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Error! note can't be deleted", 400));
	}
	
	@PostMapping("/allnotes")
	public ResponseEntity<Response> getAllNotes(@RequestParam("email") String email)  {
		
		List<NoteModel> notesList = noteService.getAllNotes(email);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All notes of user", 200, notesList));
	}
}
