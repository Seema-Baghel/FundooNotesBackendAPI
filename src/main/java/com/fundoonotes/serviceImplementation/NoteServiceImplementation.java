package com.fundoonotes.serviceImplementation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.fundoonotes.dto.NoteDto;
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
		Optional<NoteModel> note = noteRepository.findBynoteId(noteId);
		note.orElseThrow(() -> new NoteException(environment.getProperty("Note doesn't exit")));
		String remender = reminder.toLowerCase();
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);
		LocalDate nextWeek = today.plusWeeks(1);
		String[] reminderOptions = { "today", "tomorrow", "nextweek" };
		for (@SuppressWarnings("unused")
		String string : reminderOptions) {
			if (remender.equals("today")) {
				note.get().setReminder(today);
				noteRepository.save(note.get());
				return new Response("Reminder set for today",200);
			} else if (remender.equals("tomorrow")) {
				note.get().setReminder(tomorrow);
				noteRepository.save(note.get());
				return new Response("Reminder set for tomorrow",200);
			} else if (remender.equals("nextweek")) {
				note.get().setReminder(nextWeek);
				noteRepository.save(note.get());
				return new Response("Reminder set for next week",200);
			} else {
				throw new NoteException("please enter valid reminder day- { Today, Tomorrow, NextWeek }");
			}
		}
		return new Response(environment.getProperty("No reminder set"), 400, reminderOptions);
	}
}