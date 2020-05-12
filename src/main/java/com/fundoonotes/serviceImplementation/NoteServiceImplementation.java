package com.fundoonotes.serviceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fundoonotes.dto.NoteDto;
import com.fundoonotes.exception.NoteException;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.model.UserModel;
import com.fundoonotes.repository.NoteRepository;
import com.fundoonotes.repository.UserRepository;
import com.fundoonotes.responses.Response;
import com.fundoonotes.service.NoteService;
import com.fundoonotes.utility.Jwt;
import com.fundoonotes.utility.RedisTempl;

@Service
public class NoteServiceImplementation implements NoteService {

	@Autowired
	private Jwt tokenGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private RedisTempl redis;

	@Autowired
	private RestTemplate restTemplate;

	private String redisKey = "Key";

	@Override
	public NoteModel createNote(NoteDto noteDto , String token) {
//		String token = redis.getMap(redisKey, email);
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(id);
		if (user != null) {
			ModelMapper mapper = new ModelMapper();
			NoteModel note =mapper.map(noteDto, NoteModel.class);
			note.setCreatedBy(user);
			note.setCreatedDate();
			note.setUpdatedDate();
			noteRepository.insertData(note.getDescription(), note.getCreatedDate(), note.getTitle(), note.getUpdatedDate(),
					note.getNoteColor(), note.getCreatedBy().getId());
			return note;
		}
		throw new NoteException("Error! No note created");
	}

	@Override
	public boolean updateNote(NoteDto noteDto, long noteId, String token) {
//		String token = redis.getMap(redisKey, email);
		long id = tokenGenerator.parseJwtToken(token);
		UserModel user = userRepository.findById(id);
		if (user != null) {
			ModelMapper mapper = new ModelMapper();
			NoteModel note = noteRepository.findById(noteId);
			note.setDescription(noteDto.getDescription());
			note.setTitle(noteDto.getTitle());
			note.setUpdatedDate();
			noteRepository.updateData(note.getDescription(), note.getTitle(), note.getUpdatedDate(), id, id);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addColor(String token, long id, String color) {
//		String token = redis.getMap(redisKey, email);
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
//		String token = redis.getMap(redisKey, email);
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
//		String token = redis.getMap(redisKey, email);
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
//		String token = redis.getMap(redisKey, email);
		long userId = tokenGenerator.parseJwtToken(token);
		Object isUserAvailable = userRepository.findById(userId);
		if (isUserAvailable != null) {
			List<NoteModel> fetchedNotes = noteRepository.searchBy(noteTitle);
			if (!fetchedNotes.isEmpty()) {
				return fetchedNotes;
			}
			throw new NoteException("No note Found");
		}
		throw new NoteException("No user Found");
	}

	@Override
	public Response setReminder(long noteId, String reminder) {
//		NoteModel note = noteRepository.findById(noteId);
//		if(note == null)
//			throw new NoteException("No note found");
//		String reminders = reminder.toLowerCase();
//		LocalDate today = LocalDate.now();
//		LocalDate tomorrow = today.plusDays(1);
//		LocalDate nextWeek = today.plusWeeks(1);
//		switch(reminder) {
//			case "today": note.getId().setReminder(today);
//						  noteRepository.save(note.getId());
//						  return new Response("Reminder set for today",200);
//			case "tomorrow": note.getId().setReminder(tomorrow);
//			  			  noteRepository.save(note.getId());
//			  			  return new Response("Reminder set for tomorrow",200);
//			case "nextWeek": note.getId().setReminder(nextWeek);
//			  				 noteRepository.save(note.getId());
//			  				 return new Response("Reminder set for nextWeek",200);
//			 default: return new Response("Error! enater a valid Reminder",400);
//		}
		throw new NoteException("No user Found");
		
	}
	
}