package com.fundoonotes.serviceImplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fundoonotes.dto.LabelDto;
import com.fundoonotes.model.LabelModel;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.repository.LabelRepository;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.repository.UserRepository;
import com.fundoonotes.service.LabelService;
import com.fundoonotes.utility.Jwt;

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
	private RedisTemplate<String, Object> redis;
	
	@Override
	public int createLabel(LabelDto labeldto, String email) {
//		String token = redis.getMap(redis, email);
//		long userId = tokenGenerator.parseJwtToken(token);
//		Optional<UserModel> isUserAvailable = userrepository.findById(userId);
//		if(isUserAvailable != null){
//			String labelname = labeldto.getLabelTitle();
//			LabelModel label = labelrepository.findByName(labelname);
//			if(label == null){
//				return labelrepository.insertLabelData(labeldto.getLabelTitle(), userId);
//			}
//		}
		return 0;
	}

	@Override
	public boolean updateLabel(LabelDto labeldto, String email, long labelId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteLabel(String email, long labelId) {
		// TODO Auto-generated method stub
		return false;
	}

}
