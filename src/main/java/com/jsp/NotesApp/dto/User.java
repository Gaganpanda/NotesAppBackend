package com.jsp.NotesApp.dto;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Entity
@Table(name = "\"user\"")  // Properly quote user
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private long phone;

	@Column(nullable = false)
	private String password;

	// (Optional, but recommended for bidirectional mapping)
	@OneToMany
	@JoinColumn(name = "user_id") // This should match the FK in the Note table
	private List<Note> notes;
}
