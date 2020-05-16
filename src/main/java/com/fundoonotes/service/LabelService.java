package com.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fundoonotes.dto.LabelDto;
import com.fundoonotes.model.LabelModel;

@Component
public interface LabelService {

	int createLabel(LabelDto labeldto, String token);

	boolean updateLabel(LabelDto labeldto, String token, long labelId);

	boolean deleteLabel(String token, long labelId);

	List<LabelModel> getAllLabel(String token);

	LabelModel mapToNote(LabelDto labeldto, long noteid, String token);

	LabelModel addLabelsToNote(String token, long labelId, long noteId);

}
