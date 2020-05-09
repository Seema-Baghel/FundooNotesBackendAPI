package com.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundoonotes.dto.LabelDto;
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

	@PostMapping("/updatelabel")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDto labeldto, @RequestHeader("email") String email, @RequestParam("labelId") long labelId){
		
		boolean result = labelService.updateLabel(labeldto, email, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is updated", 200));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Error! label is not updated", 400));
	}

	@PostMapping("/deletelabel")
	public ResponseEntity<Response> deleteLabel(@RequestParam("email") String email, @RequestHeader("labelId") long labelId){

		boolean result = labelService.deleteLabel(email, labelId);
		if(result)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Label is deleted", 200));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Error! label is not deleted", 400));
	}

		
}
