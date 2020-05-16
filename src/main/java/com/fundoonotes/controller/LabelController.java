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
		
	/*
	 * API to add label
	 * 
	 * @header token
	 */
	
	@PostMapping("/addlabel")
	public ResponseEntity<Response> createLabel(@RequestBody LabelDto labeldto, @RequestHeader("token") String token) {
		int result = labelService.createLabel(labeldto, token);
		if (result != 0)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Label is Created"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Already exist in label "));
	}
	
	/*
	 * API to update label
	 * 
	 * @header token
	 * @param labelid
	 */

	@PutMapping("/updatelabel")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDto labeldto,@RequestHeader("token") String token, @RequestParam("labelId") long labelId){
		
		boolean result = labelService.updateLabel(labeldto, token, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Label is updated"));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Error! label is not updated"));
	}

	/*
	 * API to delete label
	 * 
	 * @header token
	 * @param labelid
	 */
	
	@DeleteMapping("/deletelabel")
	public ResponseEntity<Response> deleteLabel(@RequestHeader("token") String token, @RequestParam("labelId") long labelId){

		boolean result = labelService.deleteLabel(token, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Label is deleted"));
		return ResponseEntity.status(HttpStatus.OK).body(new Response(400, "Error! label is not deleted"));
	}
	
	/*
	 * API to get all label
	 */
		
	@GetMapping("/alllabel")
	public ResponseEntity<Response> getAllLabel(@RequestHeader("token") String token){
		List<LabelModel> labelList = labelService.getAllLabel(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All labels of user",200, labelList));
	}
	
	/*
	 * API to map label to note
	 * 
	 * @header token
	 * @param labelid
	 * @param noteid
	 */
	
	@PutMapping("/maptonote")
	public ResponseEntity<Response> mapToNote(@RequestBody LabelDto labeldto,@RequestHeader("token") String token,
											  @RequestParam("noteid") long noteid){
		LabelModel result = labelService.mapToNote(labeldto, noteid, token);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "Label is mapped to note"));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(400, "The label you are trying to map is already exist!!!"));
	}
	
	/*
	 * API to add some note to a label
	 * 
	 * @header token
	 * @param labelid
	 * @param noteid
	 */
	
	@PutMapping("/addLabelsToNote")
	public ResponseEntity<Response> addLabels(@RequestHeader("token") String token, @RequestParam("labelId") long labelId,
											  @RequestParam("noteid") long noteId){
		LabelModel result = labelService.addLabelsToNote(token, labelId, noteId);
		if(result != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response(200, "label added"));
		return ResponseEntity.status(HttpStatus.OK).body(new Response(400, "Something went wrong"));
	}


}
