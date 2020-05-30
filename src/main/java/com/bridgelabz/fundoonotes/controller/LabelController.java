package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.LabelModel;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.LabelService;
import com.bridgelabz.fundoonotes.utility.Util;

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
	public ResponseEntity<Response> createLabel(@RequestBody LabelDto labeldto, @RequestHeader("token") String token) throws UserNotFoundException {
		
		return labelService.createLabel(labeldto, token);
	}
	
	/*
	 * API to update label
	 * 
	 * @header token
	 * @param labelid
	 */

	@PutMapping("/updatelabel")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDto labeldto,@RequestHeader("token") String token, @RequestParam("labelId") long labelId){
		
		return labelService.updateLabel(labeldto, token, labelId);
	}

	/*
	 * API to delete label
	 * 
	 * @header token
	 * @param labelid
	 */
	
	@DeleteMapping("/deletelabel")
	public ResponseEntity<Response> deleteLabel(@RequestHeader("token") String token, @RequestParam("labelId") long labelId){

		return labelService.deleteLabel(token, labelId);
	}
	
	/*
	 * API to get all label
	 */
		
	@GetMapping("/alllabel")
	public ResponseEntity<Response> getAllLabel(@RequestHeader("token") String token) throws UserNotFoundException{
		
		List<LabelModel> labelList = labelService.getAllLabel(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All labels of user",Util.OK_RESPONSE_CODE, labelList));
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
									@RequestParam("noteid") long noteid) throws UserNotFoundException{
		
		return labelService.mapToNote(labeldto, noteid, token);
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
		
		return labelService.addLabelsToNote(token, labelId, noteId);
	}


}
