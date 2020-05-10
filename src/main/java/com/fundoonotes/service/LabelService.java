package com.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fundoonotes.dto.LabelDto;
import com.fundoonotes.model.LabelModel;

@Component
public interface LabelService {

	int createLabel(LabelDto labeldto, String email);

	boolean updateLabel(LabelDto labeldto, String email, long labelId);

	boolean deleteLabel(String email, long labelId);

	List<LabelModel> getAllLabel(String email);

	LabelModel mapToNote(LabelDto labeldto, long noteid, String email);

	LabelModel addLabelsToNote(String token, long labelid, long noteid);
}
