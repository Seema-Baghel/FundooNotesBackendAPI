package com.fundoonotes.serviceImplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fundoonotes.dto.CollaboratorDto;
import com.fundoonotes.exception.CollaboratorException;
import com.fundoonotes.exception.NoteException;
import com.fundoonotes.model.CollaboratorModel;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.repository.CollaboratorRepository;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.repository.UserRepository;
import com.fundoonotes.service.CollaboratorService;
import com.fundoonotes.utility.Jwt;
import com.fundoonotes.utility.RedisTempl;

@Service
public class CollaboratorServiceImplementation implements CollaboratorService {
	
	@Autowired
	private Jwt jwtGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private CollaboratorRepository collaboratorRepository;

	@Autowired
	private RedisTempl redis;

	@Autowired
	private RestTemplate restTemplate;

	private String redisKey = "Key";
	
	@Override
	public CollaboratorModel addCollaborator(CollaboratorDto collaboratorDto, String email, long noteId) {
		String token = redis.getMap(redisKey, email);
		long userId = jwtGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null) {
			CollaboratorModel collaborator = new CollaboratorModel();
			collaborator.setEmail(collaboratorDto.getEmail());
			NoteModel note = noteRepository.findById(noteId);
			CollaboratorModel collaboratorDB = collaboratorRepository.findOneByEmail(collaboratorDto.getEmail(),noteId);
			if (note != null && collaboratorDB == null) {
				BeanUtils.copyProperties(collaboratorDto, collaborator);
				collaborator.setNote(note);
				collaboratorRepository.addCollaborator(collaborator.getId(), collaborator.getEmail(), noteId);
				return collaborator;
			}
		}
		throw new CollaboratorException("No user Found");
	}

	@Override
	public Optional<CollaboratorModel> deleteCollaborator(Long collaboratorId, String email, long noteId) {
		String token = redis.getMap(redisKey, email);
		long userId = jwtGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null) {
			NoteModel note = noteRepository.findById(noteId);
			if (note != null) {
				Optional<CollaboratorModel> collaborator = collaboratorRepository.findById(collaboratorId);
				if (collaborator != null) {
					collaboratorRepository.deleteCollaborator(collaboratorId, noteId);
					return collaborator;
				}
			}
		}
		throw new CollaboratorException("No user Found");
	}

	@Override
	public List<CollaboratorModel> getNoteCollaborators(String email, long noteId) {
		String token = redis.getMap(redisKey, email);
		long userId = jwtGenerator.parseJwtToken(token);
		if (userId != 0) {
			List<NoteModel> note = noteRepository.searchAllNotesByNoteId(userId, noteId);
			if (note != null) {
				return collaboratorRepository.getAllNoteCollaborators(noteId);
			}
		}
		throw new CollaboratorException("No user Found");
	}
}
