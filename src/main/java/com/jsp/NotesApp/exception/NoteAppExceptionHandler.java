package com.jsp.NotesApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jsp.NotesApp.dto.ResponseStructure;

@ControllerAdvice
public class NoteAppExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	public ResponseEntity<ResponseStructure<String>> INFEhandler(IdNotFoundException exception) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(exception.getMessage());  // Use actual exception message
		structure.setMessage("Resource Not Found"); // Or a generic message
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ResponseStructure<String>> INFEhandler(IdNotFoundException exception) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(exception.getMessage());  // Use actual exception message
		structure.setMessage("Resource Not Found"); // Or a generic message
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	}

}
