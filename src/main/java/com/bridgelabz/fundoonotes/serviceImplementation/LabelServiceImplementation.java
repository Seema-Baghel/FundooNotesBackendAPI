package com.bridgelabz.fundoonotes.serviceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.LabelModel;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.LabelRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.LabelService;
import com.bridgelabz.fundoonotes.utility.Jwt;
import com.bridgelabz.fundoonotes.utility.Util;

@Service
public class LabelServiceImplementation implements LabelService {

	@Autowired
	private Jwt tokenGenerator;

	@Autowired
	private UserRepository userrepository;

	@Autowired
	private NoteRepository noterepository;

	@Autowired
	private LabelRepository labelrepository;

	@Override
	public ResponseEntity<Response> createLabel(LabelDto labeldto, String token) throws UserNotFoundException {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userrepository.findById(userId);
		if(isUserAvailable != null){
			String labelname = labeldto.getLabelTitle();
			LabelModel label = labelrepository.findByName(labelname);
			if(label == null){
				labelrepository.insertLabelData(labeldto.getLabelTitle(), userId);
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Label is Created"));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Already exist in label "));
		}
		throw new UserNotFoundException("Error! No User found");	
	}

	@Override
	public ResponseEntity<Response> updateLabel(LabelDto labeldto, String token, long labelId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userrepository.findById(userId);
		if (user != null) {
			LabelModel label = labelrepository.findById(labelId, userId);
			if(label != null) {
				label.setLabelTitle(labeldto.getLabelTitle());
				labelrepository.update(label.getLabelTitle(), userId);
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Label is updated"));
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Error! label is not updated"));
	}

	@Override
	public ResponseEntity<Response> deleteLabel(String token, long labelId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel label = userrepository.findById(userId);
		if (label != null){		
			labelrepository.delete(userId, labelId);
			return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Label is deleted"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Error! label is not deleted"));
	}

	@Override
	public List<LabelModel> getAllLabel(String token) throws UserNotFoundException {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userrepository.findById(userId);
		if(user != null){
			List<LabelModel> labeldata = labelrepository.getall(userId);
			return labeldata;
		}
		throw new UserNotFoundException("Error! No User found");	
	}

	@Override
	public ResponseEntity<Response> mapToNote(LabelDto labeldto, long noteId, String token) throws UserNotFoundException {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userrepository.findById(userId);
		if(isUserAvailable != null){
			NoteModel noteInfo = noterepository.findById(noteId);
			if(noteInfo != null){
				String labelTitle = labeldto.getLabelTitle();
				LabelModel label = labelrepository.findByTitle(labelTitle);
				if(label == null){
					LabelModel newLabel = new LabelModel();
					newLabel.setLabelTitle(labeldto.getLabelTitle());
					labelrepository.insertLabelData(newLabel.getLabelTitle(), userId);
					LabelModel labelCreate = labelrepository.findByName(newLabel.getLabelTitle());
					labelrepository.insertdatatomap(noteId, labelCreate.getLabelId());
					return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Label is mapped to note"));
				}else{
					Object map = labelrepository.findoneByLabelIdAndNoteId(label.getLabelId(), noteId);
					if (map == null) 
						labelrepository.insertdatatomap(noteId, label.getLabelId());
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "The label you are trying to map is already exist!!!"));
				}
			}
		}
		throw new UserNotFoundException("Error! No User found");	
	}
	
	@Override
	public ResponseEntity<Response> addLabelsToNote(String token, long labelid, long noteId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userrepository.findById(userId);
		if(user != null){
			NoteModel note = noterepository.findById(noteId);
			if(note != null){
				LabelModel isLabelAvailable = labelrepository.findById(labelid, userId);
				if(isLabelAvailable != null){
					Object label = labelrepository.findoneByLabelIdAndNoteId(isLabelAvailable.getLabelId(), noteId);
					if(label == null)
						labelrepository.insertdatatomap(noteId, isLabelAvailable.getLabelId());
					return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "label added"));
				}
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Something went wrong"));	}
	}
