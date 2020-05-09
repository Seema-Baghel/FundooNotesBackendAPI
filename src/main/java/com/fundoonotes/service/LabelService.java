package com.fundoonotes.service;

import com.fundoonotes.dto.LabelDto;

public interface LabelService {

	int createLabel(LabelDto labeldto, String email);

	boolean updateLabel(LabelDto labeldto, String email, long labelId);

	boolean deleteLabel(String email, long labelId);
}
