package com.bridgelabz.fundoonotes.serviceImplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.exception.CollaboratorException;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.CollaboratorModel;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.CollaboratorRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.responses.EmailObject;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.CollaboratorService;
import com.bridgelabz.fundoonotes.utility.Jwt;
import com.bridgelabz.fundoonotes.utility.RabbitMQSender;
import com.bridgelabz.fundoonotes.utility.Util;

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
	private RabbitMQSender rabbitMQSender;
	
	@Override
	public ResponseEntity<Response> addCollaborator(String token, String email, long noteId) {
		long userId = jwtGenerator.parseJwtToken(token);
        UserModel user = userRepository.findById(userId);
        if(user != null){
			NoteModel note = noteRepository.findById(noteId);
			if(note != null) {
				CollaboratorModel collaboratorDB = collaboratorRepository.findOneByEmail(email,noteId);
				if (collaboratorDB == null) {
					CollaboratorModel collaboratorModel = new CollaboratorModel();
					collaboratorModel.setEmail(email);
					collaboratorModel.setNote(note);
					collaboratorRepository.addCollaborator(collaboratorModel.getEmail(),noteId);
					if(rabbitMQSender.send(new EmailObject(email,"Click the Link...","Colaboration done!"+noteId)));
					return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Added collabrator sucessfully!!!"));
				}
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Sorry! No collaborator added"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "User not found"));
    } 
	
	@Override
    public ResponseEntity<Response> mapCollaboratorToNote(String token, long collabId, long noteid) {
        long userId = jwtGenerator.parseJwtToken(token);
        UserModel user = userRepository.findById(userId);
        if(user != null){
            NoteModel note = noteRepository.findById(noteid);
            if(note != null){
                CollaboratorModel isCollabAvailable = collaboratorRepository.findById(collabId, noteid);
                if(isCollabAvailable != null){
                    Object collaborator = collaboratorRepository.findOneByCollabIdAndNoteId(isCollabAvailable.getId(), noteid);
                    if(collaborator == null)
                    	collaboratorRepository.insertdatatomap(noteid, isCollabAvailable.getId());
                    return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "collaborator added"));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Something went wrong"));
    }
	
	@Override
	public ResponseEntity<Response> deleteCollaborator(long collaboratorId, String token, long noteId) throws UserNotFoundException {
		long userId = jwtGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null) {
			NoteModel note = noteRepository.findById(noteId);
			if (note != null) {
				Optional<CollaboratorModel> collaborator = collaboratorRepository.findById(collaboratorId);
				if (collaborator != null) {
					collaboratorRepository.deleteCollaborator(collaboratorId, noteId);
					return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE,"Deleted collaborator sucessfully!!!"));
				}
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Sorry! Cannot delete "));
			}
		}
		throw new UserNotFoundException("No user found");
	}

	@Override
	public List<CollaboratorModel> getNoteCollaborators(String token, long noteId) throws UserNotFoundException {
		long userId = jwtGenerator.parseJwtToken(token);
		if (userId != 0) {
			List<NoteModel> note = noteRepository.searchAllNotesByNoteId(userId, noteId);
			if (note != null) {
				return collaboratorRepository.getAllNoteCollaborators(noteId);
			}
		}
		throw new UserNotFoundException("No user found");
	}
}
