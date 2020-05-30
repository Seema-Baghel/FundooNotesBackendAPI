package com.bridgelabz.fundoonotes.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.LabelModel;
import com.bridgelabz.fundoonotes.responses.Response;

@Component
public interface LabelService {

	ResponseEntity<Response> createLabel(LabelDto labeldto, String token) throws UserNotFoundException;

	ResponseEntity<Response> updateLabel(LabelDto labeldto, String token, long labelId);

	ResponseEntity<Response> deleteLabel(String token, long labelId);

	List<LabelModel> getAllLabel(String token) throws UserNotFoundException;

	ResponseEntity<Response> mapToNote(LabelDto labeldto, long noteid, String token) throws UserNotFoundException;

	ResponseEntity<Response> addLabelsToNote(String token, long labelId, long noteId);

}
