package com.fundoonotes.serviceImplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fundoonotes.exception.CollaboratorException;
import com.fundoonotes.model.CollaboratorModel;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.repository.CollaboratorRepository;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.repository.UserRepository;
import com.fundoonotes.responses.EmailObject;
import com.fundoonotes.service.CollaboratorService;
import com.fundoonotes.utility.Jwt;
import com.fundoonotes.utility.RabbitMQSender;
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
	private RabbitMQSender rabbitMQSender;
	
	@Autowired
	private RestTemplate restTemplate;

	private String redisKey = "Key";
	
	@Override
	public CollaboratorModel addCollaborator(String email, long noteId) {
		
			NoteModel note = noteRepository.findById(noteId);
			if(note != null) {
				CollaboratorModel collaboratorDB = collaboratorRepository.findOneByEmail(email,noteId);
				if (collaboratorDB == null) {
					CollaboratorModel collaboratorModel = new CollaboratorModel();
					collaboratorModel.setEmail(email);
					collaboratorModel.setNote(note);
					collaboratorRepository.addCollaborator(collaboratorModel.getEmail(),noteId);
					if(rabbitMQSender.send(new EmailObject(email,"Click the Link...","Colaboration done!"+noteId)));
					return collaboratorModel;
			}
		}
		throw new CollaboratorException("No user Found");
	}
	
	@Override
    public CollaboratorModel mapCollaboratorToNote(String token, long collabId, long noteid) {
        long userId = jwtGenerator.parseJwtToken(token);
        UserModel user = userRepository.findById(userId);
        if(user != null){
            NoteModel note = noteRepository.findById(noteid);
            if(note != null){
                CollaboratorModel isCollabAvailable = collaboratorRepository.findById(collabId, noteid);
                if(isCollabAvailable != null){
                    Object collaborator = collaboratorRepository.findOneByCollabIdAndNoteId(isCollabAvailable.getId(), noteid);
                    if(collaborator == null){
                    	collaboratorRepository.insertdatatomap(noteid, isCollabAvailable.getId());
                    }
                    return isCollabAvailable;
                }
            }
        }
        throw new CollaboratorException("Sorry! User is UnAvailable");
    }
	
	@Override
	public Optional<CollaboratorModel> deleteCollaborator(long collaboratorId, String token, long noteId) {
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
	public List<CollaboratorModel> getNoteCollaborators(String token, long noteId) {
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
