package com.fundoonotes.serviceImplementation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fundoonotes.dto.NoteDto;
import com.fundoonotes.dto.ReminderDateTimeDto;
import com.fundoonotes.exception.NoteException;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.repository.UserRepository;
import com.fundoonotes.responses.Response;
import com.fundoonotes.service.NoteService;
import com.fundoonotes.utility.Jwt;

@Service
public class NoteServiceImplementation implements NoteService {

	@Autowired
	private Jwt tokenGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;
	
	@Autowired
	List<NoteDto> listOfNotes;

	@Autowired
	private Environment environment;
	
	 
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
	String date = dateFormat.format(new Date()).toString();
	 
	@Override
	public NoteModel createNote(NoteDto noteDto , String token) {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(id);
		if (user != null) {
			ModelMapper mapper = new ModelMapper();
			NoteModel note =mapper.map(noteDto, NoteModel.class);
			note.setCreatedBy(user);
			note.setCreatedDate(date);
			note.setUpdatedDate(date);
			noteRepository.insertData(note.getDescription(), note.getCreatedDate(), note.getTitle(), note.getUpdatedDate(),
					note.getNoteColor(), note.getCreatedBy().getId());
			return note;
		}
		throw new NoteException("Error! No note created");
	}

	@Override
	public boolean updateNote(NoteDto noteDto, long noteId, String token) {
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(id);
		if (user != null) {
			NoteModel note = noteRepository.findById(noteId);
			note.setDescription(noteDto.getDescription());
			note.setTitle(noteDto.getTitle());
			note.setUpdatedDate(date);
			noteRepository.updateData(note.getDescription(), note.getTitle(), note.getUpdatedDate(), id, id);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addColor(String token, long id, String color) {
		long userid = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userid);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(id);
			if (note != null) {
				noteRepository.updateColor(userid, id, color);
				return true;
			}
			return false;
		}
		throw new NoteException("No Data Found");
	}

	@Override
	public boolean deleteNote(String token, long id) {
		long userId = tokenGenerator.parseJwtToken(token);
		UserModel note = userRepository.findById(userId);
		if (note != null){		
			noteRepository.deleteNote(userId, id);
			return true;
		}
		return false;
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
	public ResponseEntity<String> setReminder(ReminderDateTimeDto reminderDateTimeDto, long id) {
		Optional<NoteModel> note = noteRepository.findBynoteId(id);
		LocalDateTime reminderDateTime = LocalDateTime.of(reminderDateTimeDto.getYear(), reminderDateTimeDto.getMonth(),
														  reminderDateTimeDto.getDay(), reminderDateTimeDto.getHour(),
														  reminderDateTimeDto.getMinute());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy  hh:mm a");
		String formattedDateTime = reminderDateTime.format(formatter);
		note.get().setReminder(formattedDateTime);
		noteRepository.save(note.get());
		return new ResponseEntity<String>(environment.getProperty("Reminder set successfully"), HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<String> unsetReminder(long id) {
		NoteModel note = noteRepository.findById(id);
		note.setReminder(null);
		noteRepository.save(note);
		return new ResponseEntity<String>(environment.getProperty("Reminder removed successfully"), HttpStatus.OK);
	}


	@Override
	public boolean isPinnedNote(String token, long noteId) {
		long userid = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userid);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
				if (!note.isPinned()) {
					note.setPinned(true);
					note.setUpdatedDate(date);
					noteRepository.save(note);
					return true;
				}
				note.setPinned(false);
				note.setUpdatedDate(date);
				noteRepository.save(note);
				return false;
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
	public boolean isArchivedNote(String token, long noteId) {
		long userid = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userid);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
			if (!note.isArchived()) {
				note.setArchived(true);
				note.setUpdatedDate(date);
				noteRepository.save(note);
				return true;
			}
			note.setArchived(false);
			note.setUpdatedDate(date);
			noteRepository.save(note);
			return false;
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
	public boolean trashNote(String token, long noteId) {
		long userid = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userid);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
				if (!note.isTrashed()) {
					note.setTrashed(true);
					note.setArchived(false);
					note.setPinned(false);
					note.setReminder(null);
					note.setUpdatedDate(date);
					noteRepository.save(note);
					return true;
				}
				
				return false;
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
	public boolean restoreNote(String token, long noteId) {
		long userid = tokenGenerator.parseJwtToken(token);
		UserModel isUserAvailable = userRepository.findById(userid);
		if (isUserAvailable != null) {
			NoteModel note = noteRepository.findById(noteId);
			if (note.isTrashed()) {
				note.setTrashed(false);
				note.setUpdatedDate(date);
				noteRepository.save(note);
				return true;
			}
			return false;
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

	
	
}