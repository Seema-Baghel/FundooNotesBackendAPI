package com.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundoonotes.dto.LabelDto;
import com.fundoonotes.model.LabelModel;
import com.fundoonotes.responses.Response;
import com.fundoonotes.service.LabelService;

@RestController
@RequestMapping("/label")
public class LabelController {

	@Autowired
	private LabelService labelService;
		
	@PostMapping("/addlabel")
	public ResponseEntity<Response> createLabel(@RequestBody LabelDto labeldto, @RequestHeader("email") String email) {
		int result = labelService.createLabel(labeldto, email);
		if (result != 0)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is Created", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Already exist in label ", 400));
	}

	@PutMapping("/updatelabel")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDto labeldto, @RequestHeader("email") String email, @RequestParam("labelId") long labelId){
		
		boolean result = labelService.updateLabel(labeldto, email, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is updated", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Error! label is not updated", 400));
	}

	@DeleteMapping("/deletelabel")
	public ResponseEntity<Response> deleteLabel(@RequestParam("email") String email, @RequestHeader("labelId") long labelId){

		boolean result = labelService.deleteLabel(email, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is deleted", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Error! label is not deleted", 400));
	}
		
	@GetMapping("/alllabel")
	public ResponseEntity<Response> getAllLabel(@RequestHeader("email") String email){
		List<LabelModel> labelList = labelService.getAllLabel(email);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All labels of user",200, labelList));
	}
	
	@PutMapping("/maptonote")
	public ResponseEntity<Response> mapToNote(@RequestBody LabelDto labeldto,@RequestHeader("email") String email,
											  @RequestParam("noteid") long noteid){
		LabelModel result = labelService.mapToNote(labeldto, noteid, email);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is mapped to note", 200));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("The label you are trying to map is already exist!!!", 400));

	}

	@PutMapping("/addLabelsToNote")
	public ResponseEntity<Response> addLabels(@RequestHeader("email") String email, @RequestParam("labelId") long labelId,
											  @RequestParam("noteid") long noteId){
		LabelModel result = labelService.addLabelsToNote(email, labelId, noteId);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("label added", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Something went wrong", 400));
	}


}
