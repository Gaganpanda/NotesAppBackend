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

	// Save Note with shareUrl
	public ResponseEntity<ResponseStructure<Note>> saveNote(Note note, int user_id) {
		ResponseStructure<Note> structure = new ResponseStructure<>();
		Optional<User> recUser = userDao.findById(user_id);
		if (recUser.isPresent()) {
			User u = recUser.get();
			u.getNotes().add(note);
			note.setUser(u);

			// Generate unique shareUrl
			note.setShareUrl(UUID.randomUUID().toString());

			userDao.updateUser(u);
			noteDao.saveNote(note);

			structure.setData(note);
			structure.setMessage("Note Added Successfully");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<>(structure, HttpStatus.CREATED);
		}
		throw new IdNotFoundException();
	}

	// Update note (keep shareUrl)
	public ResponseEntity<ResponseStructure<Note>> updateNote(Note note, int user_id) {
		ResponseStructure<Note> structure = new ResponseStructure<>();
		Optional<User> recUser = userDao.findById(user_id);
		if (recUser.isPresent()) {
			// Keep the existing shareUrl if present
			if (note.getShareUrl() == null || note.getShareUrl().isEmpty()) {
				note.setShareUrl(UUID.randomUUID().toString());
			}
			note.setUser(recUser.get());
			noteDao.updateNote(note);
			structure.setData(note);
			structure.setMessage("Note Updated Successfully");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<>(structure, HttpStatus.ACCEPTED);
		}
		throw new IdNotFoundException();
	}

	// Find by shareUrl
	public ResponseEntity<ResponseStructure<Note>> findByShareUrl(String shareUrl) {
		ResponseStructure<Note> structure = new ResponseStructure<>();
		Optional<Note> recNote = noteDao.findByShareUrl(shareUrl);
		if (recNote.isPresent()) {
			structure.setData(recNote.get());
			structure.setMessage("Note Found via Share URL");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(structure, HttpStatus.OK);
		}
		throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<Note>> findById(int id) {
		ResponseStructure<Note> structure = new ResponseStructure<>();
		Optional<Note> recNote = noteDao.findById(id);
		if (recNote.isPresent()) {
			structure.setData(recNote.get());
			structure.setMessage("Note Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(structure, HttpStatus.OK);
		}
		throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<String>> deleteNote(int id) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		Optional<Note> recNote = noteDao.findById(id);
		if (recNote.isPresent()) {
			noteDao.deleteNote(id);
			structure.setData("Note Deleted");
			structure.setMessage("Note Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(structure, HttpStatus.OK);
		}
		throw new IdNotFoundException();
	}

	public ResponseEntity<ResponseStructure<List<Note>>> findNotesByUserId(int id) {
		ResponseStructure<List<Note>> structure = new ResponseStructure<>();
		structure.setData(noteDao.findNotesByUserId(id));
		structure.setMessage("Notes found for User ID");
		structure.setStatusCode(HttpStatus.OK.value());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}
