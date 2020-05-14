package com.fundoonotes.serviceImplementation;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fundoonotes.dto.LabelDto;
import com.fundoonotes.exception.NoteException;
import com.fundoonotes.model.LabelModel;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.repository.LabelRepository;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.repository.UserRepository;
import com.fundoonotes.service.LabelService;
import com.fundoonotes.utility.Jwt;
import com.fundoonotes.utility.RedisTempl;

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

	@Autowired
	private RedisTempl redis;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private String redisKey = "Key";

	@Override
	public int createLabel(LabelDto labeldto, String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userrepository.findById(userId);
		if(isUserAvailable != null){
			String labelname = labeldto.getLabelTitle();
			LabelModel label = labelrepository.findByName(labelname);
			if(label == null){
				return labelrepository.insertLabelData(labeldto.getLabelTitle(), userId);
			}
		}
		throw new NoteException("Error! No User found");	
	}

	@Override
	public boolean updateLabel(LabelDto labeldto, String token, long labelId) {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userrepository.findById(id);
		if (user != null) {
			LabelModel label = labelrepository.findById(labelId, id);
			if(label != null) {
				label.setLabelTitle(labeldto.getLabelTitle());
				labelrepository.update(label.getLabelTitle(), id);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteLabel(String token, long labelId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel label = userrepository.findById(userId);
		if (label != null){		
			labelrepository.delete(userId, labelId);
			return true;
		}
		return false;
	}

	@Override
	public List<LabelModel> getAllLabel(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userrepository.findById(userId);
		if(user != null){
			List<LabelModel> labeldata = labelrepository.getall(userId);
			return labeldata;
		}
		throw new NoteException("Error! No User found");	
	}

	@Override
	public LabelModel mapToNote(LabelDto labeldto, long noteid, String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userrepository.findById(userId);
		if(isUserAvailable != null){
			NoteModel noteInfo = noterepository.findById(noteid);
			if(noteInfo != null){
				String labelTitle = labeldto.getLabelTitle();
				LabelModel label = labelrepository.findByTitle(labelTitle);
				if(label == null){
					LabelModel newLabel = new LabelModel();
					newLabel.setLabelTitle(labeldto.getLabelTitle());
					labelrepository.insertLabelData(newLabel.getLabelTitle(), userId);
					LabelModel labelCreate = labelrepository.findByName(newLabel.getLabelTitle());
					labelrepository.insertdatatomap(noteid, labelCreate.getLabelId());
					return labelCreate;
				}else{
					Object map = labelrepository.findoneByLabelIdAndNoteId(label.getLabelId(), noteid);
					if (map == null) {
						labelrepository.insertdatatomap(noteid, label.getLabelId());
					}
					return label;
				}
			}
		}
		throw new NoteException("Error! No User found");	
	}

	@Override
	public LabelModel addLabelsToNote(String token, long labelid, long noteid) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userrepository.findById(userId);
		if(user != null){
			NoteModel note = noterepository.findById(noteid);
			if(note != null){
				LabelModel isLabelAvailable = labelrepository.findById(labelid, userId);
				if(isLabelAvailable != null){
					Object label = labelrepository.findoneByLabelIdAndNoteId(isLabelAvailable.getLabelId(), noteid);
					if(label == null){
						labelrepository.insertdatatomap(noteid, isLabelAvailable.getLabelId());
					}
					return isLabelAvailable;
				}
			}
		}
		throw new NoteException("Error! No User found");	
	}
}
