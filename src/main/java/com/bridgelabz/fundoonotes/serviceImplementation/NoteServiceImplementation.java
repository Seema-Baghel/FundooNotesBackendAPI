package com.bridgelabz.fundoonotes.serviceImplementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.dto.ReminderDateTimeDto;
import com.bridgelabz.fundoonotes.exception.NoteException;
import com.bridgelabz.fundoonotes.exception.UserNotFoundException;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.responses.EmailObject;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.Jwt;
import com.bridgelabz.fundoonotes.utility.RabbitMQSender;
import com.bridgelabz.fundoonotes.utility.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoteServiceImplementation implements NoteService {

	@Autowired
	private Jwt tokenGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;
	
	@Autowired
	private RabbitMQSender rabbitMQSender;
	
	@Autowired
	List<NoteDto> listOfNotes;
	 
	@Override
	public ResponseEntity<Response> createNote(NoteDto noteDto , String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(userId);
		if (user != null) {
			ModelMapper mapper = new ModelMapper();
			NoteModel note =mapper.map(noteDto, NoteModel.class);
			note.setCreatedBy(user);
			note.setCreatedDate(LocalDateTime.now());
			note.setUpdatedDate(LocalDateTime.now());
			noteRepository.save(note);
			return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Note is created successfully"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Note not created "));
	}

	@Override
	public ResponseEntity<Response> updateNote(NoteDto noteDto, long noteId, String token) {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(id);
		if (user != null) {
			NoteModel note = noteRepository.findById(noteId);
			note.setDescription(noteDto.getDescription());
			note.setTitle(noteDto.getTitle());
			note.setUpdatedDate(LocalDateTime.now());
			noteRepository.updateData(note.getDescription(), note.getTitle(), note.getUpdatedDate(), id, id);
			return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Note is update successfully"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Something went wrong"));
	}
	
	@Override
	public ResponseEntity<Response> addColor(String token, long noteId, String color) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
			if (note != null) {
				noteRepository.updateColor(userId, noteId, color);
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Color is added"));
			}
			return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Error! color is not added"));
		}
		throw new NoteException("No Data Found");
	}

	@Override
	public ResponseEntity<Response> deleteNote(String token, long noteId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel note = userRepository.findById(userId);
		if (note != null){		
			noteRepository.deleteNote(userId, noteId);
			return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Deleted Succussfully"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE, "Error! note can't be deleted"));
	}
	
	@Override
	public List<NoteModel> getAllNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			List<NoteModel> notes = noteRepository.getAll(userId);
			return notes;
		}
		throw new NoteException("No Notes Found");
	}

	@Override
	public List<NoteModel> searchByTitle(String token, String noteTitle) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			List<NoteModel> notes = noteRepository.getAll(userId);
			if (!notes.isEmpty()) {
				List<NoteModel> list = notes.stream().filter(note ->
											note.getTitle().contains(noteTitle))
											.collect(Collectors.toList());
				return list;
			}
			throw new NoteException("No note Found");
		}
		throw new NoteException("No user Found");
	}
	
	@Override
	public List<NoteModel> searchByDecription(String token, String noteDescription) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			List<NoteModel> notes = noteRepository.getAll(userId);
			if (!notes.isEmpty()) {
				List<NoteModel> list = notes.stream().filter(note ->
											note.getDescription().contains(noteDescription))
											.collect(Collectors.toList());
				return list;
			}
			throw new NoteException("No note Found");
		}
		throw new NoteException("No user Found");
	}
	

	@Override
	public ResponseEntity<Response> setReminder(String token, ReminderDateTimeDto reminderDateTimeDto, long noteId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable == null)
			throw new NoteException("No user Found");
		Optional<NoteModel> note = noteRepository.findBynoteId(noteId);
		LocalDateTime reminderDateTime = LocalDateTime.of(reminderDateTimeDto.getYear(), reminderDateTimeDto.getMonth(),
														  reminderDateTimeDto.getDay(), reminderDateTimeDto.getHour(),
														  reminderDateTimeDto.getMinute());
		LocalDateTime date = reminderDateTime;
		note.get().setReminder(date);
		noteRepository.save(note.get());
		return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Reminder set successfully"));
	}
	
	@Override
	public ResponseEntity<Response> unsetReminder(long noteId, String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable == null)
			throw new NoteException("No user Found");
		NoteModel note = noteRepository.findById(noteId);
		note.setReminder(null);
		noteRepository.save(note);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Reminder unset successfully"));
	}

	@Override
	public ResponseEntity<Response> isPinnedNote(String token, long noteId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
				if (!note.isPinned()) {
					note.setPinned(true);
					note.setUpdatedDate(LocalDateTime.now());
					noteRepository.save(note);
					return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Note pinned"));
				}
				note.setPinned(false);
				note.setUpdatedDate(LocalDateTime.now());
				noteRepository.save(note);
				return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Util.OK_RESPONSE_CODE, "Note unpinned"));
		}
		throw new NoteException("Sorry! something went wrong");
	}

	@Override
	public List<NoteModel> allPinnedNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null){
			List<NoteModel> notes = noteRepository.getallpinned(userId);
			return notes;
		}
		throw new NoteException("Sorry! User not found");
	}

	@Override
	public List<NoteModel> allUnpinnedNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null){
			List<NoteModel> notes = noteRepository.getallunpinned(userId);
			return notes;
		}
		throw new NoteException("Sorry! User not found");
	}

	@Override
	public ResponseEntity<Response> isArchivedNote(String token, long noteId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
			if (!note.isArchived()) {
				note.setArchived(true);
				note.setUpdatedDate(LocalDateTime.now());
				noteRepository.save(note);
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Note archieved"));
			}
			note.setArchived(false);
			note.setUpdatedDate(LocalDateTime.now());
			noteRepository.save(note);
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Util.OK_RESPONSE_CODE, "Note unarchived"));
		}
		throw new NoteException("Sorry! User not found");
	}

	@Override
	public List<NoteModel> allArchived(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null){
			List<NoteModel> notes = noteRepository.getallarchived(userId);
			return notes;
		}
		throw new NoteException("Sorry! User not found");
	}

	@Override
	public List<NoteModel> allUnarchived(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null){
			List<NoteModel> notes = noteRepository.getallunarchived(userId);
			return notes;
		}
		throw new NoteException("Sorry! User not found");
	}

	@Override
	public ResponseEntity<Response> trashNote(String token, long noteId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
				if (!note.isTrashed()) {
					note.setTrashed(true);
					note.setArchived(false);
					note.setPinned(false);
					note.setReminder(null);
					note.setUpdatedDate(LocalDateTime.now());
					noteRepository.save(note);
					return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Note trashed"));
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE,"Error! Note is not trashed"));
			}
			throw new NoteException("Sorry! User not found");
		}

	@Override
	public List<NoteModel> allTrashedNotes(String token) {
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if(isUserAvailable != null){
			List<NoteModel> notes = noteRepository.getalltrashed(userId);
			return notes;
		}
		throw new NoteException("Sorry! User not found");
	}

	@Override
	public ResponseEntity<Response> restoreNote(String token, long noteId) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
			if (note.isTrashed()) {
				note.setTrashed(false);
				note.setUpdatedDate(LocalDateTime.now());
				noteRepository.save(note);
				return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE,"Note restored"));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Util.BAD_REQUEST_RESPONSE_CODE,"Error in Restoring note!"));
		}
		throw new NoteException("Sorry! User not found");
	}

	@Override
	public ResponseEntity<Object> sortByTitle() {
		ModelMapper mapper = new ModelMapper();
		noteRepository.findByOrderByTitleAsc().stream().forEach(note -> {
			listOfNotes.add(mapper.map(note, NoteDto.class));
		});
		return new ResponseEntity<Object>(listOfNotes, HttpStatus.OK);
	}
	

	@Override
	public ResponseEntity<Object> sortByDescription() {
		ModelMapper mapper = new ModelMapper();
		noteRepository.findByOrderByDescriptionAsc().stream().forEach(note -> {
			listOfNotes.add(mapper.map(note, NoteDto.class));
		});
		
		return new ResponseEntity<Object>(listOfNotes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> addCollaborator(String token, String email, long noteId) throws UserNotFoundException {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> MainUser = userRepository.findUserById(userId);
		Optional<UserModel> user = userRepository.findByEmailId(email);
		if (!user.isPresent())
			throw new UserNotFoundException("No user exist");
		NoteModel note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		if (note == null)
			throw new NoteException("No user exist");
		if (user.get().getCollaboratedNotes().contains(note))
			throw new NoteException("Note is already collaborated");
		user.get().getCollaboratedNotes().add(note);
		note.getCollaborator().add(user.get());
		userRepository.save(user.get());
		noteRepository.save(note);
		EmailObject collabEmail = new EmailObject();
		collabEmail.setEmail(email);
		if(rabbitMQSender.send(new EmailObject(email,"Note collaboration","Note from " + MainUser.get().getEmail() + " collaborated to you\nTitle : " + note.getTitle()
												+ "\nDescription : " + note.getDescription())));
		return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE, "Added collabrator sucessfully!!!"));

	}

	@Override
	public ResponseEntity<Response> deleteCollaboratorInNote(String token, long noteId, String email) throws UserNotFoundException {
		long userId = tokenGenerator.parseJwtToken(token);
		Optional<UserModel> user = userRepository.findUserById(userId);

		if (!user.isPresent())
			throw new UserNotFoundException("No user exist");
		NoteModel note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		if (note == null)
			throw new NoteException( "No note exist");
		user.get().getCollaboratedNotes().remove(note);
		note.getCollaborator().remove(user.get());
		userRepository.save(user.get());
		noteRepository.save(note);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(Util.OK_RESPONSE_CODE,"Deleted collaborator successfully!!!"));
	}

	
	
}