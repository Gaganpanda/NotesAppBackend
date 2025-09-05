package com.jsp.NotesApp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jsp.NotesApp.dao.NoteDAO;
import com.jsp.NotesApp.dao.UserDAO;
import com.jsp.NotesApp.dto.Note;
import com.jsp.NotesApp.dto.ResponseStructure;
import com.jsp.NotesApp.dto.User;
import com.jsp.NotesApp.exception.IdNotFoundException;

@Service
public class NoteService {
	@Autowired
	private UserDAO userDao;

	@Autowired
	private NoteDAO noteDao;

	public ResponseEntity<ResponseStructure<Note>> saveNote(Note note, int user_id) {
		// Debug log to check incoming content
		System.out.println("Debug: Received Note content: [" + note.getContent() + "]");

		if (note.getContent() == null || note.getContent().trim().isEmpty()) {
			throw new IllegalArgumentException("Note content cannot be blank");
		}

		Optional<User> userOpt = userDao.findById(user_id);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			note.setUser(user);

			note.setShareUrl(UUID.randomUUID().toString());

			Note savedNote = noteDao.saveNote(note);

			ResponseStructure<Note> structure = new ResponseStructure<>();
			structure.setData(savedNote);
			structure.setMessage("Note Added Successfully");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<>(structure, HttpStatus.CREATED);
		}
		throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<Note>> updateNote(Note note, int user_id) {
		if (note.getContent() == null || note.getContent().trim().isEmpty()) {
			throw new IllegalArgumentException("Note content cannot be blank");
		}

		Optional<User> userOpt = userDao.findById(user_id);
		if (userOpt.isPresent()) {
			if (note.getShareUrl() == null || note.getShareUrl().trim().isEmpty()) {
				note.setShareUrl(UUID.randomUUID().toString());
			}
			note.setUser(userOpt.get());
			Note updatedNote = noteDao.updateNote(note);

			ResponseStructure<Note> structure = new ResponseStructure<>();
			structure.setData(updatedNote);
			structure.setMessage("Note Updated Successfully");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<>(structure, HttpStatus.ACCEPTED);
		}

		throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<Note>> findByShareUrl(String shareUrl) {
		Optional<Note> noteOpt = noteDao.findByShareUrl(shareUrl);
		if (noteOpt.isPresent()) {
			ResponseStructure<Note> structure = new ResponseStructure<>();
			structure.setData(noteOpt.get());
			structure.setMessage("Note Found via Share URL");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(structure, HttpStatus.OK);
		}
		throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<Note>> findById(int id) {
		Optional<Note> noteOpt = noteDao.findById(id);
		if (noteOpt.isPresent()) {
			ResponseStructure<Note> structure = new ResponseStructure<>();
			structure.setData(noteOpt.get());
			structure.setMessage("Note Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(structure, HttpStatus.OK);
		}
		throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<String>> deleteNote(int id) {
		boolean deleted = noteDao.deleteNote(id);
		if (deleted) {
			ResponseStructure<String> structure = new ResponseStructure<>();
			structure.setData("Note Deleted");
			structure.setMessage("Note Deleted Successfully");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(structure, HttpStatus.OK);
		}
		throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<List<Note>>> findNotesByUserId(int user_id) {
		List<Note> notes = noteDao.findNotesByUserId(user_id);
		ResponseStructure<List<Note>> structure = new ResponseStructure<>();
		structure.setData(notes);
		structure.setMessage("Notes found for User ID");
		structure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}
